package com.adacore.adaintellij.lexanalysis.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the IntersectionRegex class.
 */
final class IntersectionRegexTest {
	
	// Constants
	
	// All lowercase letters except the letters "h", "e", "l" and "o"
	private static final LexerRegex INTERSECTION_REGEX_1 =
		new IntersectionRegex(
			UnionRegex.fromRange('a', 'z'),
			new NotRegex(
				UnionRegex.fromRegexes(
					new UnitRegex("h"),
					new UnitRegex("e"),
					new UnitRegex("l"),
					new UnitRegex("o")
				)
			)
		);
	
	// Equivalent to "h" but written as an intersection of
	// "hello" and "h"
	private static final LexerRegex INTERSECTION_REGEX_2 =
		new IntersectionRegex(
			new UnitRegex("hello"),
			new UnitRegex("helicopter")
		);
	
	// Parentheses only"
	private static final LexerRegex INTERSECTION_REGEX_3 =
		new IntersectionRegex(
			UnionRegex.fromRegexes(
				new UnitRegex("*"),
				new UnitRegex("-"),
				new UnitRegex("("),
				new UnitRegex(")"),
				new UnitRegex("$")
			),
			UnionRegex.fromRegexes(
				new UnitRegex("%"),
				new UnitRegex("^"),
				new UnitRegex("."),
				new UnitRegex("("),
				new UnitRegex(")")
			)
		);
	
	// Testing IntersectionRegex#nullable() method
	
	@Test
	void intersection_regex_is_nullable_iff_one_of_subregexes_is_nullable() {
		
		// Initialization
		
		LexerRegex nullableRegex    = new UnitRegex("");
		LexerRegex nonNullableRegex = new UnitRegex("abc");
		
		// Testing
		
		assertFalse(new IntersectionRegex(nonNullableRegex, nonNullableRegex).nullable());
		assertFalse(new IntersectionRegex(nonNullableRegex, nullableRegex).nullable());
		assertFalse(new IntersectionRegex(nullableRegex, nonNullableRegex).nullable());
		assertTrue(new IntersectionRegex(nullableRegex, nullableRegex).nullable());
		
	}
	
	// Testing IntersectionRegex#advanced(char) method
	
	@Test
	void intersection_regex_advances_when_it_should() {
		
		assertRegexAdvances(INTERSECTION_REGEX_2, "hel");
		
	}
	
	@Test
	void intersection_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, "h");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, "e");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, "l");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, "o");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, "A");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_1, ".");
		
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_2, "e");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_2, "el");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_2, "hello");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_2, "helicopter");
		
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, "*");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, "-");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, "$");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, "%");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, "^");
		assertRegexDoesNotAdvance(INTERSECTION_REGEX_3, ".");
		
	}
	
	@Test
	void intersection_regex_matches_when_it_should() {
		
		assertRegexMatches(INTERSECTION_REGEX_1, "a");
		assertRegexMatches(INTERSECTION_REGEX_1, "b");
		assertRegexMatches(INTERSECTION_REGEX_1, "z");
		
		assertRegexMatches(INTERSECTION_REGEX_3, "(");
		assertRegexMatches(INTERSECTION_REGEX_3, ")");
		
	}
	
}
