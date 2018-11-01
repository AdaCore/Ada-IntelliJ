package com.adacore.adaintellij.lexanalysis.regex;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Helper testing methods for regex classes.
 */
final class LexerRegexTestUtils {
	
	/**
	 * Represents the result of applying a regex to a sequence of characters.
	 * Possible values are:
	 *
	 * DOES_NOT_ADVANCE_ON_SEQUENCE         => The regex does not successfully advance
	 *                                         on the sequence of characters.
	 *
	 * ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE => The regex successfully advances but the
	 *                                         advanced regex is not nullable and therefore
	 *                                         does not match the sequence of characters.
	 *
	 * MATCHES_SEQUENCE                     => The regex successfully advances and the
	 *                                         advanced regex is nullable and therefore
	 *                                         matches the sequence of characters.
	 */
	private enum AdvanceState {
		DOES_NOT_ADVANCE_ON_SEQUENCE, ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE, MATCHES_SEQUENCE
	}
	
	/**
	 * Returns the advance state of the given regex with the given
	 * given of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 * @return The advance state.
	 */
	private static AdvanceState regexAdvanceStateOnSequence(LexerRegex regex, String sequence) {
		
		LexerRegex advancedRegex = regex;
		
		for (char character : sequence.toCharArray()) {
			advancedRegex = advancedRegex.advanced(character);
			if (advancedRegex == null) { return AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE; }
		}
		
		return advancedRegex.nullable() ?
			AdvanceState.MATCHES_SEQUENCE : AdvanceState.ADVANCES_BUT_DOES_NOT_MATCH_SEQUENCE;
		
	}
	
	/**
	 * Asserts that the the given regex successfully advances on
	 * the given sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexAdvances(LexerRegex regex, String sequence) {
		assertNotEquals(
			AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}
	
	/**
	 * Asserts that the the given regex does not successfully advance
	 * on the given sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexDoesNotAdvance(LexerRegex regex, String sequence) {
		assertEquals(
			AdvanceState.DOES_NOT_ADVANCE_ON_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}
	
	/**
	 * Asserts that the the given regex successfully advances on
	 * the given sequence of characters and matches it.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexMatches(LexerRegex regex, String sequence) {
		assertEquals(
			AdvanceState.MATCHES_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}
	
	/**
	 * Asserts that the the given regex does not match the given
	 * sequence of characters.
	 *
	 * @param regex The regex to advance.
	 * @param sequence The sequence of characters.
	 */
	static void assertRegexDoesNotMatch(LexerRegex regex, String sequence) {
		assertNotEquals(
			AdvanceState.MATCHES_SEQUENCE,
			regexAdvanceStateOnSequence(regex, sequence)
		);
	}
	
}
