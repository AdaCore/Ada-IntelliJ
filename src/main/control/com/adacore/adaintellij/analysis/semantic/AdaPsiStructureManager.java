package com.adacore.adaintellij.analysis.semantic;

import java.util.List;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import org.eclipse.lsp4j.DocumentSymbol;

import com.adacore.adaintellij.lsp.*;
import com.adacore.adaintellij.misc.cache.Marker;
import com.adacore.adaintellij.Utils;

import static com.adacore.adaintellij.analysis.semantic.AdaPsiElement.AdaElementType;

/**
 * Ada program structure manager.
 *
 * Due to the way Ada source code is parsed by the Ada-IntelliJ
 * plugin, additional semantic information can only be added to the
 * PSI tree elements after the tree has been constructed.
 *
 * This class provides an API that can be used to query additional
 * semantic information about a given PSI file from the ALS (Ada
 * Language Server) and patch the underlying PSI tree with the
 * collected information.
 * Any component that requires specific structure information for a
 * certain file must therefore first call the corresponding patcher
 * from this class before reading that information from that file.
 *
 * @see com.adacore.adaintellij.analysis.semantic.AdaParser
 * @see com.adacore.adaintellij.analysis.semantic.AdaPsiElement.AdaElementType
 */
public class AdaPsiStructureManager {

	/**
	 * Patch markers used to mark PSI files that already underwent
	 * their corresponding patches. This is useful to avoid
	 * reapplying the same patches to files that were not modified.
	 * File modifications result in new PSI files being generated
	 * without the markers added to previous PSI files, effectively
	 * invalidating all patches previously applied to a file.
	 * Each patch must have its own marker.
	 */
	private static final Marker SYMBOLS_PATCH_MARKER = Marker.getNewMarker();

	/**
	 * Applies all possible patches to the given PSI file.
	 *
	 * @param psiFile The PSI file to patch.
	 */
	public static void patchPsiFile(@NotNull AdaPsiFile psiFile) {
		patchPsiFileElementTypes(psiFile);
	}

	/**
	 * Makes a `textDocument/documentSymbol` request to the ALS and
	 * patches the given PSI file with Ada element types based on
	 * the returned symbol information for that file.
	 *
	 * @param psiFile The PSI file to patch.
	 */
	public static void patchPsiFileElementTypes(@NotNull AdaPsiFile psiFile) {

		patchPsiFileIfMarked(
			psiFile,
			SYMBOLS_PATCH_MARKER,
			() -> {

				Document    document    = Utils.getPsiFileDocument(psiFile);
				VirtualFile virtualFile = Utils.getPsiFileVirtualFile(psiFile);

				if (document == null || virtualFile == null) { return; }

				String documentUri = virtualFile.getUrl();

				// Make the request and wait for the result

				AdaLSPServer lspServer = AdaLSPDriver.getServer(psiFile.getProject());

				if (lspServer == null) { return; }

				List<DocumentSymbol> symbols = lspServer.documentSymbol(documentUri);

				// For each symbol in the result...

				symbols.forEach(symbol -> {

					// Find the PSI element at the given position

					PsiElement element = psiFile.findElementAt(
						LSPUtils.positionToOffset(document, symbol.getSelectionRange().getStart()));

					if (element == null) { return; }

					// Get the corresponding `AdaPsiElement`

					AdaPsiElement adaPsiElement = AdaPsiElement.getFrom(element);

					if (adaPsiElement == null) { return; }

					// Map the symbol kind to the corresponding Ada
					// element type and set the type of the element

					AdaElementType elementType =
						LSPUtils.symbolKindToAdaElementType(symbol);

					if (elementType == null) { return; }

					adaPsiElement.setAdaElementType(elementType);

				});

			}
		);

	}

	/**
	 * Applies the given patch only if the given PSI file is marked
	 * with the given marker, then marks the file with that marker.
	 *
	 * @param psiFile The PSI file to check for the marker.
	 * @param marker The marker to check in the given PSI file.
	 * @param patch The patch to apply if the file is marked.
	 */
	private static void patchPsiFileIfMarked(
		@NotNull AdaPsiFile psiFile,
		@NotNull Marker     marker,
		@NotNull Runnable   patch
	) {

		// If the file is marked with the marker, then abort

		if (psiFile.isMarked(marker)) { return; }

		// Apply the patch

		patch.run();

		// Mark the file with the marker

		psiFile.mark(marker);

	}

}
