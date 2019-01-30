package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching a single character not matched by another
 * single-character subregex.
 * To avoid overly complex inheritance hierarchy, the NotRegex
 * constructor simply uses the `charactersMatched` method to
 * check that the regex it receives matches a single character,
 * and throws an exception otherwise.
 */
public final class NotRegex extends LexerRegex {

	/**
	 * The negated subregex.
	 */
	final LexerRegex REGEX;

	/**
	 * Constructs a new not regex given a subregex.
	 *
	 * @param regex The negated subregex.
	 */
	public NotRegex(@NotNull LexerRegex regex) { this(regex, 0); }

	/**
	 * Constructs a new not regex given a subregex and a priority.
	 *
	 * @param regex The negated subregex.
	 * @param priority The priority to assign to the constructed regex.
	 * @throws IllegalArgumentException If the received regex does not
	 *                                  match a single character.
	 */
	public NotRegex(@NotNull LexerRegex regex, int priority) {

		super(priority);

		int regexCharacters = regex.charactersMatched();

		if (regexCharacters != 1) {
			throw new IllegalArgumentException(
				"Single-character regex expected in NotRegex constructor, but regex matching " +
					regexCharacters + " characters received.");
		}

		REGEX = regex;

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

		LexerRegex advancedRegex = REGEX.advanced(character);

		return advancedRegex == null ?
			new UnitRegex("", PRIORITY) : null;

	}

}
