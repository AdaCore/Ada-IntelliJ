package com.adacore.adaintellij.analysis.lexical.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.analysis.lexical.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the ZeroOrOneRegex class.
 */
final class ZeroOrOneRegexTest {
	
	// Constants
	
	private static final LexerRegex ZERO_OR_ONE_LOWERCASE_A_REGEX = new ZeroOrOneRegex(new UnitRegex("a"));
	
	private static final LexerRegex ZERO_OR_ONE_BRACKETS_REGEX =
		new ZeroOrOneRegex(
			new ConcatenationRegex(
				new UnionRegex(new UnitRegex("["), new UnitRegex("{")),
				new UnionRegex(new UnitRegex("]"), new UnitRegex("}"))
			)
		);
	
	private static final LexerRegex ZERO_OR_ONE_NULLABLE_REGEX =
		new ZeroOrOneRegex(
			new UnionRegex(
				new UnitRegex("abc"),
				new ZeroOrOneRegex(new UnitRegex(","))
			)
		);
	
	// Testing ZeroOrOneRegex#nullable() method
	
	@Test
	void zero_or_more_regex_is_always_nullable() {
		
		assertTrue(ZERO_OR_ONE_LOWERCASE_A_REGEX.nullable());
		assertTrue(ZERO_OR_ONE_BRACKETS_REGEX.nullable());
		assertTrue(ZERO_OR_ONE_NULLABLE_REGEX.nullable());
		
	}
	
	// Testing ZeroOrOneRegex#advanced(char) method
	
	@Test
	void zero_or_more_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "A");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "b");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "c");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "ab");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "aaaab");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "aa");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "aaa");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_LOWERCASE_A_REGEX, "aaaa");
		
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "a");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "B");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "1");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "]");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "}");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "{}{");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "{}[");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_BRACKETS_REGEX, "[][");
		
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "b");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "a,");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, ",a");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "abcab");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "abcab");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "abcabc");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "abcabcabc");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, "abc,");
		assertRegexDoesNotAdvance(ZERO_OR_ONE_NULLABLE_REGEX, ",,");
		
	}
	
	@Test
	void zero_or_more_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(ZERO_OR_ONE_BRACKETS_REGEX, "[");
		assertRegexDoesNotMatch(ZERO_OR_ONE_BRACKETS_REGEX, "{");
		
		assertRegexDoesNotMatch(ZERO_OR_ONE_NULLABLE_REGEX, "a");
		assertRegexDoesNotMatch(ZERO_OR_ONE_NULLABLE_REGEX, "ab");
	}
	
	@Test
	void zero_or_more_regex_matches_when_it_should() {
		
		assertRegexMatches(ZERO_OR_ONE_LOWERCASE_A_REGEX, "");
		assertRegexMatches(ZERO_OR_ONE_LOWERCASE_A_REGEX, "a");
		
		assertRegexMatches(ZERO_OR_ONE_BRACKETS_REGEX, "");
		assertRegexMatches(ZERO_OR_ONE_BRACKETS_REGEX, "[]");
		assertRegexMatches(ZERO_OR_ONE_BRACKETS_REGEX, "{}");
		assertRegexMatches(ZERO_OR_ONE_BRACKETS_REGEX, "{]");
		assertRegexMatches(ZERO_OR_ONE_BRACKETS_REGEX, "[}");
		
		assertRegexMatches(ZERO_OR_ONE_NULLABLE_REGEX, "");
		assertRegexMatches(ZERO_OR_ONE_NULLABLE_REGEX, "abc");
		assertRegexMatches(ZERO_OR_ONE_NULLABLE_REGEX, ",");
		
	}
	
}
