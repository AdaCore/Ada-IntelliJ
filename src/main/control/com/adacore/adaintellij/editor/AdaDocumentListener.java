package com.adacore.adaintellij.editor;

import com.intellij.openapi.editor.event.*;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.file.AdaFileType;
import com.adacore.adaintellij.Utils;

/**
 * Document listener for Ada source file documents.
 * Listeners of this class will discard events originating from
 * documents that do not correspond to Ada source files, as well as
 * events corresponding to internal document manoeuvres performed by
 * the IntelliJ platform to provide certain features.
 * An example of such manoeuvres is the temporary insertion of dummy
 * text (e.g. "IntellijIdeaRulezzz") in a document corresponding to
 * an in-memory copy of the active document's virtual file during
 * code completion or renaming.
 */
public class AdaDocumentListener implements DocumentListener {
	
	/**
	 * @see com.intellij.openapi.editor.event.DocumentListener#beforeDocumentChange(DocumentEvent)
	 */
	@Override
	public final void beforeDocumentChange(DocumentEvent event) {
		
		if (!shouldHandleEvent(event)) { return; }
		
		beforeAdaDocumentChanged(event);
		
	}
	
	/**
	 * @see com.intellij.openapi.editor.event.DocumentListener#documentChanged(DocumentEvent)
	 */
	@Override
	public final void documentChanged(DocumentEvent event) {
		
		if (!shouldHandleEvent(event)) { return; }
		
		adaDocumentChanged(event);
		
	}
	
	/**
	 * Returns whether or not the given document event should be
	 * handled.
	 *
	 * @param event The document event to test.
	 * @return Whether or not the given event should be handled.
	 */
	private boolean shouldHandleEvent(@Nullable DocumentEvent event) {
		
		if (event == null) { return false; }
		
		VirtualFile file = Utils.getDocumentVirtualFile(event.getDocument());
		
		return file != null &&
			file.isInLocalFileSystem() &&
			AdaFileType.isAdaFile(file);
		
	}
	
	/**
	 * Variant of `beforeDocumentChanged` for Ada documents:
	 * @see com.intellij.openapi.editor.event.DocumentListener#beforeDocumentChange(DocumentEvent)
	 */
	public void beforeAdaDocumentChanged(@NotNull DocumentEvent event) {}
	
	/**
	 * Variant of `documentChanged` for Ada documents:
	 * @see com.intellij.openapi.editor.event.DocumentListener#documentChanged(DocumentEvent)
	 */
	public void adaDocumentChanged(@NotNull DocumentEvent event) {}
	
}
