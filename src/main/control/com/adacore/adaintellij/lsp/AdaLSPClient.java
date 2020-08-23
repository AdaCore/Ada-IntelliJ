package com.adacore.adaintellij.lsp;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.swing.*;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerImpl;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.folding.impl.CodeFoldingManagerImpl;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.CompletableFutures;
import org.eclipse.lsp4j.services.LanguageClient;

import com.adacore.adaintellij.dialogs.ListChooserDialog;
import com.adacore.adaintellij.file.AdaFileType;
import com.adacore.adaintellij.misc.cache.*;
import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.adacore.adaintellij.Utils;

import static com.adacore.adaintellij.lsp.LSPUtils.messageTypeToNotificationType;

/**
 * LSP client implementation for the Ada-IntelliJ plugin.
 */
public final class AdaLSPClient implements LanguageClient {

	/**
	 * Cache key for document diagnostics.
	 */
	public static final CacheKey<List<Diagnostic>>
		DIAGNOSTICS_CACHE_KEY = CacheKey.getNewKey();

	/**
	 * Class-wide logger for the AdaLSPClient class.
	 */
	private static final Logger LOGGER = Logger.getInstance(AdaLSPClient.class);

	/**
	 * The LSP driver to which this client belongs.
	 */
	private AdaLSPDriver driver;

	/**
	 * The project in which this client runs.
	 */
	private Project project;

	/**
	 * Constructs a new AdaLSPClient given its driver and a project.
	 *
	 * @param driver The driver to attach to this client.
	 * @param project The project in which this client runs.
	 */
	AdaLSPClient(AdaLSPDriver driver, Project project) {
		this.driver  = driver;
		this.project = project;
	}

	/*
		Window
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#showMessage(MessageParams)
	 */
	@Override
	public void showMessage(MessageParams messageParams) {

		// Show the server message as a notification

		Notifications.Bus.notify(new AdaIJNotification(
			"Ada LSP Server",
			messageParams.getMessage(),
			messageTypeToNotificationType(messageParams.getType())
		));

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#showMessageRequest(ShowMessageRequestParams)
	 */
	@Override
	public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams requestParams) {

		// Show the server message and ask the user to
		// choose one of the given actions

		return CompletableFutures.computeAsync(cancelChecker ->
			new ListChooserDialog<>(
				project,
				"Action Required",
				requestParams.getMessage(),
				requestParams.getActions(),
				ListSelectionModel.SINGLE_SELECTION
			).showAndGetSelection()
		);

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#logMessage(MessageParams)
	 */
	@Override
	public void logMessage(MessageParams message) {

		String messageText = message.getMessage();

		// Log the server message

		switch (message.getType()) {

			case Error:
				LOGGER.error(messageText);
				break;

			case Warning:
				LOGGER.warn(messageText);
				break;

			case Info:
			case Log:
				LOGGER.info(messageText);
				break;

		}

	}

	/*
		Telemetry
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#telemetryEvent(Object)
	 */
	@Override
	public void telemetryEvent(Object object) { LOGGER.info(object.toString()); }

	/*
		Client
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#registerCapability(RegistrationParams)
	 */
	@Override
	public CompletableFuture<Void> registerCapability(RegistrationParams params) {

		if (!driver.initialized()) {
			return CompletableFuture.completedFuture(null);
		}

		throw new UnsupportedOperationException("Method: client/registerCapability");

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#unregisterCapability(UnregistrationParams)
	 */
	@Override
	public CompletableFuture<Void> unregisterCapability(UnregistrationParams params) {

		if (!driver.initialized()) {
			return CompletableFuture.completedFuture(null);
		}

		throw new UnsupportedOperationException("Method: client/unregisterCapability");

	}

	/*
		Workspace
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#workspaceFolders()
	 */
	@Override
	public CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {

		if (!driver.initialized()) {
			return CompletableFuture.completedFuture(null);
		}

		VirtualFile baseDir = project.getBaseDir();

		return CompletableFutures.computeAsync(
			cancelChecker -> Collections.singletonList(new WorkspaceFolder(
				baseDir.getPath(),
				baseDir.getName()
			))
		);

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#configuration(ConfigurationParams)
	 */
	@Override
	public CompletableFuture<List<Object>> configuration(ConfigurationParams configurationParams) {

		if (!driver.initialized()) {
			return CompletableFuture.completedFuture(null);
		}

		throw new UnsupportedOperationException("Method: workspace/configuration");

	}

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#applyEdit(ApplyWorkspaceEditParams)
	 */
	@Override
	public CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams params) {

		if (!driver.initialized()) {
			return CompletableFuture.completedFuture(null);
		}

		throw new UnsupportedOperationException("Method: workspace/applyEdit");

	}

	/*
		Diagnostics
	*/

	/**
	 * @see org.eclipse.lsp4j.services.LanguageClient#publishDiagnostics(PublishDiagnosticsParams)
	 */
	@Override
	public void publishDiagnostics(PublishDiagnosticsParams diagnostics) {

		if (!driver.initialized()) { return; }

		// Find the file corresponding to the given diagnostics'
		// document and check that it is an Ada source file

		final VirtualFile virtualFile = Utils.findFileByUrlString(diagnostics.getUri());

		if (virtualFile == null || !AdaFileType.isAdaFile(virtualFile)) { return; }

		// Get the corresponding document

		Document document = ApplicationManager.getApplication().runReadAction(
			(Computable<Document>)() -> Utils.getVirtualFileDocument(virtualFile));

		if (document == null) { return; }

		WriteCommandAction.runWriteCommandAction(this.project,() -> {

			Editor editor = FileEditorManager
				.getInstance(
					this.project
				).getSelectedTextEditor();

			CodeFoldingManagerImpl
				.getInstance(this.project)
				.scheduleAsyncFoldingUpdate(editor);

			Cacher.cacheData(
				document,
				DIAGNOSTICS_CACHE_KEY,
				diagnostics.getDiagnostics()
			);

			DaemonCodeAnalyzer.getInstance(this.project).restart(
				Utils.getVirtualFilePsiFile(
					this.project, virtualFile
				)
			);
		});
	}

}
