package com.adacore.adaintellij.lexanalysis.regex;

/**
 * Regex matching any sequence of characters not matched
 * by a certain regex.
 */
public class NotRegex implements OORegex {
	
	/**
	 * The negated subregex.
	 */
	private OORegex regex;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new not regex given a subregex.
	 *
	 * @param regex The negated subregex.
	 */
	public NotRegex(OORegex regex) { this(regex, 0); }
	
	/**
	 * Constructs a new not regex given a subregex and a priority.
	 *
	 * @param regex The negated subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public NotRegex(OORegex regex, int priority) {
		this.regex    = regex;
		this.PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return !regex.nullable(); }
	
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
		
		return advancedRegex == null ? new UnitRegex("", PRIORITY) : null;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() { return new NotRegex(regex.clone(), PRIORITY); }
	
}
