package com.adacore.adaintellij.lsp;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import com.intellij.notification.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.jetbrains.annotations.*;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageServer;

import com.adacore.adaintellij.file.AdaFileType;
import com.adacore.adaintellij.lsp.objects.AdaSettingsObject;
import com.adacore.adaintellij.notifications.AdaIJNotification;

import static com.adacore.adaintellij.Utils.*;

/**
 * Public API of the Ada Language Server (ALS) within the
 * Ada-IntelliJ plugin, internally redirecting requests to
 * the ALS using LSP4J's generic server interface.
 */
public final class AdaLSPServer {

	/**
	 * Class-wide logger for the AdaLSPServer class.
	 */
	private static Logger LOGGER = Logger.getInstance(AdaLSPServer.class);

	/**
	 * Empty result lists returned by servers.
	 */
	private static final List<TextEdit>       EMPTY_TEXT_EDIT_LIST       = Collections.emptyList();
	private static final List<CompletionItem> EMPTY_COMPLETION_ITEM_LIST = Collections.emptyList();
	private static final List<Location>       EMPTY_LOCATION_LIST        = Collections.emptyList();
	private static final List<DocumentSymbol> EMPTY_DOCUMENT_SYMBOL_LIST = Collections.emptyList();
	private static final List<FoldingRange>   EMPTY_FOLDING_RANGE_LIST   = Collections.emptyList();

	/**
	 * The LSP driver to which this server belongs.
	 */
	private AdaLSPDriver driver;

	/**
	 * The actual LSP4J server represented by this server interface.
	 */
	private LanguageServer server;

	/**
	 * Server capabilities.
	 */
	private ServerCapabilities      capabilities;
	private TextDocumentSyncOptions serverSyncPolicy;

	/**
	 * Whether or not initialization request and notification have
	 * already been sent.
	 */
	private boolean initializeRequestSent       = false;
	private boolean initializedNotificationSent = false;

	/**
	 * The number of failed requests to the server.
	 */
	private int failureCount = 0;

	/**
	 * The set of open files in the IDE.
	 */
	private Set<String> openFiles = new HashSet<>();

	/**
	 * Constructs a new AdaLSPServer given its driver and the corresponding
	 * internal LSP4J server.
	 *
	 * @param driver The driver to attach to this server.
	 * @param server The internal server corresponding to this server.
	 */
	AdaLSPServer(@NotNull AdaLSPDriver driver, @NotNull LanguageServer server) {
		this.driver = driver;
		this.server = server;
	}

	/**
	 * Sets the server capabilities. Also determines capabilities that
	 * may be communicated by the server in different possible ways (e.g.
	 * document synchronization) and stores those capabilities for easier
	 * access.
	 *
	 * @param capabilities The server capabilities to set.
	 */
	void setCapabilities(ServerCapabilities capabilities) {

		// Store server capabilities

		this.capabilities = capabilities;

		// Determine server synchronization policy

		Either<TextDocumentSyncKind, TextDocumentSyncOptions> syncOptions =
			capabilities.getTextDocumentSync();

		Boolean              openClose         = null;
		TextDocumentSyncKind change            = null;
		Boolean              willSave          = null;
		Boolean              willSaveWaitUntil = null;
		SaveOptions          save              = null;

		if (syncOptions.isRight()) {

			serverSyncPolicy = syncOptions.getRight();

			assert serverSyncPolicy != null;

			openClose         = serverSyncPolicy.getOpenClose();
			change            = serverSyncPolicy.getChange();
			willSave          = serverSyncPolicy.getWillSave();
			willSaveWaitUntil = serverSyncPolicy.getWillSaveWaitUntil();
			save              = serverSyncPolicy.getSave();

		} else if (syncOptions.isLeft()) {

			change = syncOptions.getLeft();

			serverSyncPolicy = new TextDocumentSyncOptions();

			if (change == TextDocumentSyncKind.Full ||
				change == TextDocumentSyncKind.Incremental)
			{
				openClose         = true;
				willSave          = true;
				willSaveWaitUntil = false;
				save              = new SaveOptions(change == TextDocumentSyncKind.Full);
			}

		}

		serverSyncPolicy.setOpenClose(openClose != null && openClose);
		serverSyncPolicy.setChange(change == null ? TextDocumentSyncKind.None : change);
		serverSyncPolicy.setWillSave(willSave != null && willSave);
		serverSyncPolicy.setWillSaveWaitUntil(willSaveWaitUntil != null && willSaveWaitUntil);

		if (save != null && save.getIncludeText() == null) {
			save.setIncludeText(false);
		}

		serverSyncPolicy.setSave(save);

	}

