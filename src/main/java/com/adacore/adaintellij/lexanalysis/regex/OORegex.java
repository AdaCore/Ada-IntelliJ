package com.adacore.adaintellij.lexanalysis.regex;

/**
 * Object-Based Regular Expression specifically designed
 * to be used by a Lexical Analyser.
 * The main feature of this lexer-targeting regex interface
 * is the advanced method which allows matching a sequence of
 * characters incrementally, filtering out non matching regexes
 * along the way.
 * Any implementing class must be immutable by design. This
 * allows regexes to be reused when defining complex regexes.
 *
 * TODO: Consider reducing cloning in clone implementations of
 *       implementing classes without breaking immutability
 */
public interface OORegex {
	
	/**
	 * Returns whether or not this regex is nullable, i.e. whether
	 * or not it accepts the empty string.
	 * Not to be confused with the possibility of a variable being null,
	 * as can be indicated with JetBrains' @NotNull annotation.
	 *
	 * @return The "nullability" of this regex.
	 */
	boolean nullable();
	
	/**
	 * The priority of this regex. This may be used to choose a regex
	 * when multiple regexes match a string. Greater numbers represent
	 * higher priority.
	 *
	 * @return The priority of this regex.
	 */
	int getPriority();
	
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
	 * Inversely, if a regex `r` does not match a character any sequence
	 * of characters starting with a character `c`, then the following
	 * expression is guaranteed to return a null:
	 *
	 *                        r.advanced(c)
	 *
	 * @param character The character by which to advance this regex.
	 * @return The advanced regex.
	 */
	OORegex advanced(char character);
	
	/**
	 * Returns a clone of this regex.
	 *
	 * @return The clone of this regex.
	 */
	OORegex clone();
	
}
