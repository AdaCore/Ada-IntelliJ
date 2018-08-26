package com.adacore.adaintellij.lexanalysis.regex;

/**
 * Regex matching the intersection of two subregexes.
 * In other words, a regex of this class will advance by a character
 * only if the two intersection subregexes can both advance by that
 * character.
 */
public class IntersectionRegex implements OORegex {
	
	/**
	 * The intersection subregexes.
	 */
	private OORegex firstRegex;
	private OORegex secondRegex;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new intersection regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public IntersectionRegex(OORegex firstRegex, OORegex secondRegex) {
		this(firstRegex, secondRegex, 0);
	}
	
	/**
	 * Constructs a new intersection regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public IntersectionRegex(OORegex firstRegex, OORegex secondRegex, int priority) {
		this.firstRegex  = firstRegex;
		this.secondRegex = secondRegex;
		this.PRIORITY    = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() {
		return firstRegex.nullable() && secondRegex.nullable();
	}
	
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
		
		OORegex firstRegexAdvanced  = firstRegex.advanced(character);
		OORegex secondRegexAdvanced = secondRegex.advanced(character);
		
		return firstRegexAdvanced != null && secondRegexAdvanced != null ?
			new IntersectionRegex(firstRegexAdvanced, secondRegexAdvanced, PRIORITY) : null;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() {
		return new IntersectionRegex(firstRegex.clone(), secondRegex.clone(), PRIORITY);
	}
	
}
