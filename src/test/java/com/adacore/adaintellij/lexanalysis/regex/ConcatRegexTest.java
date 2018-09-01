package com.adacore.adaintellij.lexanalysis.regex;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.OORegexTestUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the ConcatRegex class.
 */
final class ConcatRegexTest {
	
	// Constants
	
	private static final OORegex LOWER_CASE_A_UNIT_REGEX = new UnitRegex("a");
	private static final OORegex LOWER_CASE_B_UNIT_REGEX = new UnitRegex("b");
	private static final OORegex LOWER_CASE_C_UNIT_REGEX = new UnitRegex("c");
	private static final OORegex LOWER_CASE_D_UNIT_REGEX = new UnitRegex("d");
	private static final OORegex LOWER_CASE_E_UNIT_REGEX = new UnitRegex("e");
	
	private static final OORegex CONCAT_REGEX_1 =
		ConcatRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX
		);
	
	private static final OORegex CONCAT_REGEX_2 =
		ConcatRegex.fromRegexes(
			new UnitRegex("ab"),
			new UnitRegex("cd")
		);
	
	private static final OORegex CONCAT_REGEX_3 =
		ConcatRegex.fromRegexes(
			new UnitRegex("hello"),
			new UnitRegex("world")
		);
	
	// Testing helper construction methods
	
	@Test
	void helper_construction_methods_return_concat_regex() {
		
		// Initialization
		
		List<OORegex> regexes = new ArrayList<>();
		
		regexes.add(LOWER_CASE_A_UNIT_REGEX);
		regexes.add(LOWER_CASE_B_UNIT_REGEX);
		regexes.add(LOWER_CASE_C_UNIT_REGEX);
		regexes.add(LOWER_CASE_D_UNIT_REGEX);
		regexes.add(LOWER_CASE_E_UNIT_REGEX);
		
		// Testing
		
		assertTrue(ConcatRegex.fromList(regexes) instanceof ConcatRegex);
		
		assertTrue(ConcatRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX,
			LOWER_CASE_D_UNIT_REGEX,
			LOWER_CASE_E_UNIT_REGEX
		) instanceof ConcatRegex);
		
	}
	
	@Test
	void fromList_constructs_regex_properly() {
		
		// Initialization
		
		int regexCount = 5;
		
		List<OORegex> regexes = new ArrayList<>();
		
		for (int i = 0 ; i < regexCount ; i++) {
			regexes.add(new UnitRegex(""));
		}
		
		OORegex regex = ConcatRegex.fromList(regexes);
		
		// Testing
		
		for (int i = 0 ; i < regexCount - 1 ; i++) {
			
			ConcatRegex concatRegex = (ConcatRegex)regex;
			
			assertNotNull(concatRegex);
			assertEquals(regexes.get(i), concatRegex.FIRST_REGEX);
			
			regex = concatRegex.SECOND_REGEX;
			
		}
		
		assertEquals(regexes.get(regexCount - 1), regex);
		
	}
	
	@Test
	void fromRegexes_constructs_regex_properly() {
		
		// Initialization
		
		ConcatRegex regex =
			(ConcatRegex)ConcatRegex.fromRegexes(
				LOWER_CASE_A_UNIT_REGEX,
				LOWER_CASE_B_UNIT_REGEX,
				LOWER_CASE_C_UNIT_REGEX,
				LOWER_CASE_D_UNIT_REGEX,
				LOWER_CASE_E_UNIT_REGEX
			);
		
		// Testing
		
		assertEquals(LOWER_CASE_A_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_B_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_C_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (ConcatRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_D_UNIT_REGEX, regex.FIRST_REGEX);
		assertEquals(LOWER_CASE_E_UNIT_REGEX, regex.SECOND_REGEX);
		
	}
	
	// Testing ConcatRegex#nullable() method
	
	@Test
	void concat_regex_is_nullable_iff_both_subregexes_are_nullable() {
		
		// Initialization
		
		OORegex nullableRegex    = new UnitRegex("");
		OORegex nonNullableRegex = new UnitRegex("abc");
		
		// Testing
		
		assertFalse(new ConcatRegex(nonNullableRegex, nonNullableRegex).nullable());
		assertFalse(new ConcatRegex(nonNullableRegex, nullableRegex).nullable());
		assertFalse(new ConcatRegex(nullableRegex, nonNullableRegex).nullable());
		assertTrue(new ConcatRegex(nullableRegex, nullableRegex).nullable());
		
	}
	
	// Testing ConcatRegex#advanced(char) method
	
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
