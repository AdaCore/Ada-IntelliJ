package com.adacore.adaintellij.lexanalysis.regex;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * Regex matching the concatenation of two subregexes.
 */
public final class ConcatRegex implements OORegex {
	
	/**
	 * The concatenation subregexes.
	 */
	public final OORegex FIRST_REGEX;
	public final OORegex SECOND_REGEX;
	
	/**
	 * The priority of this regex.
	 */
	public final int PRIORITY;
	
	/**
	 * Constructs a new concatenation regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public ConcatRegex(@NotNull OORegex firstRegex, @NotNull OORegex secondRegex) {
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
	public ConcatRegex(@NotNull OORegex firstRegex, @NotNull OORegex secondRegex, int priority) {
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
	public static OORegex fromList(@NotNull List<OORegex> regexes) {
		
		int regexesSize = regexes.size();
		
		if (regexesSize == 0) { return null; }
		
		ListIterator<OORegex> regexIterator = regexes.listIterator(regexesSize);
		
		OORegex regex = regexIterator.previous();
		
		int maxPriority = regex.getPriority();
		
		while (regexIterator.hasPrevious()) {
			
			OORegex nextRegex = regexIterator.previous();
			
			int nextRegexPriority = nextRegex.getPriority();
			
			if (nextRegexPriority > maxPriority) {
				maxPriority = nextRegexPriority;
			}
			
			regex = new ConcatRegex(nextRegex, regex, maxPriority);
			
		}
		
		return regex;
		
	}
	
	/**
	 * Returns a new hierarchy of concatenation regexes from an arbitrary
	 * number of regexes (Java varargs) using fromList(List<OORegex>).
	 *
	 * @param regexes The regexes to concatenate.
	 * @return A hierarchy of concatenation regexes.
	 */
	public static OORegex fromRegexes(@NotNull OORegex... regexes) {
		return fromList(Arrays.asList(regexes));
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#nullable()
	 */
	@Override
	public boolean nullable() { return FIRST_REGEX.nullable() && SECOND_REGEX.nullable(); }
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() {
		
		int firstRegexCharacters  = FIRST_REGEX.charactersMatched();
		int secondRegexCharacters = SECOND_REGEX.charactersMatched();
		
		return firstRegexCharacters == -1 || secondRegexCharacters == -1 ? -1 :
			firstRegexCharacters + secondRegexCharacters;
		
	}
	
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
		
		OORegex firstRegexAdvanced  = FIRST_REGEX.advanced(character);
		
		if (FIRST_REGEX.nullable()) {
			
			OORegex secondRegexAdvanced = SECOND_REGEX.advanced(character);
			
			if (firstRegexAdvanced == null && secondRegexAdvanced == null) {
				
				return null;
				
			} else if (firstRegexAdvanced == null) {
			
				return secondRegexAdvanced;
			
			} else if (secondRegexAdvanced == null) {
			
				return new ConcatRegex(firstRegexAdvanced, SECOND_REGEX.clone(), PRIORITY);
			
			} else {
			
				return new UnionRegex(
					new ConcatRegex(firstRegexAdvanced, SECOND_REGEX.clone(), PRIORITY),
					secondRegexAdvanced,
					PRIORITY
				);
			
			}
			
		} else {
			
			if (firstRegexAdvanced == null) {
				
				return null;
				
			} else {
				
				return new ConcatRegex(firstRegexAdvanced, SECOND_REGEX.clone(), PRIORITY);
				
			}
		}
		
	}
	
	/**
	 * @see com.adacore.adaintellij.lexanalysis.regex.OORegex#clone()
	 */
	@NotNull
	@Override
	public OORegex clone() {
		return new ConcatRegex(FIRST_REGEX.clone(), SECOND_REGEX.clone(), PRIORITY);
	}
	
}