	/**
	 * Generic request wrapper allowing to systematically perform certain
	 * operations on every request, such as logging and keeping track of failed
	 * requests.
	 * Waits for the response to the given request, and returns its result.
	 * The given supplier should be a simple wrapper around a server request,
	 * for example (using a Java lambda for the Supplier anonymous class):
	 *
	 * () -> server.getTextDocumentService().definition(params)
	 *
	 * @param method The name of the request's method.
	 * @param requestSupplier A supplier representing the request to be made.
	 * @param <T> The type of the request's response result.
	 * @return The result of the response to the request.
	 */
	@Nullable
	private <T> T request(
		@NotNull String method,
		@NotNull Supplier<CompletableFuture<T>> requestSupplier
	) {

		T result = null;

		// Get the request future

		CompletableFuture<T> requestFuture = requestSupplier.get();

		// Get the timeout for the given method

		int requestTimeout = Timeouts.getMethodTimeout(method);

		// Keep looping to get the request's result
		// while the method timeout is not reached

		while (requestTimeout > 0) {

			try {

				// Try to make the request and get the result before
				// the end of the next check-cancel interval

				result = requestFuture.get(
					AdaLSPDriver.CHECK_CANCELED_INTERVAL,
					TimeUnit.MILLISECONDS
				);

				// The request resolved before the end of the next
				// check-cancel interval, so break immediately

				break;

			} catch (TimeoutException timeoutException) {

				// The check-cancel interval is over, so decrease the
				// request timeout then check if the operation was
				// canceled, and if it was then break

				requestTimeout -= AdaLSPDriver.CHECK_CANCELED_INTERVAL;

				try {
					ProgressManager.checkCanceled();
				} catch (ProcessCanceledException canceledException) {
					break;
				}

			} catch (Exception exception) {

				// Log the failed request

				LOGGER.error("Request '" + method + "' to ALS failed", exception);

				// Increment the number of failed requests

				failureCount++;

				// If the number of failed requests reaches the threshold defined in
				// the driver, then notify the user and shut down the server

				if (failureCount == AdaLSPDriver.FAILURE_COUNT_THRESHOLD) {

					Notifications.Bus.notify(new AdaIJNotification(
						"Connection to Ada Language Server unreliable",
						"The ALS has been shut down due to multiple failed requests, " +
							"which will disable smart features such as find-usages and " +
							"code completion.\nReload the current project to try again.",
						NotificationType.ERROR
					));

					driver.shutDownServer();

				}

				// Break out of the loop

				break;

			}

		}

		// Return the result

		return result;

	}

	/**
	 * Wrapper around requests that are relative to a document.
	 * Basically any request whose parameters specify a document URI must be made
	 * indirectly through this method and NOT directly. This is because the
	 * IntelliJ platform often performs certain operations, such as resolving a
	 * reference or performing find-references, on elements from files that are
	 * not actually open in the IDE, which would be problematic if those files
	 * were not open in the server's perspective.
	 * To solve this, this wrapper checks if the file referenced by the given
	 * request is already open and, if it is not, sends a `textDocument/didOpen`
	 * notification before performing the request and a `textDocument/didClose`
	 * notification after.
	 * See base request wrapper for information about expected parameters.
	 *
	 * @param method The name of the request's method.
	 * @param documentUri The URI of the document referenced by the given request.
	 * @param requestSupplier A supplier representing the request to be made.
	 * @param <T> The type of the request's response result.
	 * @return The result of the response to the request.
	 */
	@Nullable
	private <T> T documentRequest(
		@NotNull String method,
		@NotNull String documentUri,
		@NotNull Supplier<CompletableFuture<T>> requestSupplier
	) {

		boolean openOnlyForRequest =
			serverSyncPolicy.getOpenClose() &&
			!openFiles.contains(documentUri);

		// If the file is not already open, send a `textDocument/didOpen`
		// notification to tell the server that the file is open

		if (openOnlyForRequest) {
			didOpen(documentUri);
		}

		// Make the request and store the result

		T result = request(method, requestSupplier);

		// If the file was not already open, send a `textDocument/didClose`
		// notification to tell the server that the file is closed

		if (openOnlyForRequest) {
			didClose(documentUri);
		}

		// Return the result

		return result;

	}

