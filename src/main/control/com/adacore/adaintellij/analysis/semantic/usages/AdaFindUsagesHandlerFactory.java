package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Find-usages handler factory for Ada.
 */
public final class AdaFindUsagesHandlerFactory extends FindUsagesHandlerFactory {
	
	/**
	 * Returns whether or not the given element is a valid element to
	 * find usages for.
	 * 
	 * @param element The element to test.
	 * @return Whether or not the given element is a valid element to
	 *         find usages for.
	 */
	@Override
	public boolean canFindUsages(@NotNull PsiElement element) {
		return new AdaFindUsagesProvider().canFindUsagesFor(element);
	}
	
	/**
	 * Returns a find-usages handler for Ada.
	 * 
	 * @param element The element with which to construct a find-usages
	 *                handler.
	 * @param forHighlightUsages Whether or not the returned handler is
	 *                           for highlighting usages.
	 * @return a find-usages handler for Ada.
	 */
	@NotNull
	@Override
	public FindUsagesHandler createFindUsagesHandler(
		@NotNull PsiElement element,
		         boolean    forHighlightUsages
	) {
		return new AdaFindUsagesHandler(element);
	}
	
}
