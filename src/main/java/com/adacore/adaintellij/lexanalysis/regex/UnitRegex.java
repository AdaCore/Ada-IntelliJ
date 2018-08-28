package com.adacore.adaintellij.lexanalysis.regex;

import org.jetbrains.annotations.*;

/**
 * Unit regex matching a specific sequence of characters,
 * unlike other types of regexes that are defined recursively
 * using other regexes.
 */
public final class UnitRegex implements OORegex {
	
	/**
	 * The sequence of characters matched by this regex.
	 */
	public final String SEQUENCE;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new unit regex given a sequence of characters.
	 *
	 * @param sequence The sequence of characters to match.
	 */
	public UnitRegex(@NotNull String sequence) { this(sequence, 0); }
	
	/**
	 * Constructs a new unit regex given a sequence of characters and
	 * a priority.
	 *
	 * @param sequence The sequence of characters to match.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public UnitRegex(@NotNull String sequence, int priority) {
		SEQUENCE = sequence;
		PRIORITY = priority;
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return SEQUENCE.length() == 0; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() { return SEQUENCE.length(); }
	
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
		return SEQUENCE.length() == 0 || SEQUENCE.charAt(0) != character ?
			null : new UnitRegex(SEQUENCE.substring(1), PRIORITY);
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@NotNull
	@Override
	public OORegex clone() { return new UnitRegex(SEQUENCE, PRIORITY); }
	
}