	/*
		General methods
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageServer#initialize(InitializeParams)
	 */
	@Nullable
	InitializeResult initialize(InitializeParams params) {

		if (initializeRequestSent) { return null; }

		initializeRequestSent = true;

		return request("initialize", () -> server.initialize(params));

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageServer#initialized(InitializedParams)
	 */
	void initialized(InitializedParams params) {

		if (initializedNotificationSent) { return; }

		initializedNotificationSent = true;

		server.initialized(params);

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageServer#shutdown()
	 */
	void shutdown() { request("shutdown", () -> server.shutdown()); }

	/**
	 * @see org.eclipse.lsp4j.services.LanguageServer#exit()
	 */
	void exit() { server.exit(); }

	/*
		'workspace/_' methods
	*/

	/**
	 * @see org.eclipse.lsp4j.services.WorkspaceService#didChangeConfiguration(DidChangeConfigurationParams)
	 */
	void didChangeConfiguration(
		@NotNull  String              gprFilePath,
		@Nullable Map<String, String> scenarioVariables
	) {

		AdaSettingsObject adaSettingsObject = new AdaSettingsObject();

		adaSettingsObject.setProjectFile(gprFilePath);

		if (scenarioVariables != null) {
			adaSettingsObject.setScenarioVariables(scenarioVariables);
		}

		server.getWorkspaceService().didChangeConfiguration(
			new DidChangeConfigurationParams(adaSettingsObject));

	}

	/*
		'textDocument/_' methods
	*/

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didOpen(DidOpenTextDocumentParams)
	 */
	void didOpen(@NotNull String documentUri) {

		VirtualFile file = findFileByUrlString(documentUri);

		if (file == null) { return; }

		didOpen(file);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didOpen(DidOpenTextDocumentParams)
	 */
	void didOpen(@NotNull VirtualFile file) {

		if (!serverSyncPolicy.getOpenClose() ||
			!AdaFileType.isAdaFile(file)) { return; }

		String   documentUri = file.getUrl();
		Document document    = getVirtualFileDocument(file);

		if (document == null) { return; }

		TextDocumentItem textDocumentItem = new TextDocumentItem(
			documentUri, LSPUtils.ADA_LSP_LANGUAGE_ID, 1, document.getText());

		server.getTextDocumentService().didOpen(new DidOpenTextDocumentParams(textDocumentItem));

		openFiles.add(documentUri);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didChange(DidChangeTextDocumentParams)
	 */
	void didChange(@NotNull List<DocumentEvent> events) {

		TextDocumentSyncKind changePolicy = serverSyncPolicy.getChange();

		// If the server's change sync policy is "None",
		// then return

		if (
			changePolicy == TextDocumentSyncKind.None ||
			events.size() == 0
		) { return; }

		// Get the changed document from the first event and
		// check that all events are for the same document

		final Document changedDocument = events.get(0).getDocument();

		if (events.stream()
			.map(DocumentEvent::getDocument)
			.anyMatch(document -> !documentsRepresentSameFile(document, changedDocument)))
		{
			LOGGER.error("Trying to send a `textDocument/didChange` notification " +
				"with change events from different documents");
		}

		// Get the changed document's corresponding file
		// and check that it is an Ada file

		VirtualFile changedFile = getDocumentVirtualFile(changedDocument);

		if (changedFile == null || !AdaFileType.isAdaFile(changedFile)) { return; }

		List<TextDocumentContentChangeEvent> changeEvents;

		// If the server's sync change policy is "Incremental",
		// then map the given document events to LSP change events

		if (changePolicy == TextDocumentSyncKind.Incremental) {

			changeEvents = events.stream()
				.map(LSPUtils::documentEventToContentChangeEvent)
				.collect(Collectors.toList());

		}

		// Otherwise, if the server's sync change policy is "Full",
		// then create a single LSP change event with the full text
		// of the changed document

		else if (changePolicy == TextDocumentSyncKind.Full) {

			changeEvents = Collections.singletonList(
				new TextDocumentContentChangeEvent(changedDocument.getText()));

		}

		// Will never execute
		else { return; }

		// Send the notification with the computed changes and
		// and the changed document's virtual file modification
		// stamp as document version

		didChange(
			changedFile.getUrl(),
			(int)changedFile.getModificationStamp(),
			changeEvents
		);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didChange(DidChangeTextDocumentParams)
	 */
	private void didChange(
		@NotNull String documentUri,
		         int    version,
		@NotNull List<TextDocumentContentChangeEvent> changeEvents
	) {

		TextDocumentSyncKind changePolicy = serverSyncPolicy.getChange();

		// If the server's sync change policy is "None",
		// then return

		if (changePolicy == TextDocumentSyncKind.None) { return; }

		// Create notification parameters

		DidChangeTextDocumentParams params = new DidChangeTextDocumentParams();

		params.setTextDocument(
			new VersionedTextDocumentIdentifier(documentUri, version));
		params.setContentChanges(changeEvents);

		// Send the notification

		server.getTextDocumentService().didChange(params);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#willSave(WillSaveTextDocumentParams)
	 */
	void willSave(@NotNull String documentUri) {

		if (!serverSyncPolicy.getWillSave()) { return; }

		WillSaveTextDocumentParams params = new WillSaveTextDocumentParams();

		params.setTextDocument(new TextDocumentIdentifier(documentUri));
		params.setReason(TextDocumentSaveReason.Manual);

		server.getTextDocumentService().willSave(params);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#willSaveWaitUntil(WillSaveTextDocumentParams)
	 */
	@NotNull
	List<TextEdit> willSaveWaitUntil(@NotNull VirtualFile file) {

		if (!serverSyncPolicy.getWillSaveWaitUntil() ||
			!AdaFileType.isAdaFile(file))
		{ return EMPTY_TEXT_EDIT_LIST; }

		String documentUri = file.getUrl();

		final WillSaveTextDocumentParams params = new WillSaveTextDocumentParams();

		params.setTextDocument(new TextDocumentIdentifier(documentUri));
		params.setReason(TextDocumentSaveReason.Manual);

		List<TextEdit> textEdits =
			documentRequest("textDocument/willSaveWaitUntil", documentUri,
				() -> server.getTextDocumentService().willSaveWaitUntil(params));

		return textEdits == null ? EMPTY_TEXT_EDIT_LIST : textEdits;

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didSave(DidSaveTextDocumentParams)
	 */
	void didSave(@NotNull VirtualFile file) {

		SaveOptions saveOptions = serverSyncPolicy.getSave();

		if (saveOptions == null ||
			!AdaFileType.isAdaFile(file)) { return; }

		DidSaveTextDocumentParams params = new DidSaveTextDocumentParams();

		params.setTextDocument(new TextDocumentIdentifier(file.getUrl()));

		if (saveOptions.getIncludeText()) {

			Document document = getVirtualFileDocument(file);

			if (document != null) {
				params.setText(document.getText());
			}

		}

		server.getTextDocumentService().didSave(params);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didClose(DidCloseTextDocumentParams)
	 */
	void didClose(@NotNull String documentUri) {

		VirtualFile file = findFileByUrlString(documentUri);

		if (file == null) { return; }

		didClose(file);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#didClose(DidCloseTextDocumentParams)
	 */
	void didClose(@NotNull VirtualFile file) {

		if (!serverSyncPolicy.getOpenClose() ||
			!AdaFileType.isAdaFile(file)) { return; }

		String documentUri = file.getUrl();

		server.getTextDocumentService().didClose(
			new DidCloseTextDocumentParams(new TextDocumentIdentifier(documentUri)));

		openFiles.remove(documentUri);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#completion(CompletionParams)
	 */
	@NotNull
	public List<CompletionItem> completion(
		@NotNull String   documentUri,
		@NotNull Position position
	) {

		if (!driver.initialized() || capabilities.getCompletionProvider() == null) {
			return EMPTY_COMPLETION_ITEM_LIST;
		}

		final CompletionParams params = new CompletionParams();

		params.setTextDocument(new TextDocumentIdentifier(documentUri));
		params.setPosition(position);

		Either<List<CompletionItem>, CompletionList> completionResult =
			documentRequest("textDocument/completion", documentUri,
				() -> server.getTextDocumentService().completion(params));

		if (completionResult == null) { return EMPTY_COMPLETION_ITEM_LIST; }

		return
			completionResult.isLeft()  ? completionResult.getLeft() :
			completionResult.isRight() ? completionResult.getRight().getItems() :
				EMPTY_COMPLETION_ITEM_LIST;

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#definition(TextDocumentPositionParams)
	 */
	@Nullable
	public Location definition(@NotNull String documentUri, @NotNull Position position) {

		if (!driver.initialized() || !capabilities.getDefinitionProvider()) {
			return null;
		}

		final TextDocumentPositionParams params = new TextDocumentPositionParams(
			new TextDocumentIdentifier(documentUri), position);

		List<? extends Location> locations =
			documentRequest("textDocument/definition", documentUri,
				() -> server.getTextDocumentService().definition(params));

		if (locations == null || locations.size() == 0) { return null; }

		// TODO: Decide how to handle multiple locations
		return locations.get(0);

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#references(ReferenceParams)
	 */
	@NotNull
	public List<Location> references(
		@NotNull String   documentUri,
		@NotNull Position position,
		         boolean  includeDefinition
	) {

		if (!driver.initialized() ||
			!capabilities.getReferencesProvider())
		{ return EMPTY_LOCATION_LIST; }

		final ReferenceParams params = new ReferenceParams();

		params.setTextDocument(new TextDocumentIdentifier(documentUri));
		params.setPosition(position);
		params.setContext(new ReferenceContext(includeDefinition));

		List<? extends Location> locations =
			documentRequest("textDocument/references", documentUri,
				() -> server.getTextDocumentService().references(params));

		if (locations == null) { return EMPTY_LOCATION_LIST; }

		return locations
			.stream()
			.map(location -> (Location)location)
			.collect(Collectors.toList());

	}

	/**
	 * @see org.eclipse.lsp4j.services.TextDocumentService#documentSymbol(DocumentSymbolParams)
	 */
	public List<DocumentSymbol> documentSymbol(@NotNull String documentUri) {

		if (!driver.initialized() ||
			!capabilities.getDocumentSymbolProvider())
		{ return EMPTY_DOCUMENT_SYMBOL_LIST; }

		final DocumentSymbolParams params = new DocumentSymbolParams(
			new TextDocumentIdentifier(documentUri));

		List<Either<SymbolInformation, DocumentSymbol>> symbols =
			documentRequest("textDocument/documentSymbol", documentUri,
				() -> server.getTextDocumentService().documentSymbol(params));

		if (symbols == null) { return EMPTY_DOCUMENT_SYMBOL_LIST; }

		return symbols
			.stream()
			.map(either -> {

				// If it is a `DocumentSymbol`, return it directly

				if (either.isRight()) {
					return either.getRight();
				}

				// Else if it is not a `SymbolInformation` return null

				else if (!either.isLeft()) {
					return null;
				}

				// Else, translate the `SymbolInformation` to a `DocumentSymbol`

				SymbolInformation information = either.getLeft();
				DocumentSymbol    symbol      = new DocumentSymbol();

				Range symbolRange = information.getLocation().getRange();

				symbol.setName(information.getName());
				symbol.setKind(information.getKind());
				symbol.setRange(symbolRange);
				symbol.setSelectionRange(symbolRange);

				return symbol;

			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

	}

	public List<FoldingRange> foldingRange(@NotNull String documentUri) {

		final FoldingRangeRequestParams params = new FoldingRangeRequestParams(new TextDocumentIdentifier(documentUri));

		List<FoldingRange> ranges = documentRequest("textDocument/foldingRange", documentUri,
			() -> server.getTextDocumentService().foldingRange(params));

		if (ranges == null) { return EMPTY_FOLDING_RANGE_LIST; }

		return ranges;
	}

}
