package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching the intersection of two subregexes.
 * In other words, a regex of this class will advance by a character
 * only if the two intersection subregexes can both advance by that
 * character.
 */
public final class IntersectionRegex implements LexerRegex {
	
	/**
	 * The intersection subregexes.
	 */
	final LexerRegex FIRST_REGEX;
	final LexerRegex SECOND_REGEX;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new intersection regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public IntersectionRegex(@NotNull LexerRegex firstRegex, @NotNull LexerRegex secondRegex) {
		this(firstRegex, secondRegex, 0);
	}
	
	/**
	 * Constructs a new intersection regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public IntersectionRegex(@NotNull LexerRegex firstRegex, @NotNull LexerRegex secondRegex, int priority) {
		FIRST_REGEX  = firstRegex;
		SECOND_REGEX = secondRegex;
		PRIORITY     = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return FIRST_REGEX.nullable() && SECOND_REGEX.nullable(); }
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() {
		
		int firstRegexCharacters  = FIRST_REGEX.charactersMatched();
		int secondRegexCharacters = SECOND_REGEX.charactersMatched();
		
		return firstRegexCharacters != secondRegexCharacters ? -1 : firstRegexCharacters;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		
		LexerRegex firstRegexAdvanced  = FIRST_REGEX.advanced(character);
		LexerRegex secondRegexAdvanced = SECOND_REGEX.advanced(character);
		
		return firstRegexAdvanced != null && secondRegexAdvanced != null ?
			new IntersectionRegex(firstRegexAdvanced, secondRegexAdvanced, PRIORITY) : null;
		
	}
	
}
