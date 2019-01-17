package com.adacore.adaintellij.analysis.semantic.structure;

import com.intellij.ide.structureView.*;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.semantic.AdaPsiFile;
import com.adacore.adaintellij.analysis.semantic.AdaPsiStructureManager;

/**
 * Structure view builder for Ada files.
 */
public final class AdaTreeBasedStructureViewBuilder extends TreeBasedStructureViewBuilder {
	
	/**
	 * The PSI file represented by the structure views built
	 * by this builder.
	 */
	private AdaPsiFile file;
	
	/**
	 * Constructs a new AdaTreeBasedStructureViewBuilder given
	 * a PSI file.
	 *
	 * @param file The PSI file to attach to the constructed
	 *             structure view builder.
	 */
	AdaTreeBasedStructureViewBuilder(@NotNull AdaPsiFile file) { this.file = file; }
	
	/**
	 * @see com.intellij.ide.structureView.TreeBasedStructureViewBuilder#createStructureViewModel(Editor)
	 */
	@NotNull
	@Override
	public StructureViewModel createStructureViewModel(@Nullable Editor editor) {
		
		// Patch the file with Ada element types
		
		AdaPsiStructureManager.patchPsiFileElementTypes(file);
		
		// Return a new Ada structure view model
		
		return new AdaStructureViewModel(file);
		
	}
	
	/**
	 * @see com.intellij.ide.structureView.TreeBasedStructureViewBuilder#isRootNodeShown()
	 */
	@Override
	public boolean isRootNodeShown() { return false; }
	
}
