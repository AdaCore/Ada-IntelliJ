package com.adacore.adaintellij.analysis.lexical.regex;

import java.util.*;

import org.jetbrains.annotations.*;

/**
 * Regex matching the union of two subregexes.
 */
public final class UnionRegex extends LexerRegex {
	
	/**
	 * The union subregexes.
	 */
	final LexerRegex FIRST_REGEX;
	final LexerRegex SECOND_REGEX;
	
	/**
	 * Constructs a new union regex given two subregexes.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 */
	public UnionRegex(
		@NotNull LexerRegex firstRegex,
		@NotNull LexerRegex secondRegex
	) { this(firstRegex, secondRegex, 0); }
	
	/**
	 * Constructs a new union regex given two subregexes and
	 * a priority.
	 *
	 * @param firstRegex The first subregex.
	 * @param secondRegex The second subregex.
	 * @param priority The priority to assign to the constructed regex.
	 */
	public UnionRegex(
		@NotNull LexerRegex firstRegex,
		@NotNull LexerRegex secondRegex,
		         int        priority
	) {
		super(priority);
		FIRST_REGEX  = firstRegex;
		SECOND_REGEX = secondRegex;
	}
	
	/**
	 * Returns a new hierarchy of union regexes representing the union
	 * of a list of regexes:
	 *
	 *             union_regex
	 *              /      \
	 *          regex_1   union_regex
	 *                     /      \
	 *                 regex_2     .
	 *                              .
	 *                               .
	 *                              union_regex
	 *                               /      \
	 *                         regex_n-2   union_regex
	 *                                      /      \
	 *                                regex_n-1  regex_n
	 *
	 * The priority of the returned root regex is set to that of the
	 * regex in the given list with the highest priority.
	 *
	 * @param regexes The list of regexes to unite.
	 * @return A hierarchy of union regexes.
	 */
	public static LexerRegex fromList(@NotNull final List<LexerRegex> regexes) {
		
		int regexesSize = regexes.size();
		
		if (regexesSize == 0) { return null; }
		
		ListIterator<LexerRegex> regexIterator = regexes.listIterator(regexesSize);
		
		LexerRegex regex = regexIterator.previous();
		
		int maxPriority = regex.PRIORITY;
		
		while (regexIterator.hasPrevious()) {
			
			LexerRegex nextRegex = regexIterator.previous();
			
			int nextRegexPriority = nextRegex.PRIORITY;
			
			if (nextRegexPriority > maxPriority) {
				maxPriority = nextRegexPriority;
			}
			
			regex = new UnionRegex(nextRegex, regex, maxPriority);
			
		}
		
		return regex;
		
	}
	
	/**
	 * Returns a new hierarchy of union regexes from an arbitrary
	 * number of regexes (Java varargs) using fromList(List<LexerRegex>).
	 *
	 * @param regexes The regexes to unite.
	 * @return A hierarchy of union regexes.
	 */
	public static LexerRegex fromRegexes(@NotNull LexerRegex... regexes) {
		return fromList(Arrays.asList(regexes));
	}
	
	/**
	 * Returns a new hierarchy of union regexes from a pair of character
	 * bounds, using fromRange(char, char, int) with the priority is set
	 * to 0.
	 *
	 * @param fromChar The lower bound character of the range.
	 * @param toChar The upper bound character of the range.
	 * @return A hierarchy of union regexes matching a range of characters.
	 */
	public static LexerRegex fromRange(char fromChar, char toChar) {
		return fromRange(fromChar, toChar, 0);
	}
	
	/**
	 * Returns a new hierarchy of union regexes, in the same format
	 * returned by fromList, representing the union of unit regexes each
	 * matching a character from a range specified by the given character
	 * bounds. The priority of all regexes in the hierarchy is set to the
	 * given priority.
	 *
	 * @param fromChar The lower bound character of the range.
	 * @param toChar The upper bound character of the range.
	 * @param priority The priority to assign to the regexes in the hierarchy.
	 * @return A hierarchy of union regexes matching a range of characters.
	 * @throws IllegalArgumentException If fromChar is greater than toChar.
	 */
	public static LexerRegex fromRange(char fromChar, char toChar, int priority) {
		
		if (fromChar > toChar) {
			throw new IllegalArgumentException("Invalid bounds: " +
				"fromChar must be smaller or equal to toChar");
		}
		
		char character = toChar;
		
		LexerRegex regex = new UnitRegex(Character.toString(character), priority);
		
		while (character != fromChar) {
			regex = new UnionRegex(
				new UnitRegex(Character.toString(--character), priority),
				regex,
				priority
			);
		}
		
		return regex;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#nullable()
	 */
	@Override
	public boolean nullable() {
		return FIRST_REGEX.nullable() || SECOND_REGEX.nullable();
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#charactersMatched()
	 */
	@Override
	public int charactersMatched() {
		
		int firstRegexCharacters  = FIRST_REGEX.charactersMatched();
		int secondRegexCharacters = SECOND_REGEX.charactersMatched();
		
		return firstRegexCharacters != secondRegexCharacters ?
			-1 : firstRegexCharacters;
		
	}
	
	/**
	 * @see com.adacore.adaintellij.analysis.lexical.regex.LexerRegex#advanced(char)
	 */
	@Nullable
	@Override
	public LexerRegex advanced(char character) {
		
		LexerRegex firstRegexAdvanced  = FIRST_REGEX.advanced(character);
		LexerRegex secondRegexAdvanced = SECOND_REGEX.advanced(character);
		
		if (firstRegexAdvanced == null && secondRegexAdvanced == null) {
			
			return null;
			
		} else if (firstRegexAdvanced == null) {
			
			return secondRegexAdvanced;
			
		} else if (secondRegexAdvanced == null) {
			
			return firstRegexAdvanced;
			
		} else {
			
			return new UnionRegex(firstRegexAdvanced, secondRegexAdvanced, PRIORITY);
			
		}
		
	}
	
}
