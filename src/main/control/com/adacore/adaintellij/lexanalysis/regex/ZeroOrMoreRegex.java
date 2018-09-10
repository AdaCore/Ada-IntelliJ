package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching zero or more occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrMoreRegex implements OORegex {
	
	/**
	 * The subregex to be matched zero or more times.
	 */
	public final OORegex REGEX;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new zero or more regex given a subregex.
	 *
	 * @param regex The subregex for the zero or more regex.
	 */
	public ZeroOrMoreRegex(@NotNull OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new zero or more regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the zero or more regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ZeroOrMoreRegex(@NotNull OORegex regex, int priority) {
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
	public OORegex advanced(char character) {
		
		OORegex advancedRegex = REGEX.advanced(character);
		
		return advancedRegex == null ? null :
			new ConcatRegex(advancedRegex, this, PRIORITY);
		
	}
	
}
