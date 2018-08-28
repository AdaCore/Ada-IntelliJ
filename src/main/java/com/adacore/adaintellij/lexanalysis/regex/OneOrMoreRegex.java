package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Regex matching one or more occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class OneOrMoreRegex implements OORegex {
	
	/**
	 * The subregex to be matched one or more times.
	 */
	public final OORegex REGEX;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new one or more regex given a subregex.
	 *
	 * @param regex The subregex for the one or more regex.
	 */
	public OneOrMoreRegex(@NotNull OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new one or more regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the one or more regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public OneOrMoreRegex(@NotNull OORegex regex, int priority) {
		REGEX    = regex;
		PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return REGEX.nullable(); }
	
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
			new ConcatRegex(
				advancedRegex,
				new ZeroOrMoreRegex(REGEX.clone(), PRIORITY),
				PRIORITY
			);
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@NotNull
	@Override
	public OORegex clone() { return new OneOrMoreRegex(REGEX.clone(), PRIORITY); }
	
}
