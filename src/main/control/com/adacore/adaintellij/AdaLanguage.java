package com.adacore.adaintellij;

import com.intellij.lang.Language;

/**
 * Internal representation of the Ada language.
 */
public final class AdaLanguage extends Language {

	/**
	 * Unique instance representing the Ada language.
	 */
	public static final AdaLanguage INSTANCE = new AdaLanguage();

	/**
	 * Constructs a new instance of the Ada language.
	 */
	private AdaLanguage() { super("Ada"); }

}
