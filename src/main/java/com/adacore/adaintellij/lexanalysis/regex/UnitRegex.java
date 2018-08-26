package com.adacore.adaintellij.lexanalysis.regex;

/**
 * Unit regex matching a specific sequence of characters,
 * unlike other types of regexes that are defined recursively
 * using other regexes.
 */
public final class UnitRegex implements OORegex {
	
	/**
	 * The sequence of characters matched by this regex.
	 */
	private String sequence;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new unit regex given a sequence of characters.
	 *
	 * @param sequence The sequence of characters to match.
	 */
	public UnitRegex(String sequence) { this(sequence, 0); }
	
	/**
	 * Constructs a new unit regex given a sequence of characters and
	 * a priority.
	 *
	 * @param sequence The sequence of characters to match.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public UnitRegex(String sequence, int priority) {
		this.sequence = sequence;
		this.PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return sequence.length() == 0; }
	
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
		return nullable() || sequence.charAt(0) != character ?
			null : new UnitRegex(sequence.substring(1), PRIORITY);
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@Override
	public OORegex clone() { return new UnitRegex(sequence, PRIORITY); }
	
}
