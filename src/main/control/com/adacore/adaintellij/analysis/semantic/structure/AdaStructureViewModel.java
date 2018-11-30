package com.adacore.adaintellij.analysis.semantic.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;

public class AdaStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
	
	AdaStructureViewModel(@NotNull PsiFile psiFile) {
		super(psiFile, new AdaStructureViewElement(psiFile));
	}
	
	@NotNull
	@Override
	protected Class[] getSuitableClasses() {
		return new Class[] { AdaPsiElement.class };
	}
	
	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		return false;
	}
	
	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return false;
	}
	
}
