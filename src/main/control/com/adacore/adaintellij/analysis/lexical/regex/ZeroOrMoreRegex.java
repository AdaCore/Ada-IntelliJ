package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching zero or more occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrMoreRegex extends LexerRegex {

	/**
	 * The subregex to be matched zero or more times.
	 */
	final LexerRegex REGEX;

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
		super(priority);
		REGEX = regex;
	}

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return true; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return -1; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {

		LexerRegex advancedRegex = REGEX.advanced(character);

		return advancedRegex == null ? null :
			new ConcatenationRegex(advancedRegex, this, PRIORITY);

	}

}
