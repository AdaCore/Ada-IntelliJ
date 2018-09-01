package com.adacore.adaintellij.lexanalysis.regex;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.OORegexTestUtils.*;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the ZeroOrMoreRegex class.
 */
final class ZeroOrMoreRegexTest {
	
	// Constants
	
	private static final OORegex ZERO_OR_MORE_LOWERCASE_A_REGEX = new ZeroOrMoreRegex(new UnitRegex("a"));
	
	private static final OORegex ZERO_OR_MORE_BRACKETS_REGEX =
		new ZeroOrMoreRegex(
			new ConcatRegex(
				new UnionRegex(new UnitRegex("["), new UnitRegex("{")),
				new UnionRegex(new UnitRegex("]"), new UnitRegex("}"))
			)
		);
	
	private static final OORegex ZERO_OR_MORE_NULLABLE_REGEX =
		new ZeroOrMoreRegex(
			new UnionRegex(
				new UnitRegex("abc"),
				new ZeroOrOneRegex(new UnitRegex(","))
			)
		);
	
	// Testing ZeroOrMoreRegex#nullable() method
	
	@Test
	void zero_or_more_regex_is_always_nullable() {
		
		assertTrue(ZERO_OR_MORE_LOWERCASE_A_REGEX.nullable());
		assertTrue(ZERO_OR_MORE_BRACKETS_REGEX.nullable());
		assertTrue(ZERO_OR_MORE_NULLABLE_REGEX.nullable());
		
	}
	
	// Testing ZeroOrMoreRegex#advanced(char) method
	
	@Test
	void zero_or_more_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(ZERO_OR_MORE_LOWERCASE_A_REGEX, "A");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_LOWERCASE_A_REGEX, "b");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_LOWERCASE_A_REGEX, "c");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_LOWERCASE_A_REGEX, "ab");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_LOWERCASE_A_REGEX, "aaaab");
		
		assertRegexDoesNotAdvance(ZERO_OR_MORE_BRACKETS_REGEX, "a");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_BRACKETS_REGEX, "B");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_BRACKETS_REGEX, "1");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_BRACKETS_REGEX, "]");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_BRACKETS_REGEX, "}");
		
		assertRegexDoesNotAdvance(ZERO_OR_MORE_NULLABLE_REGEX, "b");
		assertRegexDoesNotAdvance(ZERO_OR_MORE_NULLABLE_REGEX, "a,");
		
	}
	
	@Test
	void zero_or_more_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(ZERO_OR_MORE_BRACKETS_REGEX, "[");
		assertRegexDoesNotMatch(ZERO_OR_MORE_BRACKETS_REGEX, "{");
		assertRegexDoesNotMatch(ZERO_OR_MORE_BRACKETS_REGEX, "{}{");
		assertRegexDoesNotMatch(ZERO_OR_MORE_BRACKETS_REGEX, "{}[");
		
		assertRegexDoesNotMatch(ZERO_OR_MORE_NULLABLE_REGEX, "ab");
		assertRegexDoesNotMatch(ZERO_OR_MORE_NULLABLE_REGEX, ",a");
		assertRegexDoesNotMatch(ZERO_OR_MORE_NULLABLE_REGEX, "abcab");
		
	}
	
	@Test
	void zero_or_more_regex_matches_when_it_should() {
		
		assertRegexMatches(ZERO_OR_MORE_LOWERCASE_A_REGEX, "");
		assertRegexMatches(ZERO_OR_MORE_LOWERCASE_A_REGEX, "a");
		assertRegexMatches(ZERO_OR_MORE_LOWERCASE_A_REGEX, "aa");
		assertRegexMatches(ZERO_OR_MORE_LOWERCASE_A_REGEX, "aaa");
		assertRegexMatches(ZERO_OR_MORE_LOWERCASE_A_REGEX, "aaaa");
		
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "");
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "[]");
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "{}");
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "{]");
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "[}");
		assertRegexMatches(ZERO_OR_MORE_BRACKETS_REGEX, "[][]{}[}[]");
		
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, "");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, "abc");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, ",");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, "abcabc");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, "abcabcabc");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, ",,");
		assertRegexMatches(ZERO_OR_MORE_NULLABLE_REGEX, ",abc,");
		
	}
	
}
