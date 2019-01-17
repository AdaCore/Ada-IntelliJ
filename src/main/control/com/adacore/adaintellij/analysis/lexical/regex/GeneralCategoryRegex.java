package com.adacore.adaintellij.analysis.lexical.regex;

import java.util.regex.Pattern;

import org.jetbrains.annotations.*;

/**
 * Regex matching a single character from a specific "General Category",
 * as defined by the Unicode standard.
 * Internally, a regex of this class stores a Java pattern compiled to
 * match only characters from a specific general category.
 */
public final class GeneralCategoryRegex extends LexerRegex {
	
	/**
	 * The internal pattern used to match a character
	 * based on its general category.
	 */
	private final Pattern PATTERN;
	
	/**
	 * Constructs a new general category regex given a general category
	 * identifier string (e.g. "Lu" for category "Letter, uppercase").
	 *
	 * @param generalCategory The general category identifier string.
	 */
	public GeneralCategoryRegex(@NotNull String generalCategory) {
		this(generalCategory, 0);
	}
	
	/**
	 * Constructs a new general category regex given a general category
	 * identifier string (e.g. "Lu" for category "Letter, uppercase")
	 * and a priority.
	 *
	 * @param generalCategory The general category identifier string.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public GeneralCategoryRegex(@NotNull String generalCategory, int priority) {
		super(priority);
		PATTERN  = Pattern.compile(String.format("\\p{%s}", generalCategory));
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return false; }
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return 1; }
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		return PATTERN.matcher(String.valueOf(character)).find() ?
			new UnitRegex("") : null;
	}
	
}
