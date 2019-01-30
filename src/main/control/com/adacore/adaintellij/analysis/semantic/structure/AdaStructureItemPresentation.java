package com.adacore.adaintellij.analysis.semantic.structure;

import javax.swing.*;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;

/**
 * The presentation of an item in the structure view of an Ada file.
 */
public final class AdaStructureItemPresentation implements ItemPresentation {

	/**
	 * The PSI element that this item presentation represents.
	 */
	private NavigatablePsiElement element;

	/**
	 * Constructs a new AdaStructureItemPresentation given a
	 * PSI element.
	 *
	 * @param element The PSI element represented by the
	 *                constructed item presentation.
	 */
	AdaStructureItemPresentation(@NotNull NavigatablePsiElement element) {
		this.element = element;
	}

	/**
	 * @see com.intellij.navigation.ItemPresentation#getPresentableText()
	 */
	@Nullable
	@Override
	public String getPresentableText() {
		return element instanceof PsiFile ?
			((PsiFile)element).getName() : element.getText();
	}

	/**
	 * @see com.intellij.navigation.ItemPresentation#getLocationString()
	 */
	@Nullable
	@Override
	public String getLocationString() { return null; }

	/**
	 * @see com.intellij.navigation.ItemPresentation#getIcon(boolean)
	 */
	@Nullable
	@Override
	public Icon getIcon(boolean unused) {

		AdaPsiElement adaPsiElement = AdaPsiElement.getFrom(element);

		return adaPsiElement == null ? null : adaPsiElement.getIcon();

	}

}
