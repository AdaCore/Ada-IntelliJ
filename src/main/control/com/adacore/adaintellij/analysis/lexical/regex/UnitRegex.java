package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.*;

/**
 * Unit regex matching a specific sequence of characters,
 * unlike other types of regexes that are defined recursively
 * using other regexes.
 */
public final class UnitRegex implements LexerRegex {
	
	/**
	 * The sequence of characters matched by this regex.
	 */
	final String SEQUENCE;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new unit regex given a sequence of characters.
	 *
	 * @param sequence The sequence of characters to match.
	 */
	public UnitRegex(@NotNull String sequence) { this(sequence, 0); }
	
	/**
	 * Constructs a new unit regex given a sequence of characters and
	 * a priority.
	 *
	 * @param sequence The sequence of characters to match.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public UnitRegex(@NotNull String sequence, int priority) {
		SEQUENCE = sequence;
		PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return SEQUENCE.length() == 0; }
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return SEQUENCE.length(); }
	
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
		return SEQUENCE.length() == 0 || SEQUENCE.charAt(0) != character ?
			null : new UnitRegex(SEQUENCE.substring(1), PRIORITY);
	}
	
}
