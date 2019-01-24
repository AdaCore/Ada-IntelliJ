package com.adacore.adaintellij.analysis.semantic.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;

/**
 * Structure view model of an Ada file.
 */
public final class AdaStructureViewModel extends StructureViewModelBase
	implements StructureViewModel.ElementInfoProvider
{
	
	/**
	 * Constructs an AdaStructureViewModel for the given PSI file.
	 *
	 * @param psiFile The PSI file represented by the constructed
	 *                model.
	 */
	AdaStructureViewModel(@NotNull PsiFile psiFile) {
		super(psiFile, new AdaStructureViewElement(psiFile));
	}
	
	/**
	 * @see com.intellij.ide.structureView.TextEditorBasedStructureViewModel#getSuitableClasses()
	 */
	@NotNull
	@Override
	protected Class[] getSuitableClasses() {
		return new Class[] { AdaPsiElement.class };
	}
	
	/**
	 * Returns whether or not the given element is always a container
	 * that can be expanded to reveal a subtree of elements.
	 *
	 * @param element The element to test.
	 * @return Whether or not the given element is always expandable.
	 */
	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		return false;
	}
	
	/**
	 * Returns whether or not the given element is always a leaf element.
	 *
	 * @param element The element to test.
	 * @return Whether or not the given element is always a leaf element.
	 */
	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return false;
	}
	
}
