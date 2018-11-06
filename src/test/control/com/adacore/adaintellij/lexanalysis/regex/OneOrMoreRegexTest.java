package com.adacore.adaintellij.lexanalysis.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the OneOrMoreRegex class.
 */
final class OneOrMoreRegexTest {
	
	// Constants
	
	private static final LexerRegex ONE_OR_MORE_LOWERCASE_A_REGEX = new OneOrMoreRegex(new UnitRegex("a"));
	
	private static final LexerRegex ONE_OR_MORE_BRACKETS_REGEX =
		new OneOrMoreRegex(
			new ConcatenationRegex(
				new UnionRegex(new UnitRegex("["), new UnitRegex("{")),
				new UnionRegex(new UnitRegex("]"), new UnitRegex("}"))
			)
		);
	
	private static final LexerRegex ONE_OR_MORE_NULLABLE_REGEX =
		new OneOrMoreRegex(
			new UnionRegex(
				new UnitRegex("abc"),
				new ZeroOrOneRegex(new UnitRegex(","))
			)
		);
	
	// Testing OneOrMoreRegex#nullable() method
	
	@Test
	void one_or_more_regex_is_nullable_if_subregex_is_nullable() {
		
		assertFalse(ONE_OR_MORE_LOWERCASE_A_REGEX.nullable());
		assertFalse(ONE_OR_MORE_BRACKETS_REGEX.nullable());
		
		assertTrue(ONE_OR_MORE_NULLABLE_REGEX.nullable());
		
	}
	
	// Testing OneOrMoreRegex#advanced(char) method
	
	@Test
	void one_or_more_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(ONE_OR_MORE_LOWERCASE_A_REGEX, "A");
		assertRegexDoesNotAdvance(ONE_OR_MORE_LOWERCASE_A_REGEX, "b");
		assertRegexDoesNotAdvance(ONE_OR_MORE_LOWERCASE_A_REGEX, "c");
		assertRegexDoesNotAdvance(ONE_OR_MORE_LOWERCASE_A_REGEX, "ab");
		assertRegexDoesNotAdvance(ONE_OR_MORE_LOWERCASE_A_REGEX, "aaaab");
		
		assertRegexDoesNotAdvance(ONE_OR_MORE_BRACKETS_REGEX, "a");
		assertRegexDoesNotAdvance(ONE_OR_MORE_BRACKETS_REGEX, "B");
		assertRegexDoesNotAdvance(ONE_OR_MORE_BRACKETS_REGEX, "1");
		assertRegexDoesNotAdvance(ONE_OR_MORE_BRACKETS_REGEX, "]");
		assertRegexDoesNotAdvance(ONE_OR_MORE_BRACKETS_REGEX, "}");
		
		assertRegexDoesNotAdvance(ONE_OR_MORE_NULLABLE_REGEX, "b");
		assertRegexDoesNotAdvance(ONE_OR_MORE_NULLABLE_REGEX, "a,");
		
	}
	
	@Test
	void one_or_more_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(ONE_OR_MORE_LOWERCASE_A_REGEX, "");
		
		assertRegexDoesNotMatch(ONE_OR_MORE_BRACKETS_REGEX, "");
		assertRegexDoesNotMatch(ONE_OR_MORE_BRACKETS_REGEX, "[");
		assertRegexDoesNotMatch(ONE_OR_MORE_BRACKETS_REGEX, "{");
		assertRegexDoesNotMatch(ONE_OR_MORE_BRACKETS_REGEX, "{}{");
		assertRegexDoesNotMatch(ONE_OR_MORE_BRACKETS_REGEX, "{}[");
		
		assertRegexDoesNotMatch(ONE_OR_MORE_NULLABLE_REGEX, "ab");
		assertRegexDoesNotMatch(ONE_OR_MORE_NULLABLE_REGEX, ",a");
		assertRegexDoesNotMatch(ONE_OR_MORE_NULLABLE_REGEX, "abcab");
		
	}
	
	@Test
	void one_or_more_regex_matches_when_it_should() {
		
		assertRegexMatches(ONE_OR_MORE_LOWERCASE_A_REGEX, "a");
		assertRegexMatches(ONE_OR_MORE_LOWERCASE_A_REGEX, "aa");
		assertRegexMatches(ONE_OR_MORE_LOWERCASE_A_REGEX, "aaa");
		assertRegexMatches(ONE_OR_MORE_LOWERCASE_A_REGEX, "aaaa");
		
		assertRegexMatches(ONE_OR_MORE_BRACKETS_REGEX, "[]");
		assertRegexMatches(ONE_OR_MORE_BRACKETS_REGEX, "{}");
		assertRegexMatches(ONE_OR_MORE_BRACKETS_REGEX, "{]");
		assertRegexMatches(ONE_OR_MORE_BRACKETS_REGEX, "[}");
		assertRegexMatches(ONE_OR_MORE_BRACKETS_REGEX, "[][]{}[}[]");
		
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, "");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, "abc");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, ",");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, "abcabc");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, "abcabcabc");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, ",,");
		assertRegexMatches(ONE_OR_MORE_NULLABLE_REGEX, ",abc,");
		
	}
	
}
