package com.adacore.adaintellij.analysis.semantic.structure;

import javax.swing.*;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdaStructureItemPresentation implements ItemPresentation {
	
	private NavigatablePsiElement element;
	
	AdaStructureItemPresentation(@NotNull NavigatablePsiElement element) {
		this.element = element;
	}
	
	@Nullable
	@Override
	public String getPresentableText() {
		return element instanceof PsiFile ?
			((PsiFile)element).getName() : element.getText();
	}
	
	@Nullable
	@Override
	public String getLocationString() {
		return element instanceof PsiFile ?
			"" : element.getContainingFile().getName();
	}
	
	@Nullable
	@Override
	public Icon getIcon(boolean unused) { return null; }
	
}
