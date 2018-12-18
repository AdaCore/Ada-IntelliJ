package com.adacore.adaintellij;

import com.intellij.lang.Language;

/**
 * Internal representation of the GPR file language.
 */
public final class GPRLanguage extends Language {
	
	/**
	 * Unique instance representing the GPR file language.
	 */
	public static final GPRLanguage INSTANCE = new GPRLanguage();
	
	/**
	 * Constructs a new instance of the GPR file language.
	 */
	private GPRLanguage() { super("GPR File"); }
	
}
