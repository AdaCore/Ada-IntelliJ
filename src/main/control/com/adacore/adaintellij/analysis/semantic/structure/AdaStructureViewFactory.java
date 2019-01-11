package com.adacore.adaintellij.analysis.semantic.structure;

import com.intellij.ide.structureView.*;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.*;

/**
 * Structure view factory for Ada files.
 */
public class AdaStructureViewFactory implements PsiStructureViewFactory {
	
	/**
	 * Returns a new structure view builder for the given file.
	 *
	 * @param psiFile The file for which to return a builder.
	 * @return A new structure view builder for the given file.
	 */
	@NotNull
	@Override
	public StructureViewBuilder getStructureViewBuilder(@NotNull PsiFile psiFile) {
		return new AdaTreeBasedStructureViewBuilder(psiFile);
	}
	
}
