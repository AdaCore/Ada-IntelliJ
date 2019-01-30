package com.adacore.adaintellij.analysis.semantic.diagnostics;

import java.util.List;

import com.intellij.lang.annotation.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.*;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.Range;

import com.adacore.adaintellij.lsp.*;
import com.adacore.adaintellij.misc.cache.*;
import com.adacore.adaintellij.Utils;

import static com.adacore.adaintellij.lsp.LSPUtils.diagnosticSeverityToHighlightSeverity;

/**
 * Annotator for Ada source code, powered by the
 * Ada Language Server (ALS).
 */
public class AdaAnnotator extends ExternalAnnotator<List<Diagnostic>, List<Diagnostic>> {
	
	/**
	 * Maximum number of attempts to get a document's diagnostics.
	 */
	private static final int MAXIMUM_GET_DIAGNOSTICS_ATTEMPTS = 7;
	
	/**
	 * The interval duration, in milliseconds, before trying to get
	 * a document's internally stored diagnostics.
	 */
	private static final long GET_DIAGNOSTICS_INTERVAL = 100;
	
	/**
	 * @see com.intellij.lang.annotation.ExternalAnnotator#collectInformation(PsiFile)
	 *
	 * Fetches and returns the list of diagnostics from the LSP client.
	 */
	@Nullable
	@Override
	public List<Diagnostic> collectInformation(@NotNull PsiFile file) {
		
		// Get the file's corresponding document
		
		Document document = Utils.getPsiFileDocument(file);
		
		if (document == null) { return null; }
		
		// Get the list of diagnostics from the document's cache
		
		CacheResult<List<Diagnostic>> cacheResult =
			Cacher.getCachedData(document, AdaLSPClient.DIAGNOSTICS_CACHE_KEY);
		
		int attempts = MAXIMUM_GET_DIAGNOSTICS_ATTEMPTS;
		
		// While the cache read was a miss and the number of attempts
		// has not reached the maximum number of attempts, sleep for
		// a short duration of time and try again
		
		while (!cacheResult.hit) {
			
			try {
				Thread.sleep(GET_DIAGNOSTICS_INTERVAL);
			} catch (InterruptedException exception) {}
			
			cacheResult = Cacher.getCachedData(document, AdaLSPClient.DIAGNOSTICS_CACHE_KEY);
			
			attempts--;
			
			if (attempts == 0) { break; }
			
		}
		
		// Return the last cache read result
		
		return cacheResult.data;
		
	}
	
	/**
	 * @see com.intellij.lang.annotation.ExternalAnnotator#collectInformation(PsiFile, Editor, boolean)
	 */
	@Nullable
	@Override
	public List<Diagnostic> collectInformation(
		@NotNull PsiFile file,
		@NotNull Editor  editor,
		         boolean hasErrors
	) { return collectInformation(file); }
	
	/**
	 * @see com.intellij.lang.annotation.ExternalAnnotator#doAnnotate(Object)
	 */
	@Nullable
	@Override
	public List<Diagnostic> doAnnotate(List<Diagnostic> collectedInfo) {
		return collectedInfo;
	}
	
	/**
	 * @see com.intellij.lang.annotation.ExternalAnnotator#apply(PsiFile, Object, AnnotationHolder)
	 */
	@Override
	public void apply(
		@NotNull  PsiFile          file,
		@Nullable List<Diagnostic> annotationResult,
		@NotNull  AnnotationHolder holder
	) {
		
		// Check that there are diagnostics to process
		// before continuing
		
		if (annotationResult == null || annotationResult.size() == 0) {
			return;
		}
		
		// Get the file's document
		
		Document document = Utils.getPsiFileDocument(file);
		
		if (document == null) { return; }
		
		// For each diagnostic...
		
		for (Diagnostic diagnostic : annotationResult) {
			
			// Compute the start and end offsets given the
			// range of the diagnostic
			
			Range diagnosticRange = diagnostic.getRange();
			
			int startOffset = LSPUtils.positionToOffset(document, diagnosticRange.getStart());
			int endOffset   = LSPUtils.positionToOffset(document, diagnosticRange.getEnd());
			
			// Get the diagnostic severity and message
			// If the severity is not set, consider it an error
			
			DiagnosticSeverity severity = diagnostic.getSeverity();
			
			if (severity == null) {
				severity = DiagnosticSeverity.Error;
			}
			
			String message = diagnostic.getMessage();
			
			// Create an annotation based on the diagnostic data
			
			holder.createAnnotation(
				diagnosticSeverityToHighlightSeverity(severity),
				new TextRange(startOffset, endOffset),
				message == null ? severity.name() : message
			);
			
		}
		
	}
	
}
