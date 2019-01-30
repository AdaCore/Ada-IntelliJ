package com.adacore.adaintellij.analysis.lexical.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static com.adacore.adaintellij.analysis.lexical.regex.LexerRegexTestUtils.*;

/**
 * JUnit test class for the GeneralCategoryRegex class.
 */
final class GeneralCategoryRegexTest {

	// Constants

	private static final LexerRegex MARK_SPACING_COMBINING_REGEX    = new GeneralCategoryRegex("Mc");
	private static final LexerRegex PUNCTUATION_CONNECTOR_REGEX     = new GeneralCategoryRegex("Pc");
	private static final LexerRegex OTHER_CONTROL_REGEX             = new GeneralCategoryRegex("Cc");
	private static final LexerRegex SYMBOL_CURRENCY_REGEX           = new GeneralCategoryRegex("Sc");
	private static final LexerRegex PUNCTUATION_DASH_REGEX          = new GeneralCategoryRegex("Pd");
	private static final LexerRegex NUMBER_DECIMAL_REGEX            = new GeneralCategoryRegex("Nd");
	private static final LexerRegex MARK_ENCLOSING_REGEX            = new GeneralCategoryRegex("Me");
	private static final LexerRegex PUNCTUATION_END_REGEX           = new GeneralCategoryRegex("Pe");
	private static final LexerRegex PUNCTUATION_FINAL_QUOTE_REGEX   = new GeneralCategoryRegex("Pf");
	private static final LexerRegex OTHER_FORMAT_REGEX              = new GeneralCategoryRegex("Cf");
	private static final LexerRegex PUNCTUATION_INITIAL_QUOTE_REGEX = new GeneralCategoryRegex("Pi");
	private static final LexerRegex NUMBER_LETTER_REGEX             = new GeneralCategoryRegex("Nl");
	private static final LexerRegex SEPARATOR_LINE_REGEX            = new GeneralCategoryRegex("Zl");
	private static final LexerRegex LETTER_LOWERCASE_REGEX          = new GeneralCategoryRegex("Ll");
	private static final LexerRegex SYMBOL_MATH_REGEX               = new GeneralCategoryRegex("Sm");
	private static final LexerRegex LETTER_MODIFIER_REGEX           = new GeneralCategoryRegex("Lm");
	private static final LexerRegex SYMBOL_MODIFIER_REGEX           = new GeneralCategoryRegex("Sk");
	private static final LexerRegex MARK_NON_SPACING_REGEX          = new GeneralCategoryRegex("Mn");
	private static final LexerRegex LETTER_OTHER_REGEX              = new GeneralCategoryRegex("Lo");
	private static final LexerRegex NUMBER_OTHER_REGEX              = new GeneralCategoryRegex("No");
	private static final LexerRegex PUNCTUATION_OTHER_REGEX         = new GeneralCategoryRegex("Po");
	private static final LexerRegex SYMBOL_OTHER_REGEX              = new GeneralCategoryRegex("So");
	private static final LexerRegex SEPARATOR_PARAGRAPH_REGEX       = new GeneralCategoryRegex("Zp");
	private static final LexerRegex OTHER_PRIVATE_USE_REGEX         = new GeneralCategoryRegex("Co");
	private static final LexerRegex SEPARATOR_SPACE_REGEX           = new GeneralCategoryRegex("Zs");
	private static final LexerRegex PUNCTUATION_START_REGEX         = new GeneralCategoryRegex("Ps");
	private static final LexerRegex OTHER_SURROGATE_REGEX           = new GeneralCategoryRegex("Cs");
	private static final LexerRegex LETTER_TITLECASE_REGEX          = new GeneralCategoryRegex("Lt");
	private static final LexerRegex OTHER_UNASSIGNED_REGEX          = new GeneralCategoryRegex("Cn");
	private static final LexerRegex LETTER_UPPERCASE_REGEX          = new GeneralCategoryRegex("Lu");

	// Testing GeneralCategoryRegex#nullable() method

	@Test
	void general_category_regex_is_never_nullable() {

		assertFalse(MARK_SPACING_COMBINING_REGEX.nullable());
		assertFalse(PUNCTUATION_CONNECTOR_REGEX.nullable());
		assertFalse(OTHER_CONTROL_REGEX.nullable());
		assertFalse(SYMBOL_CURRENCY_REGEX.nullable());
		assertFalse(PUNCTUATION_DASH_REGEX.nullable());
		assertFalse(NUMBER_DECIMAL_REGEX.nullable());
		assertFalse(MARK_ENCLOSING_REGEX.nullable());
		assertFalse(PUNCTUATION_END_REGEX.nullable());
		assertFalse(PUNCTUATION_FINAL_QUOTE_REGEX.nullable());
		assertFalse(OTHER_FORMAT_REGEX.nullable());
		assertFalse(PUNCTUATION_INITIAL_QUOTE_REGEX.nullable());
		assertFalse(NUMBER_LETTER_REGEX.nullable());
		assertFalse(SEPARATOR_LINE_REGEX.nullable());
		assertFalse(LETTER_LOWERCASE_REGEX.nullable());
		assertFalse(SYMBOL_MATH_REGEX.nullable());
		assertFalse(LETTER_MODIFIER_REGEX.nullable());
		assertFalse(SYMBOL_MODIFIER_REGEX.nullable());
		assertFalse(MARK_NON_SPACING_REGEX.nullable());
		assertFalse(LETTER_OTHER_REGEX.nullable());
		assertFalse(NUMBER_OTHER_REGEX.nullable());
		assertFalse(PUNCTUATION_OTHER_REGEX.nullable());
		assertFalse(SYMBOL_OTHER_REGEX.nullable());
		assertFalse(SEPARATOR_PARAGRAPH_REGEX.nullable());
		assertFalse(OTHER_PRIVATE_USE_REGEX.nullable());
		assertFalse(SEPARATOR_SPACE_REGEX.nullable());
		assertFalse(PUNCTUATION_START_REGEX.nullable());
		assertFalse(OTHER_SURROGATE_REGEX.nullable());
		assertFalse(LETTER_TITLECASE_REGEX.nullable());
		assertFalse(OTHER_UNASSIGNED_REGEX.nullable());
		assertFalse(LETTER_UPPERCASE_REGEX.nullable());

	}

	// Testing GeneralCategoryRegex#advanced(char) method

	@Test
	void intersection_regex_does_not_advance_when_it_should_not() {

		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, "A");
		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, "B");
		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, "C");
		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, "1");
		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, ",");
		assertRegexDoesNotAdvance(LETTER_LOWERCASE_REGEX, "ab");

		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "a");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "B");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "-");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "]");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, ".");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "<");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "()");
		assertRegexDoesNotAdvance(PUNCTUATION_START_REGEX, "((");

	}

	@Test
	void not_regex_matches_when_it_should() {

		assertRegexMatches(LETTER_LOWERCASE_REGEX, "a");
		assertRegexMatches(LETTER_LOWERCASE_REGEX, "b");
		assertRegexMatches(LETTER_LOWERCASE_REGEX, "c");
		assertRegexMatches(LETTER_LOWERCASE_REGEX, "z");

		assertRegexMatches(PUNCTUATION_START_REGEX, "(");
		assertRegexMatches(PUNCTUATION_START_REGEX, "[");
		assertRegexMatches(PUNCTUATION_START_REGEX, "{");

	}

}
