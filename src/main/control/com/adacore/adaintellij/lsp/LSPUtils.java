package com.adacore.adaintellij.lsp;

import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Document;
import org.jetbrains.annotations.*;

import org.eclipse.lsp4j.*;

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
	@Contract(pure = true)
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
