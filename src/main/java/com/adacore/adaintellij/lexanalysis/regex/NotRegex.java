package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching a single character not matched by another
 * single-character subregex.
 * To avoid overly complex inheritance hierarchy, the NotRegex
 * constructor simply uses the charactersMatched() method to
 * check that the regex it receives matches a single character,
 * and throws an exception otherwise.
 */
public final class NotRegex implements OORegex {
	
	/**
	 * The negated subregex.
	 */
	public final OORegex REGEX;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new not regex given a subregex.
	 *
	 * @param regex The negated subregex.
	 */
	public NotRegex(@NotNull OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new not regex given a subregex and a priority.
	 *
	 * @param regex The negated subregex.
	 * @param priority The priority to assign to the constructed regex.
	 * @throws IllegalArgumentException If the received regex does not
	 *                                  match a single character.
	 */
	public NotRegex(@NotNull OORegex regex, int priority) {
		
		int regexCharacters = regex.charactersMatched();
		
		if (regexCharacters != 1) {
			throw new IllegalArgumentException(
				"Single-character regex expected in NotRegex constructor, but regex matching " +
					regexCharacters + " characters received.");
		}
		
		REGEX    = regex;
		PRIORITY = priority;
		
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
		
		OORegex advancedRegex = REGEX.advanced(character);
		
		return advancedRegex == null ?
			new UnitRegex("", PRIORITY) : null;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@NotNull
	@Override
	public OORegex clone() { return new NotRegex(REGEX.clone(), PRIORITY); }
	
}
