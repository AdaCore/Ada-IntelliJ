package com.adacore.adaintellij.analysis.lexical;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.GPRLanguage;

/**
 * GPR file token type.
 */
public class GPRFileTokenType extends IElementType {
	
	/**
	 * Constructs a new GPR file token type.
	 *
	 * @param debugName The name of the token type, used for debugging purposes.
	 *
	 * @see com.intellij.psi.tree.IElementType#IElementType(String, Language)
	 */
	GPRFileTokenType(@NotNull @NonNls String debugName) {
		super(debugName, GPRLanguage.INSTANCE);
	}
	
	/**
	 * Returns a string representation of this token type.
	 *
	 * @return A string representation of this token type.
	 */
	@Override
	public String toString() { return "GPRFileTokenType." + super.toString(); }
	
}
