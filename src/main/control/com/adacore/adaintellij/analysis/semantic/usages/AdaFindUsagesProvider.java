package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.analysis.lexical.AdaLexer;
import com.adacore.adaintellij.analysis.lexical.AdaTokenTypes;
import com.adacore.adaintellij.analysis.semantic.AdaPsiReference;

/**
 * Find-usages provider for Ada.
 */
public final class AdaFindUsagesProvider implements FindUsagesProvider {
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#getWordsScanner()
	 */
	@NotNull
	@Override
	public WordsScanner getWordsScanner() {
		return new DefaultWordsScanner(
			new AdaLexer(),
			AdaTokenTypes.IDENTIFIER_TOKEN_SET,
			AdaTokenTypes.COMMENT_TOKEN_SET,
			AdaTokenTypes.LITERAL_TOKEN_SET
		);
	}
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#canFindUsagesFor(PsiElement)
	 */
	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof AdaPsiReference ||
			psiElement.getParent() instanceof AdaPsiReference;
	}
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#getHelpId(PsiElement)
	 */
	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) { return null; }
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#getType(PsiElement)
	 */
	@NotNull
	@Override
	public String getType(@NotNull PsiElement element) {
		// TODO: Return element type (e.g. package/procedure/variable...)
		return "";
	}
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#getDescriptiveName(PsiElement)
	 */
	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement element) {
		return getNodeText(element, true);
	}
	
	/**
	 * com.intellij.lang.findUsages.FindUsagesProvider#getNodeText(PsiElement, boolean)
	 */
	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		return element.getText();
	}
	
}
