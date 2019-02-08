package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.lang.cacheBuilder.*;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.lexical.*;
import com.adacore.adaintellij.analysis.syntactic.*;

/**
 * Find-usages provider for Ada.
 */
public final class AdaFindUsagesProvider implements FindUsagesProvider {

	/**
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#getWordsScanner()
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
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#canFindUsagesFor(PsiElement)
	 */
	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return AdaPsiElement.getFrom(psiElement) instanceof AdaPsiReference;
	}

	/**
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#getHelpId(PsiElement)
	 */
	@Nullable
	@Override
	public String getHelpId(@NotNull PsiElement psiElement) { return null; }

	/**
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#getType(PsiElement)
	 */
	@NotNull
	@Override
	public String getType(@NotNull PsiElement element) {
		// TODO: Return element type (e.g. package/procedure/variable...)
		return "";
	}

	/**
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#getDescriptiveName(PsiElement)
	 */
	@NotNull
	@Override
	public String getDescriptiveName(@NotNull PsiElement element) {
		return getNodeText(element, true);
	}

	/**
	 * @see com.intellij.lang.findUsages.FindUsagesProvider#getNodeText(PsiElement, boolean)
	 */
	@NotNull
	@Override
	public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		return element.getText();
	}

}
