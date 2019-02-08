package com.adacore.adaintellij.analysis.syntactic.structure;

import com.intellij.ide.structureView.*;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.syntactic.AdaPsiFile;

/**
 * Structure view factory for Ada files.
 */
public final class AdaStructureViewFactory implements PsiStructureViewFactory {

	/**
	 * Returns a new structure view builder for the given file.
	 *
	 * @param psiFile The file for which to return a builder.
	 * @return A new structure view builder for the given file.
	 */
	@NotNull
	@Override
	public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {

		// Check that the PSI file is an Ada PSI file

		assert psiFile instanceof AdaPsiFile :
			"Attempt to get an Ada structure view builder for a non-Ada PSI file";

		// Return a new Ada structure view builder for
		// the given Ada PSI file

		return new AdaTreeBasedStructureViewBuilder((AdaPsiFile)psiFile);

	}

}
