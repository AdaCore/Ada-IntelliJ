package com.adacore.adaintellij.lsp;

import com.adacore.adaintellij.editor.AdaDocumentEvent;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.impl.DocumentImpl;
import org.jetbrains.annotations.*;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.Range;

import static com.adacore.adaintellij.analysis.syntactic.AdaPsiElement.AdaElementType;

/**
 * LSP-specific utilities.
 */
public final class LSPUtils {

	/**
	 * The LSP language ID for Ada.
	 */
	static final String ADA_LSP_LANGUAGE_ID = "ada";

	/**
	 * The extension-less name of the Ada Language Server (ALS) executable.
	 */
	static final String ALS_NAME = "ada_language_server";

	/**
	 * Private default constructor to prevent instantiation.
	 */
	private LSPUtils() {}

	/**
	 * Translates the given message type, defined in the LSP standard,
	 * to the notification type used in the IntelliJ platform.
	 *
	 * @param type The message type to translate.
	 * @return The corresponding notification type.
	 */
	@NotNull
	public static NotificationType messageTypeToNotificationType(@NotNull MessageType type) {

		switch (type) {

			case Error:   return NotificationType.ERROR;

			case Warning: return NotificationType.WARNING;

			case Info:
			case Log:
			default:      return NotificationType.INFORMATION;

		}

	}

	/**
	 * Translates the given diagnostic severity, defined in the LSP
	 * standard, to the severity type used by the IntelliJ platform's
	 * annotation holders.
	 *
	 * @param severity The diagnostic severity to translate.
	 * @return The corresponding annotation severity.
	 */
	@NotNull
	public static HighlightSeverity diagnosticSeverityToHighlightSeverity(
		@NotNull DiagnosticSeverity severity
	) {

		switch (severity) {

			case Warning:     return HighlightSeverity.WARNING;

			case Information: return HighlightSeverity.INFORMATION;

			case Hint:        return HighlightSeverity.WEAK_WARNING;

			case Error:
			default:          return HighlightSeverity.ERROR;

		}

	}

	/**
	 * Translates the symbol kind of the given symbol, defined in the
	 * LSP standard, to the Ada element type defined for Ada PSI
	 * elements.
	 * @see LSPUtils#symbolKindToAdaElementType(SymbolKind)
	 *
	 * @param symbol The symbol whose kind to translate.
	 * @return The corresponding Ada element type.
	 */
	@Nullable
	public static AdaElementType symbolKindToAdaElementType(@NotNull DocumentSymbol symbol) {

		SymbolKind symbolKind = symbol.getKind();

		return symbolKind == null ? null :
			symbolKindToAdaElementType(symbolKind);

	}

	/**
	 * Translates the given symbol kind, defined in the LSP standard,
	 * to the Ada element type defined for Ada PSI elements.
	 * @see com.adacore.adaintellij.analysis.syntactic.AdaPsiElement.AdaElementType
	 *
	 * @param kind The symbol kind to translate.
	 * @return The corresponding Ada element type.
	 */
	@Nullable
	public static AdaElementType symbolKindToAdaElementType(@NotNull SymbolKind kind) {

		switch (kind) {

			case Package:  return AdaElementType.PACKAGE_SPEC_IDENTIFIER;
			case Module:   return AdaElementType.PACKAGE_BODY_IDENTIFIER;

			case Class:    return AdaElementType.TYPE_IDENTIFIER;
			case Constant: return AdaElementType.CONSTANT_IDENTIFIER;
			case Variable: return AdaElementType.VARIABLE_IDENTIFIER;

			case Function: return AdaElementType.FUNCTION_IDENTIFIER;

			default:       return null;

		}

	}

	/**
	 * Translates the given IntelliJ platform document event to a
	 * text-document content change event, defined in the LSP
	 * standard.
	 *
	 * @param adaDocEvent The document event to translate.
	 * @return The corresponding text-document content change event.
	 */
	@NotNull
	public static TextDocumentContentChangeEvent
		documentEventToContentChangeEvent(@NotNull AdaDocumentEvent adaDocEvent)
	{
		DocumentEvent event = adaDocEvent.getDocumentEvent();

		TextDocumentContentChangeEvent changeEvent =
			new TextDocumentContentChangeEvent();

		int offset    = event.getOffset();
		int oldLength = event.getOldLength();
		Document copyOfOldDoc = new DocumentImpl(adaDocEvent.getPreviousContent());

		changeEvent.setRange(new Range(
			offsetToPosition(copyOfOldDoc, offset),
			offsetToPosition(copyOfOldDoc, offset + oldLength)
		));
		changeEvent.setRangeLength(oldLength);
		changeEvent.setText(event.getNewFragment().toString());

		return changeEvent;

	}

	/**
	 * Returns the offset corresponding to the given LSP position
	 * in the given document.
	 *
	 * @param document The reference document.
	 * @param position The LSP position to translate.
	 * @return The corresponding offset.
	 */
	public static int positionToOffset(@NotNull Document document, @NotNull Position position) {

		int line   = position.getLine();
		int column = position.getCharacter();

		return document.getLineStartOffset(line) + column;

	}

	/**
	 * Returns the LSP position corresponding to the given offset
	 * in the given document.
	 *
	 * @param document The reference document.
	 * @param offset The offset to translate.
	 * @return The corresponding LSP position.
	 */
	public static Position offsetToPosition(@NotNull Document document, int offset) {

		int line   = document.getLineNumber(offset);
		int column = offset - document.getLineStartOffset(line);

		return new Position(line, column);

	}

}
