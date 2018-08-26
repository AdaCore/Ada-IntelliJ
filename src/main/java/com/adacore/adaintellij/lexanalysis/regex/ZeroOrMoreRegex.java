package com.adacore.adaintellij.lexanalysis.regex;

/**
 * Regex matching zero or more occurrences of a sequence
 * of characters matched by a subregex.
 */
public final class ZeroOrMoreRegex implements OORegex {
	
	/**
	 * The subregex to be matched zero or more times.
	 */
	private OORegex regex;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new zero or more regex given a subregex.
	 *
	 * @param regex The subregex for the zero or more regex.
	 */
	public ZeroOrMoreRegex(OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new zero or more regex given a subregex and
	 * a priority.
	 *
	 * @param regex The subregex for the zero or more regex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ZeroOrMoreRegex(OORegex regex, int priority) {
		this.regex    = regex;
		this.PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return true; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#advanced(char)
	 */
	@Override
	public OORegex advanced(char character) {
		
		OORegex advancedRegex = regex.advanced(character);
		
		return advancedRegex == null ? null :
			new ConcatRegex(advancedRegex, clone(), PRIORITY);
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() { return new ZeroOrMoreRegex(regex.clone(), PRIORITY); }
	
}
