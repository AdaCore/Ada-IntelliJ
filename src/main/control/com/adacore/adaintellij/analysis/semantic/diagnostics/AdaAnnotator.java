package com.adacore.adaintellij.analysis.semantic.diagnostics;

import java.util.List;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.Range;

import com.adacore.adaintellij.lsp.AdaLSPClient;
import com.adacore.adaintellij.lsp.AdaLSPDriver;
import com.adacore.adaintellij.lsp.LSPUtils;
import com.adacore.adaintellij.Utils;

/**
 * Annotator for Ada source code, powered by the
 * Ada Language Server (ALS).
 */
public class AdaAnnotator implements Annotator {
	
	/**
	 * @see com.intellij.lang.annotation.Annotator#annotate(PsiElement, AnnotationHolder)
	 *
	 * This method simply fetches the list of diagnostics available
	 * for the given element's document from the LSP client and looks
	 * for diagnostics with the same range as the given element to
	 * create annotations out of them.
	 *
	 * TODO: Improve the performance of the annotation process
	 * TODO: Set the annotation type based on the diagnostic severity
	 * TODO: Find a way to keep track of which diagnostics were
	 *       already "transformed" into annotations, and create
	 *       range-based annotations for the remaining diagnostics
	 */
	@Override
	public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
		
		// Get the document containing element
		
		Document document = Utils.getPsiFileDocument(element.getContainingFile());
		
		if (document == null) { return; }
		
		// Get the LSP diagnostics for the document
		
		AdaLSPClient lspClient = AdaLSPDriver.getClient(element.getProject());
		
		if (lspClient == null) { return; }
		
		List<Diagnostic> diagnostics = lspClient.getDiagnostics(document);
		
		// Element start/end offsets
		int elementStartOffset = element.getTextOffset();
		int elementEndOffset   = elementStartOffset + element.getTextLength();
		
		// For each of the document's diagnostics...
		
		for (Diagnostic diagnostic : diagnostics) {
		
			// Compute the start and end offsets given the
			// range of the diagnostic
			
			Range diagnosticRange = diagnostic.getRange();
			
			int diagnosticStartOffset = LSPUtils.positionToOffset(document, diagnosticRange.getStart());
			int diagnosticEndOffset   = LSPUtils.positionToOffset(document, diagnosticRange.getEnd());
			
			// Check if the offsets match those of the element
			
			if (
				elementStartOffset != diagnosticStartOffset ||
				elementEndOffset   != diagnosticEndOffset
			) { continue; }
			
			// Get the diagnostic message and create an error annotation
			
			String message = diagnostic.getMessage();
			
			if (message == null) {
				message = "Error";
			}
			
			holder.createErrorAnnotation(element, message);
			
		}
		
	}
	
}
