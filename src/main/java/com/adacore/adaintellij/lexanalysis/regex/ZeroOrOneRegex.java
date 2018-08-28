package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching zero or one occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrOneRegex implements OORegex {
	
	/**
	 * The subregex to be matched zero or one times.
	 */
	public final OORegex REGEX;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new zero or one regex given a subregex.
	 *
	 * @param regex The subregex for the zero or one regex.
	 */
	public ZeroOrOneRegex(@NotNull OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new zero or one regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the zero or one regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ZeroOrOneRegex(@NotNull OORegex regex, int priority) {
		REGEX    = regex;
		PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return true; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return -1; }
	
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
	public OORegex advanced(char character) { return REGEX.advanced(character); }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@NotNull
	@Override
	public OORegex clone() { return new ZeroOrOneRegex(REGEX.clone(), PRIORITY); }
	
}
