package com.adacore.adaintellij.lexanalysis.regex;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * Regex matching the concatenation of two subregexes.
 */
public final class ConcatenationRegex implements LexerRegex {
	
	/**
	 * The concatenation subregexes.
	 */
	final LexerRegex FIRST_REGEX;
	final LexerRegex SECOND_REGEX;
	
	/**
	 * The priority of this regex.
	 */
	private final int PRIORITY;
	
	/**
	 * Constructs a new concatenation regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public ConcatenationRegex(@NotNull LexerRegex firstRegex, @NotNull LexerRegex secondRegex) {
		this(firstRegex, secondRegex, 0);
	}
	
	/**
	 * Constructs a new concatenation regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public ConcatenationRegex(@NotNull LexerRegex firstRegex, @NotNull LexerRegex secondRegex, int priority) {
		FIRST_REGEX  = firstRegex;
		SECOND_REGEX = secondRegex;
		PRIORITY     = priority;
	}
	
	/**
	 * Returns a new hierarchy of concatenation regexes representing
	 * the concatenation of a list of regexes, in the same order as they
	 * appear in the list:
	 *
	 *            concat_regex
	 *              /      \
	 *          regex_1  concat_regex
	 *                     /      \
	 *                 regex_2     .
	 *                              .
	 *                               .
	 *                             concat_regex
	 *                               /      \
	 *                         regex_n-2  concat_regex
	 *                                      /      \
	 *                                regex_n-1  regex_n
	 *
	 * The priority of the returned root regex is set to that of the
	 * regex in the given list with the highest priority.
	 *
	 * @param regexes The list of regexes to concatenate.
	 * @return A hierarchy of concatenation regexes.
	 */
	public static LexerRegex fromList(@NotNull List<LexerRegex> regexes) {
		
		int regexesSize = regexes.size();
		
		if (regexesSize == 0) { return null; }
		
		ListIterator<LexerRegex> regexIterator = regexes.listIterator(regexesSize);
		
		LexerRegex regex = regexIterator.previous();
		
		int maxPriority = regex.getPriority();
		
		while (regexIterator.hasPrevious()) {
			
			LexerRegex nextRegex = regexIterator.previous();
			
			int nextRegexPriority = nextRegex.getPriority();
			
			if (nextRegexPriority > maxPriority) {
				maxPriority = nextRegexPriority;
			}
			
			regex = new ConcatenationRegex(nextRegex, regex, maxPriority);
			
		}
		
		return regex;
		
	}
	
	/**
	 * Returns a new hierarchy of concatenation regexes from an arbitrary
	 * number of regexes (Java varargs) using fromList(List<LexerRegex>).
	 *
	 * @param regexes The regexes to concatenate.
	 * @return A hierarchy of concatenation regexes.
	 */
	public static LexerRegex fromRegexes(@NotNull LexerRegex... regexes) {
		return fromList(Arrays.asList(regexes));
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() { return FIRST_REGEX.nullable() && SECOND_REGEX.nullable(); }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() {
		
		int firstRegexCharacters  = FIRST_REGEX.charactersMatched();
		int secondRegexCharacters = SECOND_REGEX.charactersMatched();
		
		return firstRegexCharacters == -1 || secondRegexCharacters == -1 ? -1 :
			firstRegexCharacters + secondRegexCharacters;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#getPriority()
	 */
	@Override
	public int getPriority() { return PRIORITY; }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		
		LexerRegex firstRegexAdvanced = FIRST_REGEX.advanced(character);
		
		if (FIRST_REGEX.nullable()) {
			
			LexerRegex secondRegexAdvanced = SECOND_REGEX.advanced(character);
			
			if (firstRegexAdvanced == null && secondRegexAdvanced == null) {
				
				return null;
				
			} else if (firstRegexAdvanced == null) {
			
				return secondRegexAdvanced;
			
			} else if (secondRegexAdvanced == null) {
			
				return new ConcatenationRegex(firstRegexAdvanced, SECOND_REGEX, PRIORITY);
			
			} else {
			
				return new UnionRegex(
					new ConcatenationRegex(firstRegexAdvanced, SECOND_REGEX, PRIORITY),
					secondRegexAdvanced,
					PRIORITY
				);
			
			}
			
		} else {
			
			return firstRegexAdvanced == null ? null :
				new ConcatenationRegex(firstRegexAdvanced, SECOND_REGEX, PRIORITY);
			
		}
		
	}
	
}
