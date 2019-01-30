package com.adacore.adaintellij.lsp;

import java.util.Collections;
import java.util.List;

import org.eclipse.lsp4j.FailureHandlingKind;

/**
 * Capability settings for Ada-IntelliJ's integrated LSP client.
 *
 * The class structure directly reflects client capability
 * definitions in the LSP protocol.
 */
final class ClientCapabilities {

	private ClientCapabilities() {}

	/**
	 * Workspace capabilities.
	 */
	static final class Workspace {

		private Workspace() {}

		static final boolean APPLY_EDIT = false;

		static final class WorkspaceEdit {

			private WorkspaceEdit() {}

			static final boolean DOCUMENT_CHANGES = false;

			static final List<String> RESOURCE_OPERATIONS = Collections.emptyList();

			static final String FAILURE_HANDLING = FailureHandlingKind.Abort;

		}

		static final class DidChangeConfiguration {

			private DidChangeConfiguration() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class DidChangeWatchedFiles {

			private DidChangeWatchedFiles() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class Symbol {

			private Symbol() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final class SymbolKind {

				private SymbolKind() {}

				static final List<org.eclipse.lsp4j.SymbolKind> VALUE_SET =
					Collections.emptyList();

			}

		}

		static final class ExecuteCommand {

			private ExecuteCommand() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final boolean WORKSPACE_FOLDERS = false;

		static final boolean CONFIGURATION = false;

	}

	/**
	 * Text document capabilities.
	 */
	static final class TextDocument {

		private TextDocument() {}

		static final class Synchronization {

			private Synchronization() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final boolean WILL_SAVE = true;

			static final boolean WILL_SAVE_WAIT_UNTIL = true;

			static final boolean DID_SAVE = true;

		}

		static final class Completion {

			private Completion() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final class CompletionItem {

				private CompletionItem() {}

				static final boolean SNIPPET_SUPPORT = false;

				static final boolean COMMIT_CHARACTERS_SUPPORT = false;

				static final List<String> DOCUMENTATION_FORMAT = Collections.emptyList();

				static final boolean DEPRECATED_SUPPORT = false;

				static final boolean PRESELECT_SUPPORT = false;

			}

			static final class CompletionItemKind {

				private CompletionItemKind() {}

				static final List<org.eclipse.lsp4j.CompletionItemKind> VALUE_SET =
					Collections.emptyList();

			}

			static final boolean CONTEXT_SUPPORT = false;

		}

		static final class Hover {

			private Hover() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final List<String> CONTENT_FORMAT = Collections.emptyList();

		}

		static final class SignatureHelp {

			private SignatureHelp() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final class SignatureInformation {

				private SignatureInformation() {}

				static final List<String> DOCUMENTATION_FORMAT = Collections.emptyList();

			}

		}

		static final class References {

			private References() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class DocumentHighlight {

			private DocumentHighlight() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class DocumentSymbol {

			private DocumentSymbol() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final class SymbolKind {

				private SymbolKind() {}

				static final List<org.eclipse.lsp4j.SymbolKind> VALUE_SET =
					Collections.emptyList();

			}

			static final boolean HIERARCHICAL_DOCUMENT_SYMBOL_SUPPORT = false;

		}

		static final class Formatting {

			private Formatting() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class RangeFormatting {

			private RangeFormatting() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class OnTypeFormatting {

			private OnTypeFormatting() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class Definition {

			private Definition() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class TypeDefinition {

			private TypeDefinition() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class Implementation {

			private Implementation() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class CodeAction {

			private CodeAction() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final class CodeActionLiteralSupport {

				private CodeActionLiteralSupport() {}

				static final class CodeActionKind {

					private CodeActionKind() {}

					static final List<String> VALUE_SET = Collections.emptyList();

				}

			}

		}

		static final class CodeLens {

			private CodeLens() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class DocumentLink {

			private DocumentLink() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class ColorProvider {

			private ColorProvider() {}

			static final boolean DYNAMIC_REGISTRATION = false;

		}

		static final class Rename {

			private Rename() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final boolean PREPARE_SUPPORT = false;

		}

		static final class PublishDiagnostics {

			private PublishDiagnostics() {}

			static final boolean RELATED_INFORMATION = false;

		}

		static final class FoldingRange {

			private FoldingRange() {}

			static final boolean DYNAMIC_REGISTRATION = false;

			static final int RANGE_LIMIT = 0;

			static final boolean LINE_FOLDING_ONLY = false;

		}

	}

}
