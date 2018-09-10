package com.adacore.adaintellij.lexanalysis.regex;

import java.util.regex.Pattern;

import org.jetbrains.annotations.*;

/**
 * Regex matching a single character from a specific "General Category",
 * as defined by the Unicode standard.
 * Internally, a regex of this class stores a Java pattern compiled to
 * match only characters from a specific general category.
 */
public final class GeneralCategoryRegex implements OORegex {
	
	/**
	 * The internal pattern used to match a character
	 * based on its general category.
	 */
	public final Pattern PATTERN;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new general category regex given a general category
	 * identifier string (e.g. "Lu" for category "Letter, uppercase").
	 *
	 * @param generalCategory The general category identifier string.
	 */
	public GeneralCategoryRegex(@NotNull String generalCategory) { this(generalCategory, 0); }
	
	/**
	 * Constructs a new general category regex given a general category
	 * identifier string (e.g. "Lu" for category "Letter, uppercase")
	 * and a priority.
	 *
	 * @param generalCategory The general category identifier string.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public GeneralCategoryRegex(@NotNull String generalCategory, int priority) {
		PATTERN          = Pattern.compile(String.format("\\p{%s}", generalCategory));
		PRIORITY         = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return false; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return 1; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#advanced(char)
	 */
	@Nullable
	@Override
	public OORegex advanced(char character) {
		
		return PATTERN.matcher(String.valueOf(character)).find() ?
			new UnitRegex("") : null;
		
	}
	
}
