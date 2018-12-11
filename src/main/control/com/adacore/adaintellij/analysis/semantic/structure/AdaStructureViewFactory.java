package com.adacore.adaintellij.analysis.semantic.structure;

import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
		
		return new TreeBasedStructureViewBuilder() {
			
			/**
			 * @see com.intellij.ide.structureView.TreeBasedStructureViewBuilder#createStructureViewModel(Editor)
			 */
			@NotNull
			@Override
			public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
				return new AdaStructureViewModel(psiFile);
			}
			
		};
		
	}
	
}
