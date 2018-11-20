package com.adacore.adaintellij.lsp;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.editor.Document;
import org.jetbrains.annotations.NotNull;

import org.eclipse.lsp4j.MessageType;
import org.eclipse.lsp4j.Position;

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
	 * Returns an IntelliJ notification type corresponding to the given
	 * LSP message type.
	 *
	 * @param type The message type to translate to a notification type.
	 * @return The corresponding IntelliJ notification type.
	 */
	static NotificationType messageTypeToNotificationType(MessageType type) {
		return
			type == MessageType.Error   ? NotificationType.ERROR :
			type == MessageType.Warning ? NotificationType.WARNING :
				NotificationType.INFORMATION;
	}
	
	/**
	 * Returns a URL string corresponding to the given file path.
	 *
	 * @param filePath The file path to translate to a URL string.
	 * @return The corresponding URL string.
	 */
	static String pathToUri(@NotNull String filePath) {
		return "file:" + (filePath.startsWith("/") ? "//" : "") + filePath;
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
