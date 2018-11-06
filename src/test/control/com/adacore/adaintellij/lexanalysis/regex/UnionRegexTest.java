package com.adacore.adaintellij.lexanalysis.regex;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.lexanalysis.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the UnionRegex class.
 */
final class UnionRegexTest {
	
	// Constants
	
	private static final LexerRegex LOWER_CASE_A_UNIT_REGEX = new UnitRegex("a");
	private static final LexerRegex LOWER_CASE_B_UNIT_REGEX = new UnitRegex("b");
	private static final LexerRegex LOWER_CASE_C_UNIT_REGEX = new UnitRegex("c");
	private static final LexerRegex LOWER_CASE_D_UNIT_REGEX = new UnitRegex("d");
	private static final LexerRegex LOWER_CASE_E_UNIT_REGEX = new UnitRegex("e");
	
	private static final LexerRegex UNION_REGEX_1 =
		UnionRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX
		);
	
	private static final LexerRegex UNION_REGEX_2 =
		UnionRegex.fromRegexes(
			new UnitRegex("abc"),
			new UnitRegex("cba")
		);
	
	private static final LexerRegex UNION_REGEX_3 =
		UnionRegex.fromRegexes(
			new UnitRegex("hello"),
			new UnitRegex("h")
		);
	
	// Testing helper construction methods
	
	@Test
	void helper_construction_methods_return_union_regex() {
		
		// Initialization
		
		List<LexerRegex> regexes = new ArrayList<>();
		
		regexes.add(LOWER_CASE_A_UNIT_REGEX);
		regexes.add(LOWER_CASE_B_UNIT_REGEX);
		regexes.add(LOWER_CASE_C_UNIT_REGEX);
		regexes.add(LOWER_CASE_D_UNIT_REGEX);
		regexes.add(LOWER_CASE_E_UNIT_REGEX);
		
		// Testing
		
		assertTrue(UnionRegex.fromList(regexes) instanceof UnionRegex);
		
		assertTrue(UnionRegex.fromRegexes(
			LOWER_CASE_A_UNIT_REGEX,
			LOWER_CASE_B_UNIT_REGEX,
			LOWER_CASE_C_UNIT_REGEX,
			LOWER_CASE_D_UNIT_REGEX,
			LOWER_CASE_E_UNIT_REGEX
		) instanceof UnionRegex);
		
		assertTrue(UnionRegex.fromRange('a', 'c') instanceof UnionRegex);
		
	}
	
	@Test
	void fromList_constructs_regex_properly() {
		
		// Initialization
		
		int regexCount = 5;
		
		List<LexerRegex> regexes = new ArrayList<>();
		
		for (int i = 0 ; i < regexCount ; i++) {
			regexes.add(new UnitRegex(""));
		}
		
		LexerRegex regex = UnionRegex.fromList(regexes);
		
		// Testing
		
		for (int i = 0 ; i < regexCount - 1 ; i++) {
			
			UnionRegex unionRegex = (UnionRegex)regex;
			
			assertNotNull(unionRegex);
			assertEquals(regexes.get(i), unionRegex.FIRST_REGEX);
			
			regex = unionRegex.SECOND_REGEX;
			
		}
		
		assertEquals(regexes.get(regexCount - 1), regex);
		
	}
	
	@Test
	void fromRegexes_constructs_regex_properly() {
		
		// Initialization
		
		UnionRegex regex =
			(UnionRegex)UnionRegex.fromRegexes(
				LOWER_CASE_A_UNIT_REGEX,
				LOWER_CASE_B_UNIT_REGEX,
				LOWER_CASE_C_UNIT_REGEX,
				LOWER_CASE_D_UNIT_REGEX,
				LOWER_CASE_E_UNIT_REGEX
			);
		
		// Testing
		
		assertEquals(LOWER_CASE_A_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_B_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_C_UNIT_REGEX, regex.FIRST_REGEX);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals(LOWER_CASE_D_UNIT_REGEX, regex.FIRST_REGEX);
		assertEquals(LOWER_CASE_E_UNIT_REGEX, regex.SECOND_REGEX);
		
	}
	
	@Test
	void fromRange_constructs_regex_properly() {
		
		// Initialization
		
		UnionRegex regex = (UnionRegex)UnionRegex.fromRange('a', 'e');
		
		// Testing
		
		assertEquals("a", ((UnitRegex)regex.FIRST_REGEX).SEQUENCE);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals("b", ((UnitRegex)regex.FIRST_REGEX).SEQUENCE);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals("c", ((UnitRegex)regex.FIRST_REGEX).SEQUENCE);
		regex = (UnionRegex)regex.SECOND_REGEX;
		assertEquals("d", ((UnitRegex)regex.FIRST_REGEX).SEQUENCE);
		assertEquals("e", ((UnitRegex)regex.SECOND_REGEX).SEQUENCE);
		
	}
	
	// Testing UnionRegex#nullable() method
	
	@Test
	void union_regex_is_nullable_iff_one_of_subregexes_is_nullable() {
		
		// Initialization
		
		LexerRegex nullableRegex    = new UnitRegex("");
		LexerRegex nonNullableRegex = new UnitRegex("abc");
		
		// Testing
		
		assertFalse(new UnionRegex(nonNullableRegex, nonNullableRegex).nullable());
		assertTrue(new UnionRegex(nonNullableRegex, nullableRegex).nullable());
		assertTrue(new UnionRegex(nullableRegex, nonNullableRegex).nullable());
		assertTrue(new UnionRegex(nullableRegex, nullableRegex).nullable());
		
	}
	
	// Testing UnionRegex#advanced(char) method
	
	@Test
	void union_regex_does_not_advance_when_it_should_not() {
		
		assertRegexDoesNotAdvance(UNION_REGEX_1, "d");
		assertRegexDoesNotAdvance(UNION_REGEX_1, "A");
		assertRegexDoesNotAdvance(UNION_REGEX_1, " ");
		assertRegexDoesNotAdvance(UNION_REGEX_1, "ab");
		assertRegexDoesNotAdvance(UNION_REGEX_1, "abc");
		
		assertRegexDoesNotAdvance(UNION_REGEX_2, "d");
		assertRegexDoesNotAdvance(UNION_REGEX_2, "aba");
		assertRegexDoesNotAdvance(UNION_REGEX_2, "cbc");
		assertRegexDoesNotAdvance(UNION_REGEX_2, "bca");
		
		assertRegexDoesNotAdvance(UNION_REGEX_3, "ello");
		assertRegexDoesNotAdvance(UNION_REGEX_3, "hello!");
		
	}
	
	@Test
	void union_regex_does_not_match_when_it_should_not() {
		
		assertRegexDoesNotMatch(UNION_REGEX_1, "");
		
		assertRegexDoesNotMatch(UNION_REGEX_2, "");
		assertRegexDoesNotMatch(UNION_REGEX_2, "a");
		assertRegexDoesNotMatch(UNION_REGEX_2, "c");
		assertRegexDoesNotMatch(UNION_REGEX_2, "ab");
		assertRegexDoesNotMatch(UNION_REGEX_2, "cb");
		
		assertRegexDoesNotMatch(UNION_REGEX_3, "");
		assertRegexDoesNotMatch(UNION_REGEX_3, "hell");
		
	}
	
	@Test
	void union_regex_matches_when_it_should() {
		
		assertRegexMatches(UNION_REGEX_1, "a");
		assertRegexMatches(UNION_REGEX_1, "b");
		assertRegexMatches(UNION_REGEX_1, "c");
		
		assertRegexMatches(UNION_REGEX_2, "abc");
		assertRegexMatches(UNION_REGEX_2, "cba");
		
		assertRegexMatches(UNION_REGEX_3, "hello");
		assertRegexMatches(UNION_REGEX_3, "h");
		
	}
	
}
