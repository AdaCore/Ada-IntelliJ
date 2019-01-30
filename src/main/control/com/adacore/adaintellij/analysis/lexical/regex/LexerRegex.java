package com.adacore.adaintellij.analysis.lexical.regex;

import org.jetbrains.annotations.*;

/**
 * Object-Based Regular Expression specifically designed to be used
 * by a Lexical Analyser.
 * The main feature of this lexer-targeting regex class is the
 * `advanced` method which allows matching a sequence of characters
 * incrementally, filtering out non matching regexes along the way.
 * Any implementing class must be immutable by design. This allows
 * regexes to be reused when defining complex regexes.
 */
public abstract class LexerRegex {

	/**
	 * The priority of this regex. This may be used to choose a regex
	 * when multiple regexes match a string. Greater numbers represent
	 * higher priority.
	 */
	public final int PRIORITY;

	/**
	 * Constructs a new LexerRegex.
	 */
	public LexerRegex() { this(0); }

	/**
	 * Constructs a new LexerRegex given a priority.
	 *
	 * @param priority The priority to assign to the constructed regex.
	 */
	public LexerRegex(int priority) {
		PRIORITY = priority;
	}

	/**
	 * Returns whether or not this regex is nullable, i.e. whether
	 * or not it accepts the empty string.
	 * Not to be confused with the possibility of a variable being null,
	 * as can be indicated with JetBrains' @Nullable annotation.
	 *
	 * @return The "nullability" of this regex.
	 */
	public abstract boolean nullable();

	/**
	 * Returns the number of characters matched by this regex.
	 * This is useful to enforce constraints on the usage of certain
	 * regexes, such as requiring that a NotRegex is constructed using
	 * a single-character regex.
	 *
	 * @return The number of characters matched by this regex, or -1
	 *         if this regex matches an indefinite number of characters.
	 */
	public abstract int charactersMatched();

	/**
	 * Returns a new regex that is "advanced" by the given character.
	 * If a regex `r` matches a sequence of characters `(c_1, ... c_n)`,
	 * then the following expression is guaranteed to run without any
	 * of the calls to advanced returning a null regex:
	 *
	 *                       r.advanced(c_1)
	 *                        .advanced(c_2)
	 *                              .
	 *                              .
	 *                              .
	 *                        .advanced(c_n-1)
	 *                        .advanced(c_n)
	 *
	 * Inversely, if a regex `r` does not match any sequence of
	 * characters starting with a character `c`, then the following
	 * expression is guaranteed to return null:
	 *
	 *                        r.advanced(c)
	 *
	 * @param character The character by which to advance this regex.
	 * @return The advanced regex.
	 */
	@Nullable
	public abstract LexerRegex advanced(char character);

}
