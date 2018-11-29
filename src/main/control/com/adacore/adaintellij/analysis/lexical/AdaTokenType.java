package com.adacore.adaintellij.analysis.lexical;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.AdaLanguage;

/**
 * Ada 2012 token type.
 */
public class AdaTokenType extends IElementType {
	
	/**
	 * Constructs a new Ada token type.
	 *
	 * @param debugName The name of the token type, used for debugging purposes.
	 *
	 * @see com.intellij.psi.tree.IElementType#IElementType(String, Language)
	 */
	AdaTokenType(@NotNull @NonNls String debugName) {
		super(debugName, AdaLanguage.INSTANCE);
	}
	
	/**
	 * Returns a string representation of this token type.
	 *
	 * @return A string representation of this token type.
	 */
	@Override
	public String toString() { return "AdaTokenType." + super.toString(); }
	
}
