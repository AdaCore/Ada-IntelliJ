package com.adacore.adaintellij.analysis.lexical.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.analysis.lexical.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the UnitRegex class.
 */
final class UnitRegexTest {
	
	// Constants
	
	private static final LexerRegex EMPTY_UNIT_REGEX         = new UnitRegex("");
	private static final LexerRegex LOWERCASE_ABC_UNIT_REGEX = new UnitRegex("abc");
	
	// Testing UnitRegex#nullable() method
	
	@Test
	void empty_string_unit_regex_is_nullable() {
		assertTrue(EMPTY_UNIT_REGEX.nullable());
	}
	
	@Test
	void non_empty_string_unit_regex_is_not_nullable() {
		assertFalse(LOWERCASE_ABC_UNIT_REGEX.nullable());
	}
	
	// Testing UnitRegex#advanced(char) method
	
	@Test
	void unit_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(EMPTY_UNIT_REGEX, "a");
		
		assertRegexDoesNotAdvance(LOWERCASE_ABC_UNIT_REGEX, "d");
		assertRegexDoesNotAdvance(LOWERCASE_ABC_UNIT_REGEX, "bc");
		assertRegexDoesNotAdvance(LOWERCASE_ABC_UNIT_REGEX, "abd");
		assertRegexDoesNotAdvance(LOWERCASE_ABC_UNIT_REGEX, "abcd");
		
	}
	
	@Test
	void unit_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(LOWERCASE_ABC_UNIT_REGEX, "");
		assertRegexDoesNotMatch(LOWERCASE_ABC_UNIT_REGEX, "a");
		assertRegexDoesNotMatch(LOWERCASE_ABC_UNIT_REGEX, "ab");
		
	}
	
	@Test
	void unit_regex_matches_when_it_should() {
		
		assertRegexMatches(EMPTY_UNIT_REGEX, "");
		
		assertRegexMatches(LOWERCASE_ABC_UNIT_REGEX, "abc");
		
	}
	
}
