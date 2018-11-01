package com.adacore.adaintellij.lexanalysis.regex;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the ConcatenationRegex class.
 */
final class ConcatenationRegexTest {
	
	// Constants
	
	private static final LexerRegex LOWER_CASE_A_UNIT_REGEX = new UnitRegex("a");
	private static final LexerRegex LOWER_CASE_B_UNIT_REGEX = new UnitRegex("b");
	private static final LexerRegex LOWER_CASE_C_UNIT_REGEX = new UnitRegex("c");
	private static final LexerRegex LOWER_CASE_D_UNIT_REGEX = new UnitRegex("d");
	private static final LexerRegex LOWER_CASE_E_UNIT_REGEX = new UnitRegex("e");
	
	private static final LexerRegex CONCAT_REGEX_1 =
		ConcatenationRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX
		);
	
	private static final LexerRegex CONCAT_REGEX_2 =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("ab"),
			new UnitRegex("cd")
		);
	
	private static final LexerRegex CONCAT_REGEX_3 =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("hello"),
			new UnitRegex("world")
		);
	
	// Testing helper construction methods
	
	@Test
	void helper_construction_methods_return_concat_regex() {
		
		// Initialization
		
		List<LexerRegex> regexes = new ArrayList<>();
		
		regexes.add(LOWER_CASE_A_UNIT_REGEX);
		regexes.add(LOWER_CASE_B_UNIT_REGEX);
		regexes.add(LOWER_CASE_C_UNIT_REGEX);
		regexes.add(LOWER_CASE_D_UNIT_REGEX);
		regexes.add(LOWER_CASE_E_UNIT_REGEX);
		
		// Testing
		
		assertTrue(ConcatenationRegex.fromList(regexes) instanceof ConcatenationRegex);
		
		assertTrue(ConcatenationRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX,
			LOWER_CASE_D_UNIT_REGEX,
			LOWER_CASE_E_UNIT_REGEX
		) instanceof ConcatenationRegex);
		
	}
	
	@Test
	void fromList_constructs_regex_properly() {
		
		// Initialization
		
		int regexCount = 5;
		
		List<LexerRegex> regexes = new ArrayList<>();
		
		for (int i = 0 ; i < regexCount ; i++) {
			regexes.add(new UnitRegex(""));
		}
		
		LexerRegex regex = ConcatenationRegex.fromList(regexes);
		
		// Testing
		
		for (int i = 0 ; i < regexCount - 1 ; i++) {
			
			ConcatenationRegex concatRegex = (ConcatenationRegex)regex;
			
			assertNotNull(concatRegex);
			assertEquals(regexes.get(i), concatRegex.FIRST_REGEX);
			
			regex = concatRegex.SECOND_REGEX;
			
		}
		
		assertEquals(regexes.get(regexCount - 1), regex);
		
	}
	
	@Test
	void fromRegexes_constructs_regex_properly() {
		
		// Initialization
		
		ConcatenationRegex regex =
			(ConcatenationRegex) ConcatenationRegex.fromRegexes(
				LOWER_CASE_A_UNIT_REGEX,
				LOWER_CASE_B_UNIT_REGEX,
				LOWER_CASE_C_UNIT_REGEX,
				LOWER_CASE_D_UNIT_REGEX,
				LOWER_CASE_E_UNIT_REGEX
			);
		
		// Testing
		
		assertEquals(LOWER_CASE_A_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatenationRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_B_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatenationRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_C_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatenationRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_D_UNIT_REGEX, regex.FIRST_REGEX);
		assertEquals(LOWER_CASE_E_UNIT_REGEX, regex.SECOND_REGEX);
		
	}
	
	// Testing ConcatenationRegex#nullable() method
	
	@Test
	void concat_regex_is_nullable_iff_both_subregexes_are_nullable() {
		
		// Initialization
		
		LexerRegex nullableRegex    = new UnitRegex("");
		LexerRegex nonNullableRegex = new UnitRegex("abc");
		
		// Testing
		
		assertFalse(new ConcatenationRegex(nonNullableRegex, nonNullableRegex).nullable());
		assertFalse(new ConcatenationRegex(nonNullableRegex, nullableRegex).nullable());
		assertFalse(new ConcatenationRegex(nullableRegex, nonNullableRegex).nullable());
		assertTrue(new ConcatenationRegex(nullableRegex, nullableRegex).nullable());
		
	}
	
	// Testing ConcatenationRegex#advanced(char) method
	
	@Test
	void concat_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(CONCAT_REGEX_1, "bc");
		assertRegexDoesNotAdvance(CONCAT_REGEX_1, "ABC");
		assertRegexDoesNotAdvance(CONCAT_REGEX_1, "abcd");
		
		assertRegexDoesNotAdvance(CONCAT_REGEX_2, "acd");
		assertRegexDoesNotAdvance(CONCAT_REGEX_2, "bc");
		assertRegexDoesNotAdvance(CONCAT_REGEX_2, "cd");
		assertRegexDoesNotAdvance(CONCAT_REGEX_2, "abcde");
		
		assertRegexDoesNotAdvance(CONCAT_REGEX_3, "ello");
		assertRegexDoesNotAdvance(CONCAT_REGEX_3, "world");
		assertRegexDoesNotAdvance(CONCAT_REGEX_3, "helloworld!");
		
	}
	
	@Test
	void concat_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(CONCAT_REGEX_1, "");
		assertRegexDoesNotMatch(CONCAT_REGEX_1, "a");
		assertRegexDoesNotMatch(CONCAT_REGEX_1, "ab");
		
		assertRegexDoesNotMatch(CONCAT_REGEX_2, "");
		assertRegexDoesNotMatch(CONCAT_REGEX_2, "a");
		assertRegexDoesNotMatch(CONCAT_REGEX_2, "ab");
		assertRegexDoesNotMatch(CONCAT_REGEX_2, "abc");
		
		assertRegexDoesNotMatch(CONCAT_REGEX_3, "");
		assertRegexDoesNotMatch(CONCAT_REGEX_3, "hello");
		assertRegexDoesNotMatch(CONCAT_REGEX_3, "world");
		assertRegexDoesNotMatch(CONCAT_REGEX_3, "helloworl");
		
	}
	
	@Test
	void concat_regex_matches_when_it_should() {
		
		assertRegexMatches(CONCAT_REGEX_1, "abc");
		
		assertRegexMatches(CONCAT_REGEX_2, "abcd");
		
		assertRegexMatches(CONCAT_REGEX_3, "helloworld");
		
	}
	
}
