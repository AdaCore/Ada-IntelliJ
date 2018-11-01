package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching zero or more occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrMoreRegex implements LexerRegex {
	
	/**
	 * The subregex to be matched zero or more times.
	 */
	final LexerRegex REGEX;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new zero or more regex given a subregex.
	 *
	 * @param regex The subregex for the zero or more regex.
	 */
	public ZeroOrMoreRegex(@NotNull LexerRegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new zero or more regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the zero or more regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ZeroOrMoreRegex(@NotNull LexerRegex regex, int priority) {
		REGEX    = regex;
		PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return true; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return -1; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		
		LexerRegex advancedRegex = REGEX.advanced(character);
		
		return advancedRegex == null ? null :
			new ConcatenationRegex(advancedRegex, this, PRIORITY);
		
	}
	
}
