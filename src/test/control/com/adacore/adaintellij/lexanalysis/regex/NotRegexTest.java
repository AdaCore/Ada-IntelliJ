package com.adacore.adaintellij.lexanalysis.regex;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.OORegexTestUtils.*;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the NotRegex class.
 */
final class NotRegexTest {
	
	// Constants
	
	private static final OORegex NOT_LOWERCASE_A_REGEX      = new NotRegex(new UnitRegex("a"));
	private static final OORegex NOT_LOWERCASE_LETTER_REGEX = new NotRegex(UnionRegex.fromRange('a', 'z'));
	
	// Testing NotRegex#nullable() method
	
	@Test
	void not_regex_is_not_nullable() {
		assertFalse(NOT_LOWERCASE_A_REGEX.nullable());
		assertFalse(NOT_LOWERCASE_LETTER_REGEX.nullable());
	}
	
	// Testing NotRegex#advanced(char) method
	
	@Test
	void not_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(NOT_LOWERCASE_A_REGEX, "a");
		assertRegexDoesNotAdvance(NOT_LOWERCASE_A_REGEX, "bb");
		
		assertRegexDoesNotAdvance(NOT_LOWERCASE_LETTER_REGEX, "a");
		assertRegexDoesNotAdvance(NOT_LOWERCASE_LETTER_REGEX, "b");
		assertRegexDoesNotAdvance(NOT_LOWERCASE_LETTER_REGEX, "c");
		assertRegexDoesNotAdvance(NOT_LOWERCASE_LETTER_REGEX, "z");
		assertRegexDoesNotAdvance(NOT_LOWERCASE_LETTER_REGEX, "ab");
		
	}
	
	@Test
	void not_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(NOT_LOWERCASE_A_REGEX, "123");
		
		assertRegexDoesNotMatch(NOT_LOWERCASE_LETTER_REGEX, "ABC");
		
	}
	
	@Test
	void not_regex_matches_when_it_should() {
		
		assertRegexMatches(NOT_LOWERCASE_A_REGEX, "A");
		assertRegexMatches(NOT_LOWERCASE_A_REGEX, "b");
		assertRegexMatches(NOT_LOWERCASE_A_REGEX, "1");
		assertRegexMatches(NOT_LOWERCASE_A_REGEX, "/");
		assertRegexMatches(NOT_LOWERCASE_A_REGEX, "\t");
		
		assertRegexMatches(NOT_LOWERCASE_LETTER_REGEX, "1");
		assertRegexMatches(NOT_LOWERCASE_LETTER_REGEX, "/");
		assertRegexMatches(NOT_LOWERCASE_LETTER_REGEX, "_");
		assertRegexMatches(NOT_LOWERCASE_LETTER_REGEX, "A");
		assertRegexMatches(NOT_LOWERCASE_LETTER_REGEX, "\n");
		
	}
	
}
