package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;
import com.adacore.adaintellij.analysis.semantic.AdaPsiReference;

/**
 * Read/write access detector for Ada.
 */
public final class AdaReadWriteAccessDetector extends ReadWriteAccessDetector {

	/**
	 * Returns whether or not the given element can be classified
	 * as a read and/or write access.
	 *
	 * @param element The element to test.
	 * @return Whether or not the given element can be classified
	 *         as a read and/or write access.
	 */
	@Override
	public boolean isReadWriteAccessible(@NotNull PsiElement element) {
		return AdaPsiElement.getFrom(element) instanceof AdaPsiReference;
	}

	/**
	 * Returns whether or not the given element is a write access
	 * of a declaration.
	 *
	 * @param element The element to test.
	 * @return Whether or not the given element is a write access
	 *         of a declaration.
	 */
	@Override
	public boolean isDeclarationWriteAccess(@NotNull PsiElement element) {
		return element instanceof AdaPsiReference &&
			((AdaPsiReference)element).isDeclaration();
	}

	/**
	 * Returns the access type of the given reference.
	 *
	 * @param referencedElement The element referenced by the
	 *                          given reference.
	 * @param reference The reference for which to get the
	 *                  access type.
	 * @return The access type of the given reference.
	 */
	@NotNull
	@Override
	public Access getReferenceAccess(
		@NotNull PsiElement   referencedElement,
		@NotNull PsiReference reference
	) {
		return getExpressionAccess(reference.getElement());
	}

	/**
	 * Returns the access type of the given expression.
	 *
	 * @param expression The expression for which to get the
	 *                   access type.
	 * @return The access type of the given expression.
	 */
	@NotNull
	@Override
	public Access getExpressionAccess(@NotNull PsiElement expression) {
		return isDeclarationWriteAccess(expression) ? Access.Write : Access.Read;
	}

}
