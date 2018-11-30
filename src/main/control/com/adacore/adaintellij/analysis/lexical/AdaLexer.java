package com.adacore.adaintellij.analysis.lexical;

import java.util.*;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.lexical.regex.*;

import static com.adacore.adaintellij.analysis.lexical.AdaTokenTypes.*;

/**
 * Lexical Analyser for Ada 2012 (ISO/IEC 8652:2012(E)).
 */
public final class AdaLexer extends LexerBase {
	
	/*
		Constants
	*/
	
	// Whitespaces
	
	/**
	 * Regexes matching different whitespace characters.
	 */
	private static final LexerRegex HORIZONTAL_TABULATION_REGEX = new UnitRegex("\t");
	private static final LexerRegex LINE_FEED_REGEX             = new UnitRegex("\n");
	private static final LexerRegex VERTICAL_TABULATION_REGEX   = new UnitRegex("\u000b");
	private static final LexerRegex FORM_FEED_REGEX             = new UnitRegex("\f");
	private static final LexerRegex CARRIAGE_RETURN_REGEX       = new UnitRegex("\r");
	private static final LexerRegex SPACE_REGEX                 = new UnitRegex("\u0020");
	private static final LexerRegex NEXT_LINE_REGEX             = new UnitRegex("\u0085");
	private static final LexerRegex NO_BREAK_SPACE_REGEX        = new UnitRegex("\u00a0");
	
	/**
	 * Regex defining a sequence of whitespaces in Ada.
	 */
	private static final LexerRegex WHITESPACES_REGEX =
		new OneOrMoreRegex(
			UnionRegex.fromRegexes(
				HORIZONTAL_TABULATION_REGEX,
				LINE_FEED_REGEX,
				VERTICAL_TABULATION_REGEX,
				FORM_FEED_REGEX,
				CARRIAGE_RETURN_REGEX,
				SPACE_REGEX,
				NEXT_LINE_REGEX,
				NO_BREAK_SPACE_REGEX
			)
		);
	
	// Delimiters
	
	/**
	 * Unit regexes for matching Ada single delimiters.
	 */
	private static final LexerRegex AMPERSAND_REGEX         = new UnitRegex(AMPERSAND.TOKEN_TEXT);
	private static final LexerRegex APOSTROPHE_REGEX        = new UnitRegex(APOSTROPHE.TOKEN_TEXT);
	private static final LexerRegex LEFT_PARENTHESIS_REGEX  = new UnitRegex(LEFT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex RIGHT_PARENTHESIS_REGEX = new UnitRegex(RIGHT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex ASTERISK_REGEX          = new UnitRegex(ASTERISK.TOKEN_TEXT);
	private static final LexerRegex PLUS_SIGN_REGEX         = new UnitRegex(PLUS_SIGN.TOKEN_TEXT);
	private static final LexerRegex COMMA_REGEX             = new UnitRegex(COMMA.TOKEN_TEXT);
	private static final LexerRegex HYPHEN_MINUS_REGEX      = new UnitRegex(HYPHEN_MINUS.TOKEN_TEXT);
	private static final LexerRegex FULL_STOP_REGEX         = new UnitRegex(FULL_STOP.TOKEN_TEXT);
	private static final LexerRegex SOLIDUS_REGEX           = new UnitRegex(SOLIDUS.TOKEN_TEXT);
	private static final LexerRegex COLON_REGEX             = new UnitRegex(COLON.TOKEN_TEXT);
	private static final LexerRegex SEMICOLON_REGEX         = new UnitRegex(SEMICOLON.TOKEN_TEXT);
	private static final LexerRegex LESS_THAN_SIGN_REGEX    = new UnitRegex(LESS_THAN_SIGN.TOKEN_TEXT);
	private static final LexerRegex EQUALS_SIGN_REGEX       = new UnitRegex(EQUALS_SIGN.TOKEN_TEXT);
	private static final LexerRegex GREATER_THAN_SIGN_REGEX = new UnitRegex(GREATER_THAN_SIGN.TOKEN_TEXT);
	private static final LexerRegex VERTICAL_LINE_REGEX     = new UnitRegex(VERTICAL_LINE.TOKEN_TEXT);
	
	/**
	 * Unit regexes for matching Ada compound delimiters.
	 */
	private static final LexerRegex ARROW_REGEX               = new UnitRegex(ARROW.TOKEN_TEXT);
	private static final LexerRegex DOUBLE_DOT_REGEX          = new UnitRegex(DOUBLE_DOT.TOKEN_TEXT);
	private static final LexerRegex DOUBLE_ASTERISK_REGEX     = new UnitRegex(DOUBLE_ASTERISK.TOKEN_TEXT);
	private static final LexerRegex ASSIGNMENT_REGEX          = new UnitRegex(ASSIGNMENT.TOKEN_TEXT);
	private static final LexerRegex NOT_EQUAL_SIGN_REGEX      = new UnitRegex(NOT_EQUAL_SIGN.TOKEN_TEXT);
	private static final LexerRegex GREATER_EQUAL_SIGN_REGEX  = new UnitRegex(GREATER_EQUAL_SIGN.TOKEN_TEXT);
	private static final LexerRegex LESS_EQUAL_SIGN_REGEX     = new UnitRegex(LESS_EQUAL_SIGN.TOKEN_TEXT);
	private static final LexerRegex LEFT_LABEL_BRACKET_REGEX  = new UnitRegex(LEFT_LABEL_BRACKET.TOKEN_TEXT);
	private static final LexerRegex RIGHT_LABEL_BRACKET_REGEX = new UnitRegex(RIGHT_LABEL_BRACKET.TOKEN_TEXT);
	private static final LexerRegex BOX_SIGN_REGEX            = new UnitRegex(BOX_SIGN.TOKEN_TEXT);
	
	// Character Categories
	
	/**
	 * Regexes matching characters based on their "General Category"
	 * as defined by the Unicode standard.
	 */
	private static final LexerRegex LETTER_UPPERCASE_REGEX       = new GeneralCategoryRegex("Lu");
	private static final LexerRegex LETTER_LOWERCASE_REGEX       = new GeneralCategoryRegex("Ll");
	private static final LexerRegex LETTER_TITLECASE_REGEX       = new GeneralCategoryRegex("Lt");
	private static final LexerRegex LETTER_MODIFIER_REGEX        = new GeneralCategoryRegex("Lm");
	private static final LexerRegex LETTER_OTHER_REGEX           = new GeneralCategoryRegex("Lo");
	private static final LexerRegex MARK_NON_SPACING_REGEX       = new GeneralCategoryRegex("Mn");
	private static final LexerRegex MARK_SPACING_COMBINING_REGEX = new GeneralCategoryRegex("Mc");
	private static final LexerRegex NUMBER_DECIMAL_REGEX         = new GeneralCategoryRegex("Nd");
	private static final LexerRegex NUMBER_LETTER_REGEX          = new GeneralCategoryRegex("Nl");
	private static final LexerRegex PUNCTUATION_CONNECTOR_REGEX  = new GeneralCategoryRegex("Pc");
	private static final LexerRegex OTHER_FORMAT_REGEX           = new GeneralCategoryRegex("Cf"); // Currently not used
	private static final LexerRegex SEPARATOR_SPACE_REGEX        = new GeneralCategoryRegex("Zs"); // Currently not used
	private static final LexerRegex SEPARATOR_LINE_REGEX         = new GeneralCategoryRegex("Zl");
	private static final LexerRegex SEPARATOR_PARAGRAPH_REGEX    = new GeneralCategoryRegex("Zp");
	private static final LexerRegex OTHER_PRIVATE_USE_REGEX      = new GeneralCategoryRegex("Co");
	private static final LexerRegex OTHER_SURROGATE_REGEX        = new GeneralCategoryRegex("Cs");
	
	/**
	 * Regexes matching various character classes defined by
	 * the Ada 2012 specification.
	 */
	private static final LexerRegex FORMAT_EFFECTOR_REGEX =
		UnionRegex.fromRegexes(
			HORIZONTAL_TABULATION_REGEX,
			LINE_FEED_REGEX,
			VERTICAL_TABULATION_REGEX,
			FORM_FEED_REGEX,
			CARRIAGE_RETURN_REGEX,
			NEXT_LINE_REGEX,
			SEPARATOR_LINE_REGEX,
			SEPARATOR_PARAGRAPH_REGEX
		);
	
	private static final LexerRegex OTHER_CONTROL_REGEX =
		new IntersectionRegex(
			new GeneralCategoryRegex("Cc"),
			new NotRegex(FORMAT_EFFECTOR_REGEX)
		);
	
	private static final LexerRegex GRAPHIC_CHARACTER_REGEX =
		new NotRegex(
			UnionRegex.fromRegexes(
				OTHER_CONTROL_REGEX,
				OTHER_PRIVATE_USE_REGEX,
				OTHER_SURROGATE_REGEX,
				FORMAT_EFFECTOR_REGEX,
				new UnitRegex("\ufffe"),
				new UnitRegex("\uffff")
			)
		);
	
	// Identifiers
	
	/**
	 * Regex defining the first character of an Ada identifier.
	 *
	 * identifier_start ::=
	 *     letter_uppercase
	 *   | letter_lowercase
	 *   | letter_titlecase
	 *   | letter_modifier
	 *   | letter_other
	 *   | number_letter
	 */
	private static final LexerRegex IDENTIFIER_START_REGEX =
		UnionRegex.fromRegexes(
			LETTER_UPPERCASE_REGEX,
			LETTER_LOWERCASE_REGEX,
			LETTER_TITLECASE_REGEX,
			LETTER_MODIFIER_REGEX,
			LETTER_OTHER_REGEX,
			NUMBER_LETTER_REGEX
		);
	
	/**
	 * Regex defining the additional character categories allowed
	 * for non-first characters in an Ada identifier.
	 *
	 * identifier_extend ::=
	 *     mark_non_spacing
	 *   | mark_spacing_combining
	 *   | number_decimal
	 *   | punctuation_connector
	 */
	private static final LexerRegex IDENTIFIER_EXTEND_REGEX =
		UnionRegex.fromRegexes(
			MARK_NON_SPACING_REGEX,
			MARK_SPACING_COMBINING_REGEX,
			NUMBER_DECIMAL_REGEX,
			PUNCTUATION_CONNECTOR_REGEX
		);
	
	/**
	 * Regex defining an Ada identifier.
	 *
	 * identifier ::=
	 *     identifier_start {identifier_start | identifier_extend}
	 */
	private static final LexerRegex IDENTIFIER_REGEX =
		new ConcatenationRegex(
			IDENTIFIER_START_REGEX,
			new ZeroOrMoreRegex(
				new UnionRegex(
					IDENTIFIER_START_REGEX,
					IDENTIFIER_EXTEND_REGEX
				)
			)
		);
	
	// Numeric Literals
	
	/**
	 * Regex defining a digit.
	 *
	 * digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
	 */
	private static final LexerRegex DIGIT_REGEX = UnionRegex.fromRange('0', '9');
	
	/**
	 * Regex defining a numeral (used to define numeric literals).
	 *
	 * numeral ::= digit {[underline] digit}
	 */
	private static final LexerRegex NUMERAL_REGEX =
		new ConcatenationRegex(
			DIGIT_REGEX,
			new ZeroOrMoreRegex(
				new ConcatenationRegex(
					new ZeroOrOneRegex(new UnitRegex("_")),
					DIGIT_REGEX
				)
			)
		);
	
	/**
	 * Regex defining an exponent (used to define numeric literals).
	 *
	 * exponent ::= e [+] numeral | e – numeral
	 */
	private static final LexerRegex EXPONENT_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("e"),
			UnionRegex.fromRegexes(
				new UnitRegex("-"),
				new ZeroOrOneRegex(new UnitRegex("+"))
			),
			NUMERAL_REGEX
		);
	
	// Decimal Literals
	
	/**
	 * Regex defining an Ada decimal literal.
	 *
	 * decimal_literal ::= numeral [.numeral] [exponent]
	 */
	private static final LexerRegex DECIMAL_LITERAL_REGEX =
		ConcatenationRegex.fromRegexes(
			NUMERAL_REGEX,
			new ZeroOrOneRegex(
				new ConcatenationRegex(
					new UnitRegex("."),
					NUMERAL_REGEX
				)
			),
			new ZeroOrOneRegex(EXPONENT_REGEX)
		);
	
	// Based Literals
	
	/**
	 * Regex defining a base (used to define based literals).
	 *
	 * base ::= numeral
	 */
	private static final LexerRegex BASE_REGEX = NUMERAL_REGEX;
	
	/**
	 * Regex defining an extended digit (hexadecimal digit).
	 *
	 * extended_digit ::= digit | a | b | c | d | e | f
	 */
	private static final LexerRegex EXTENDED_DIGIT_REGEX =
		UnionRegex.fromRegexes(
			DIGIT_REGEX,
			new UnitRegex("a"),
			new UnitRegex("b"),
			new UnitRegex("c"),
			new UnitRegex("d"),
			new UnitRegex("e"),
			new UnitRegex("f")
		);
	
	/**
	 * Regex defining a based numeral (used to define based literals).
	 *
	 * based_numeral ::=
	 *     extended_digit {[underline] extended_digit}
	 */
	private static final LexerRegex BASED_NUMERAL_REGEX =
		new ConcatenationRegex(
			EXTENDED_DIGIT_REGEX,
			new ZeroOrMoreRegex(
				new ConcatenationRegex(
					new ZeroOrOneRegex(new UnitRegex("_")),
					EXTENDED_DIGIT_REGEX
				)
			)
		);
	
	/**
	 * Regex defining an Ada based literal.
	 *
	 * based_literal ::=
	 *     base # based_numeral [.based_numeral] # [exponent]
	 */
	private static final LexerRegex BASED_LITERAL_REGEX =
		ConcatenationRegex.fromRegexes(
			BASE_REGEX,
			new UnitRegex("#"),
			BASED_NUMERAL_REGEX,
			new ZeroOrOneRegex(
				new ConcatenationRegex(
					new UnitRegex("."),
					BASED_NUMERAL_REGEX
				)
			),
			new UnitRegex("#"),
			new ZeroOrOneRegex(EXPONENT_REGEX)
		);
	
	// Character Literals
	
	/**
	 * Regex defining an Ada character literal.
	 *
	 * character_literal ::= 'graphic_character'
	 */
	private static final LexerRegex CHARACTER_LITERAL_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("'"),
			GRAPHIC_CHARACTER_REGEX,
			new UnitRegex("'")
		);
	
	// String Literals
	
	/**
	 * Regex defining a non-quotation-mark graphic character (used
	 * to define string literals).
	 *
	 * A non-quotation-mark graphic character is defined as any
	 * graphic_character other than the quotation mark character '"'
	 */
	private static final LexerRegex NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX =
		new IntersectionRegex(
			GRAPHIC_CHARACTER_REGEX,
			new NotRegex(new UnitRegex("\""))
		);
	
	/**
	 * Regex defining a string element (used to define string literals).
	 *
	 * string_element ::= "" | non_quotation_mark_graphic_character
	 */
	private static final LexerRegex STRING_ELEMENT_REGEX =
		new UnionRegex(
			new UnitRegex("\"\""),
			NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX
		);
	
	/**
	 * Regex defining an Ada string literal.
	 *
	 * string_literal ::= "{string_element}"
	 */
	private static final LexerRegex STRING_LITERAL_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("\""),
			new ZeroOrMoreRegex(STRING_ELEMENT_REGEX),
			new UnitRegex("\"")
		);
	
	// Comments
	
	/**
	 * Regex defining a non-end-of-line character (useed to define comments).
	 */
	private static final LexerRegex NON_END_OF_LINE_CHARACTER_REGEX =
		new NotRegex(
			UnionRegex.fromRegexes(
				LINE_FEED_REGEX,
				VERTICAL_TABULATION_REGEX,
				FORM_FEED_REGEX,
				CARRIAGE_RETURN_REGEX,
				NEXT_LINE_REGEX,
				SEPARATOR_LINE_REGEX,
				SEPARATOR_PARAGRAPH_REGEX
			)
		);
	
	/**
	 * Regex defining an Ada comment.
	 *
	 * comment ::= --{non_end_of_line_character}
	 */
	private static final LexerRegex COMMENT_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("--"),
			new ZeroOrMoreRegex(NON_END_OF_LINE_CHARACTER_REGEX)
		);
	
	// Keywords
	
	/**
	 * Unit regexes for matching Ada keywords.
	 */
	private static final LexerRegex ABORT_KEYWORD_REGEX        = new UnitRegex(ABORT_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex ABS_KEYWORD_REGEX          = new UnitRegex(ABS_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex ABSTRACT_KEYWORD_REGEX     = new UnitRegex(ABSTRACT_KEYWORD.TOKEN_TEXT    , 1);
	private static final LexerRegex ACCEPT_KEYWORD_REGEX       = new UnitRegex(ACCEPT_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex ACCESS_KEYWORD_REGEX       = new UnitRegex(ACCESS_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex ALIASED_KEYWORD_REGEX      = new UnitRegex(ALIASED_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex ALL_KEYWORD_REGEX          = new UnitRegex(ALL_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex AND_KEYWORD_REGEX          = new UnitRegex(AND_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex ARRAY_KEYWORD_REGEX        = new UnitRegex(ARRAY_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex AT_KEYWORD_REGEX           = new UnitRegex(AT_KEYWORD.TOKEN_TEXT          , 1);
	
	private static final LexerRegex BEGIN_KEYWORD_REGEX        = new UnitRegex(BEGIN_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex BODY_KEYWORD_REGEX         = new UnitRegex(BODY_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex CASE_KEYWORD_REGEX         = new UnitRegex(CASE_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex CONSTANT_KEYWORD_REGEX     = new UnitRegex(CONSTANT_KEYWORD.TOKEN_TEXT    , 1);
	
	private static final LexerRegex DECLARE_KEYWORD_REGEX      = new UnitRegex(DECLARE_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex DELAY_KEYWORD_REGEX        = new UnitRegex(DELAY_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex DELTA_KEYWORD_REGEX        = new UnitRegex(DELTA_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex DIGITS_KEYWORD_REGEX       = new UnitRegex(DIGITS_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex DO_KEYWORD_REGEX           = new UnitRegex(DO_KEYWORD.TOKEN_TEXT          , 1);
	
	private static final LexerRegex ELSE_KEYWORD_REGEX         = new UnitRegex(ELSE_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex ELSIF_KEYWORD_REGEX        = new UnitRegex(ELSIF_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex END_KEYWORD_REGEX          = new UnitRegex(END_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex ENTRY_KEYWORD_REGEX        = new UnitRegex(ENTRY_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex EXCEPTION_KEYWORD_REGEX    = new UnitRegex(EXCEPTION_KEYWORD.TOKEN_TEXT   , 1);
	private static final LexerRegex EXIT_KEYWORD_REGEX         = new UnitRegex(EXIT_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex FOR_KEYWORD_REGEX          = new UnitRegex(FOR_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex FUNCTION_KEYWORD_REGEX     = new UnitRegex(FUNCTION_KEYWORD.TOKEN_TEXT    , 1);
	
	private static final LexerRegex GENERIC_KEYWORD_REGEX      = new UnitRegex(GENERIC_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex GOTO_KEYWORD_REGEX         = new UnitRegex(GOTO_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex IF_KEYWORD_REGEX           = new UnitRegex(IF_KEYWORD.TOKEN_TEXT          , 1);
	private static final LexerRegex IN_KEYWORD_REGEX           = new UnitRegex(IN_KEYWORD.TOKEN_TEXT          , 1);
	private static final LexerRegex INTERFACE_KEYWORD_REGEX    = new UnitRegex(INTERFACE_KEYWORD.TOKEN_TEXT   , 1);
	private static final LexerRegex IS_KEYWORD_REGEX           = new UnitRegex(IS_KEYWORD.TOKEN_TEXT          , 1);
	
	private static final LexerRegex LIMITED_KEYWORD_REGEX      = new UnitRegex(LIMITED_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex LOOP_KEYWORD_REGEX         = new UnitRegex(LOOP_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex MOD_KEYWORD_REGEX          = new UnitRegex(MOD_KEYWORD.TOKEN_TEXT         , 1);
	
	private static final LexerRegex NEW_KEYWORD_REGEX          = new UnitRegex(NEW_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex NOT_KEYWORD_REGEX          = new UnitRegex(NOT_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex NULL_KEYWORD_REGEX         = new UnitRegex(NULL_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex OF_KEYWORD_REGEX           = new UnitRegex(OF_KEYWORD.TOKEN_TEXT          , 1);
	private static final LexerRegex OR_KEYWORD_REGEX           = new UnitRegex(OR_KEYWORD.TOKEN_TEXT          , 1);
	private static final LexerRegex OTHERS_KEYWORD_REGEX       = new UnitRegex(OTHERS_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex OUT_KEYWORD_REGEX          = new UnitRegex(OUT_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex OVERRIDING_KEYWORD_REGEX   = new UnitRegex(OVERRIDING_KEYWORD.TOKEN_TEXT  , 1);
	
	private static final LexerRegex PACKAGE_KEYWORD_REGEX      = new UnitRegex(PACKAGE_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex PRAGMA_KEYWORD_REGEX       = new UnitRegex(PRAGMA_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex PRIVATE_KEYWORD_REGEX      = new UnitRegex(PRIVATE_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex PROCEDURE_KEYWORD_REGEX    = new UnitRegex(PROCEDURE_KEYWORD.TOKEN_TEXT   , 1);
	private static final LexerRegex PROTECTED_KEYWORD_REGEX    = new UnitRegex(PROTECTED_KEYWORD.TOKEN_TEXT   , 1);
	
	private static final LexerRegex RAISE_KEYWORD_REGEX        = new UnitRegex(RAISE_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex RANGE_KEYWORD_REGEX        = new UnitRegex(RANGE_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex RECORD_KEYWORD_REGEX       = new UnitRegex(RECORD_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex REM_KEYWORD_REGEX          = new UnitRegex(REM_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex RENAMES_KEYWORD_REGEX      = new UnitRegex(RENAMES_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex REQUEUE_KEYWORD_REGEX      = new UnitRegex(REQUEUE_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex RETURN_KEYWORD_REGEX       = new UnitRegex(RETURN_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex REVERSE_KEYWORD_REGEX      = new UnitRegex(REVERSE_KEYWORD.TOKEN_TEXT     , 1);
	
	private static final LexerRegex SELECT_KEYWORD_REGEX       = new UnitRegex(SELECT_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex SEPARATE_KEYWORD_REGEX     = new UnitRegex(SEPARATE_KEYWORD.TOKEN_TEXT    , 1);
	private static final LexerRegex SOME_KEYWORD_REGEX         = new UnitRegex(SOME_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex SUBTYPE_KEYWORD_REGEX      = new UnitRegex(SUBTYPE_KEYWORD.TOKEN_TEXT     , 1);
	private static final LexerRegex SYNCHRONIZED_KEYWORD_REGEX = new UnitRegex(SYNCHRONIZED_KEYWORD.TOKEN_TEXT, 1);
	
	private static final LexerRegex TAGGED_KEYWORD_REGEX       = new UnitRegex(TAGGED_KEYWORD.TOKEN_TEXT      , 1);
	private static final LexerRegex TASK_KEYWORD_REGEX         = new UnitRegex(TASK_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex TERMINATE_KEYWORD_REGEX    = new UnitRegex(TERMINATE_KEYWORD.TOKEN_TEXT   , 1);
	private static final LexerRegex THEN_KEYWORD_REGEX         = new UnitRegex(THEN_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex TYPE_KEYWORD_REGEX         = new UnitRegex(TYPE_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex UNTIL_KEYWORD_REGEX        = new UnitRegex(UNTIL_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex USE_KEYWORD_REGEX          = new UnitRegex(USE_KEYWORD.TOKEN_TEXT         , 1);
	
	private static final LexerRegex WHEN_KEYWORD_REGEX         = new UnitRegex(WHEN_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex WHILE_KEYWORD_REGEX        = new UnitRegex(WHILE_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex WITH_KEYWORD_REGEX         = new UnitRegex(WITH_KEYWORD.TOKEN_TEXT        , 1);
	
	private static final LexerRegex XOR_KEYWORD_REGEX          = new UnitRegex(XOR_KEYWORD.TOKEN_TEXT         , 1);
	
	// Lexer data
	
	/**
	 * A map associating root regexes with the token types
	 * they represent.
	 */
	private static final Map<LexerRegex, IElementType> REGEX_TOKEN_TYPES;
	
	/**
	 * The set of all root regexes (defined above).
	 */
	private static final Set<LexerRegex> ROOT_REGEXES;
	
	/*
		Static Initializer
	*/
	
	static {
		
		// Populate the regex -> token-type map
		
		REGEX_TOKEN_TYPES = new HashMap<>();
		
		REGEX_TOKEN_TYPES.put(WHITESPACES_REGEX         , WHITESPACES);
		
		REGEX_TOKEN_TYPES.put(AMPERSAND_REGEX           , AMPERSAND);
		REGEX_TOKEN_TYPES.put(APOSTROPHE_REGEX          , APOSTROPHE);
		REGEX_TOKEN_TYPES.put(LEFT_PARENTHESIS_REGEX    , LEFT_PARENTHESIS);
		REGEX_TOKEN_TYPES.put(RIGHT_PARENTHESIS_REGEX   , RIGHT_PARENTHESIS);
		REGEX_TOKEN_TYPES.put(ASTERISK_REGEX            , ASTERISK);
		REGEX_TOKEN_TYPES.put(PLUS_SIGN_REGEX           , PLUS_SIGN);
		REGEX_TOKEN_TYPES.put(COMMA_REGEX               , COMMA);
		REGEX_TOKEN_TYPES.put(HYPHEN_MINUS_REGEX        , HYPHEN_MINUS);
		REGEX_TOKEN_TYPES.put(FULL_STOP_REGEX           , FULL_STOP);
		REGEX_TOKEN_TYPES.put(SOLIDUS_REGEX             , SOLIDUS);
		REGEX_TOKEN_TYPES.put(COLON_REGEX               , COLON);
		REGEX_TOKEN_TYPES.put(SEMICOLON_REGEX           , SEMICOLON);
		REGEX_TOKEN_TYPES.put(LESS_THAN_SIGN_REGEX      , LESS_THAN_SIGN);
		REGEX_TOKEN_TYPES.put(EQUALS_SIGN_REGEX         , EQUALS_SIGN);
		REGEX_TOKEN_TYPES.put(GREATER_THAN_SIGN_REGEX   , GREATER_THAN_SIGN);
		REGEX_TOKEN_TYPES.put(VERTICAL_LINE_REGEX       , VERTICAL_LINE);
		
		REGEX_TOKEN_TYPES.put(ARROW_REGEX               , ARROW);
		REGEX_TOKEN_TYPES.put(DOUBLE_DOT_REGEX          , DOUBLE_DOT);
		REGEX_TOKEN_TYPES.put(DOUBLE_ASTERISK_REGEX     , DOUBLE_ASTERISK);
		REGEX_TOKEN_TYPES.put(ASSIGNMENT_REGEX          , ASSIGNMENT);
		REGEX_TOKEN_TYPES.put(NOT_EQUAL_SIGN_REGEX      , NOT_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(GREATER_EQUAL_SIGN_REGEX  , GREATER_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(LESS_EQUAL_SIGN_REGEX     , LESS_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(LEFT_LABEL_BRACKET_REGEX  , LEFT_LABEL_BRACKET);
		REGEX_TOKEN_TYPES.put(RIGHT_LABEL_BRACKET_REGEX , RIGHT_LABEL_BRACKET);
		REGEX_TOKEN_TYPES.put(BOX_SIGN_REGEX            , BOX_SIGN);
		
		REGEX_TOKEN_TYPES.put(IDENTIFIER_REGEX          , IDENTIFIER);
		REGEX_TOKEN_TYPES.put(DECIMAL_LITERAL_REGEX     , DECIMAL_LITERAL);
		REGEX_TOKEN_TYPES.put(BASED_LITERAL_REGEX       , BASED_LITERAL);
		REGEX_TOKEN_TYPES.put(CHARACTER_LITERAL_REGEX   , CHARACTER_LITERAL);
		REGEX_TOKEN_TYPES.put(STRING_LITERAL_REGEX      , STRING_LITERAL);
		
		REGEX_TOKEN_TYPES.put(COMMENT_REGEX             , COMMENT);
		
		REGEX_TOKEN_TYPES.put(ABORT_KEYWORD_REGEX       , ABORT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ABS_KEYWORD_REGEX         , ABS_KEYWORD);
		REGEX_TOKEN_TYPES.put(ABSTRACT_KEYWORD_REGEX    , ABSTRACT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ACCEPT_KEYWORD_REGEX      , ACCEPT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ACCESS_KEYWORD_REGEX      , ACCESS_KEYWORD);
		REGEX_TOKEN_TYPES.put(ALIASED_KEYWORD_REGEX     , ALIASED_KEYWORD);
		REGEX_TOKEN_TYPES.put(ALL_KEYWORD_REGEX         , ALL_KEYWORD);
		REGEX_TOKEN_TYPES.put(AND_KEYWORD_REGEX         , AND_KEYWORD);
		REGEX_TOKEN_TYPES.put(ARRAY_KEYWORD_REGEX       , ARRAY_KEYWORD);
		REGEX_TOKEN_TYPES.put(AT_KEYWORD_REGEX          , AT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(BEGIN_KEYWORD_REGEX       , BEGIN_KEYWORD);
		REGEX_TOKEN_TYPES.put(BODY_KEYWORD_REGEX        , BODY_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(CASE_KEYWORD_REGEX        , CASE_KEYWORD);
		REGEX_TOKEN_TYPES.put(CONSTANT_KEYWORD_REGEX    , CONSTANT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(DECLARE_KEYWORD_REGEX     , DECLARE_KEYWORD);
		REGEX_TOKEN_TYPES.put(DELAY_KEYWORD_REGEX       , DELAY_KEYWORD);
		REGEX_TOKEN_TYPES.put(DELTA_KEYWORD_REGEX       , DELTA_KEYWORD);
		REGEX_TOKEN_TYPES.put(DIGITS_KEYWORD_REGEX      , DIGITS_KEYWORD);
		REGEX_TOKEN_TYPES.put(DO_KEYWORD_REGEX          , DO_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(ELSE_KEYWORD_REGEX        , ELSE_KEYWORD);
		REGEX_TOKEN_TYPES.put(ELSIF_KEYWORD_REGEX       , ELSIF_KEYWORD);
		REGEX_TOKEN_TYPES.put(END_KEYWORD_REGEX         , END_KEYWORD);
		REGEX_TOKEN_TYPES.put(ENTRY_KEYWORD_REGEX       , ENTRY_KEYWORD);
		REGEX_TOKEN_TYPES.put(EXCEPTION_KEYWORD_REGEX   , EXCEPTION_KEYWORD);
		REGEX_TOKEN_TYPES.put(EXIT_KEYWORD_REGEX        , EXIT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(FOR_KEYWORD_REGEX         , FOR_KEYWORD);
		REGEX_TOKEN_TYPES.put(FUNCTION_KEYWORD_REGEX    , FUNCTION_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(GENERIC_KEYWORD_REGEX     , GENERIC_KEYWORD);
		REGEX_TOKEN_TYPES.put(GOTO_KEYWORD_REGEX        , GOTO_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(IF_KEYWORD_REGEX          , IF_KEYWORD);
		REGEX_TOKEN_TYPES.put(IN_KEYWORD_REGEX          , IN_KEYWORD);
		REGEX_TOKEN_TYPES.put(INTERFACE_KEYWORD_REGEX   , INTERFACE_KEYWORD);
		REGEX_TOKEN_TYPES.put(IS_KEYWORD_REGEX          , IS_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(LIMITED_KEYWORD_REGEX     , LIMITED_KEYWORD);
		REGEX_TOKEN_TYPES.put(LOOP_KEYWORD_REGEX        , LOOP_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(MOD_KEYWORD_REGEX         , MOD_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(NEW_KEYWORD_REGEX         , NEW_KEYWORD);
		REGEX_TOKEN_TYPES.put(NOT_KEYWORD_REGEX         , NOT_KEYWORD);
		REGEX_TOKEN_TYPES.put(NULL_KEYWORD_REGEX        , NULL_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(OF_KEYWORD_REGEX          , OF_KEYWORD);
		REGEX_TOKEN_TYPES.put(OR_KEYWORD_REGEX          , OR_KEYWORD);
		REGEX_TOKEN_TYPES.put(OTHERS_KEYWORD_REGEX      , OTHERS_KEYWORD);
		REGEX_TOKEN_TYPES.put(OUT_KEYWORD_REGEX         , OUT_KEYWORD);
		REGEX_TOKEN_TYPES.put(OVERRIDING_KEYWORD_REGEX  , OVERRIDING_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(PACKAGE_KEYWORD_REGEX     , PACKAGE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PRAGMA_KEYWORD_REGEX      , PRAGMA_KEYWORD);
		REGEX_TOKEN_TYPES.put(PRIVATE_KEYWORD_REGEX     , PRIVATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PROCEDURE_KEYWORD_REGEX   , PROCEDURE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PROTECTED_KEYWORD_REGEX   , PROTECTED_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(RAISE_KEYWORD_REGEX       , RAISE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RANGE_KEYWORD_REGEX       , RANGE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RECORD_KEYWORD_REGEX      , RECORD_KEYWORD);
		REGEX_TOKEN_TYPES.put(REM_KEYWORD_REGEX         , REM_KEYWORD);
		REGEX_TOKEN_TYPES.put(RENAMES_KEYWORD_REGEX     , RENAMES_KEYWORD);
		REGEX_TOKEN_TYPES.put(REQUEUE_KEYWORD_REGEX     , REQUEUE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RETURN_KEYWORD_REGEX      , RETURN_KEYWORD);
		REGEX_TOKEN_TYPES.put(REVERSE_KEYWORD_REGEX     , REVERSE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(SELECT_KEYWORD_REGEX      , SELECT_KEYWORD);
		REGEX_TOKEN_TYPES.put(SEPARATE_KEYWORD_REGEX    , SEPARATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(SOME_KEYWORD_REGEX        , SOME_KEYWORD);
		REGEX_TOKEN_TYPES.put(SUBTYPE_KEYWORD_REGEX     , SUBTYPE_KEYWORD);
		REGEX_TOKEN_TYPES.put(SYNCHRONIZED_KEYWORD_REGEX, SYNCHRONIZED_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(TAGGED_KEYWORD_REGEX      , TAGGED_KEYWORD);
		REGEX_TOKEN_TYPES.put(TASK_KEYWORD_REGEX        , TASK_KEYWORD);
		REGEX_TOKEN_TYPES.put(TERMINATE_KEYWORD_REGEX   , TERMINATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(THEN_KEYWORD_REGEX        , THEN_KEYWORD);
		REGEX_TOKEN_TYPES.put(TYPE_KEYWORD_REGEX        , TYPE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(UNTIL_KEYWORD_REGEX       , UNTIL_KEYWORD);
		REGEX_TOKEN_TYPES.put(USE_KEYWORD_REGEX         , USE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(WHEN_KEYWORD_REGEX        , WHEN_KEYWORD);
		REGEX_TOKEN_TYPES.put(WHILE_KEYWORD_REGEX       , WHILE_KEYWORD);
		REGEX_TOKEN_TYPES.put(WITH_KEYWORD_REGEX        , WITH_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(XOR_KEYWORD_REGEX         , XOR_KEYWORD);
		
		// Set the root regexes set from the regex -> token-type map's keyset
		
		ROOT_REGEXES = Collections.unmodifiableSet(REGEX_TOKEN_TYPES.keySet());
		
	}
	
	/*
		Fields
	*/
	
	/**
	 * The lowercase version of the text to be analysed.
	 */
	private CharSequence text;
	
	/**
	 * The end of the lexing range.
	 */
	private int lexingEndOffset;
	
	/**
	 * The current position of the Lexer in the text.
	 */
	private int lexingOffset;
	
	/**
	 * The current state of the Lexer.
	 */
	private int state;
	
	/**
	 * The type of the last analysed token.
	 */
	private IElementType tokenType;
	
	/**
	 * The start offset of the last analysed token.
	 */
	private int tokenStart;
	
	/**
	 * The end offset of the last analysed token.
	 */
	private int tokenEnd;
	
	/*
		Constructor
	*/
	
	/**
	 * Constructs a new Ada Lexer.
	 */
	public AdaLexer() {}
	
	/*
		Methods
	*/
	
	/**
	 * @see com.intellij.lexer.Lexer#start(CharSequence, int, int, int)
	 */
	@Override
	public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
		
		// Check lexing bounds
		
		if (startOffset < 0) {
			
			throw new IndexOutOfBoundsException("Illegal negative lexing start offset: " + startOffset);
			
		} else if (endOffset > buffer.length()) {
			
			throw new IndexOutOfBoundsException("Illegal lexing end offset greater than total text length: " + endOffset);
			
		} else if (startOffset > endOffset) {
			
			throw new IndexOutOfBoundsException("Illegal lexing bounds: " + startOffset + " to " + endOffset);
			
		}
		
		// Initialize lexer fields
		
		text            = buffer.toString().toLowerCase(Locale.ROOT);
		
		lexingEndOffset = endOffset;
		lexingOffset    = startOffset;
		state           = initialState;
		
		tokenType       = null;
		tokenStart      = startOffset;
		tokenEnd        = startOffset;
		
		// Analyse the first token
		// Note: It seems that IntelliJ expects the first call to getTokenType()
		//       to return the type of the first token, but it does not call
		//       advance() itself for some reason...
		
		advance();
		
	}
	
	/**
	 * @see com.intellij.lexer.Lexer#getState()
	 */
	@Override
	public int getState() { return state; }
	
	/**
	 * @see com.intellij.lexer.Lexer#getTokenType()
	 */
	@Override
	@Nullable
	public IElementType getTokenType() {
		return tokenType;
	}
	
	/**
	 * @see com.intellij.lexer.Lexer#getTokenStart()
	 */
	@Override
	public int getTokenStart() { return tokenStart; }
	
	/**
	 * @see com.intellij.lexer.Lexer#getTokenEnd()
	 */
	@Override
	public int getTokenEnd() { return tokenEnd; }
	
	/**
	 * @see com.intellij.lexer.Lexer#advance()
	 */
	@Override
	public void advance() {
		
		// If the end of the text is reached, set the token type to null
		// and return
		// Note: It is important to set the token type to null as IntelliJ
		//       seems to rely on this to conclude its token fetching
		//       procedure properly
		
		if (lexingOffset == lexingEndOffset) {
			tokenType = null;
			return;
		}
		
		// Set the start of the next token to the end of the previous one
		
		tokenStart = tokenEnd;
		
		// The offset by which the lexer needs to be rolled back before
		// marking the end of the matched token. This happens for example
		// when lexing the sequence "'Access" where:
		// 1. After the "'" character, only the following regexes advance:
		//    * APOSTROPHE_REGEX and it is nullable at this point
		//    * CHARACTER_LITERAL_REGEX and it is not nullable at this point
		// 2. After the "A" character, only the following regex advances:
		//    * CHARACTER_LITERAL_REGEX and it is not nullable at this point
		// 3. After the first "c" character, no regexes advance
		// At this point, the matching regex is the nullable APOSTROPHE_REGEX
		// obtained at step 1, so the lexer needs to "mark" the sequence "'"
		// as the apostrophe token and roll back to the "A" character in
		// order to start from there during the next call to `advance`
		int rollBackOffset = 0;
		
		// The set of regexes that successfully advanced so far
		Set<LexerRegex> regexes = new HashSet<>(ROOT_REGEXES);
		
		// The last set of regexes, resulting from an iteration of
		// characterLoop, that contained at least one nullable regex
		// (see rollBackOffset description for an example)
		Set<LexerRegex> matchingRegexes = new HashSet<>();
		
		// A map specifying the root regex from which every regex originates
		// by a series of calls to regex.advanced(char)
		final Map<LexerRegex, LexerRegex> regexLineages = new HashMap<>();
		
		// The next character to be analysed
		char nextCharacter = text.charAt(lexingOffset);
		
		// If the next character is an apostrophe and the last token
		// was an identifier, then immediately mark this token as an
		// apostrophe token and return
		
		if (nextCharacter == '\'' && tokenType == IDENTIFIER) {
			
			lexingOffset = tokenEnd = tokenStart + 1;
			
			tokenType = APOSTROPHE;
			
			return;
		
		}
		
		// While the next token has not been determined...
		
		characterLoop: // label only used for reference in comments
		while (tokenEnd == tokenStart) {
			
			final char character = nextCharacter;
			
			// The set of regexes that will have advanced successfully
			// at the end of this iteration of characterLoop
			final Set<LexerRegex> advancedRegexes = new HashSet<>();
			
			// For each regex that successfully advanced by all
			// characters so far...
			
			regexes.forEach(regex -> {
				
				// Try to advance the regex
				
				LexerRegex advancedRegex = regex.advanced(character);
				
				// If the regex advanced successfully, store it for the next
				// iteration of characterLoop, and keep track of the root
				// regex that is the ancestor of the advanced regex
				
				if (advancedRegex != null) {
					
					advancedRegexes.add(advancedRegex);
					
					LexerRegex ancestor = regexLineages.get(regex);
					
					if (advancedRegex.nullable() || !regex.nullable()) {
						regexLineages.remove(regex);
					}
					
					regexLineages.put(advancedRegex, ancestor == null ? regex : ancestor);
					
				}
				
			});
			
			// Set the regex set to be the advanced regex set
			
			regexes = advancedRegexes;
			
			int remainingRegexCount = regexes.size();
			
			// If no remaining matching regexes exist, or the last character
			// of the text is reached...
			
			if (remainingRegexCount == 0 || lexingOffset == lexingEndOffset - 1) {
				
				Iterator<LexerRegex> matchingRegexIterator;
				
				// If no remaining matching regexes exist, then choose a regex
				// from those that last matched and had at least one nullable
				// regex (or the empty set if either that was never the case, or
				// this is the first iteration of characterLoop)
				
				if (remainingRegexCount == 0) {
					matchingRegexIterator = matchingRegexes.iterator();
				}
				
				// If there are still matching regexes but the last character
				// was reached, then choose a regex from those that matched
				// during this iteration of characterLoop
				
				else {
					matchingRegexIterator = regexes.iterator();
					lexingOffset = lexingEndOffset;
				}
				
				LexerRegex highestPriorityRegex = null;
				
				// Find the matching regex with the highest priority
				// The chosen regex still has to be nullable, which prevents for
				// example the word "proc" at the end of an Ada file from being
				// assigned the token of the procedure keyword, as its regex for
				// that has a higher priority than the identifier regex, and it
				// does match the sequence "proc" (but it should not be chosen
				// as its advanced regex at that point is not nullable, in other
				// words it still requires the sequence "edure" to "fully match")
				
				while (matchingRegexIterator.hasNext()) {
					
					LexerRegex regex = matchingRegexIterator.next();
					
					if (
						regex.nullable() &&
						(
							highestPriorityRegex == null ||
							regex.getPriority() > highestPriorityRegex.getPriority()
						)
					) {
						highestPriorityRegex = regex;
					}
					
				}
				
				// If a non nullable regex (with highest priority) was found,
				// then get the root regex from which this regex originates
				// then get the token type corresponding to that root regex
				// then set the lexer token type to that type
				
				if (highestPriorityRegex != null) {
					
					LexerRegex rootRegex =
						regexLineages.getOrDefault(highestPriorityRegex, highestPriorityRegex);
					
					tokenType =
						REGEX_TOKEN_TYPES.get(rootRegex);
					
				}
				
				// Otherwise, set the token type to BAD_CHARACTER
				
				else {
					
					tokenType = BAD_CHARACTER;
					
					// If this is a single-character, then the lexing offset
					// needs to be advanced manually to avoid infinite calls
					// to `advance`
					
					if (lexingOffset == tokenStart) { lexingOffset++; }
					
					// Reset the rollback offset
					
					rollBackOffset = 0;
					
				}
				
				// Roll the lexer back by the necessary offset
				
				lexingOffset -= rollBackOffset;
				
				// Set the token end offset to the lexing offset
				// This will also break the execution of characterLoop
				
				tokenEnd = lexingOffset;
				
			}
			
			// Otherwise, there are still matching regexes and the last character
			// in the text was not yet reached, so get the next character and
			// execute the next iteration of characterLoop
			
			else {
				
				// If at least one of the regexes that advanced successfully in
				// this iteration of characterLoop is nullable, then store that
				// set of regexes to be potentially used in the next iteration
				// to find matching regexes, and reset the rollback offset
				
				if (regexes.stream().anyMatch(LexerRegex::nullable)) {
					
					matchingRegexes = new HashSet<>(regexes);
					
					rollBackOffset = 0;
					
				}
				
				// Otherwise, do not overwrite the last set of regexes with at
				// least one nullable regex and increase the rollback offset
				
				else {
					rollBackOffset++;
				}
				
				// Advance the lexer to the next character
				
				nextCharacter = text.charAt(++lexingOffset);
				
			}
			
		}
		
	}
	
	/**
	 * @see com.intellij.lexer.Lexer#getBufferSequence()
	 */
	@NotNull
	@Override
	public CharSequence getBufferSequence() { return text; }
	
	/**
	 * @see com.intellij.lexer.Lexer#getBufferEnd()
	 */
	@Override
	public int getBufferEnd() { return lexingEndOffset; }
	
	/*
		Convenience Classes and Methods
	*/
	
	/**
	 * Simple data class representing a token.
	 */
	public static class Token {
		
		/**
		 * The token's properties.
		 */
		public final IElementType TOKEN_TYPE;
		public final int          START_OFFSET;
		public final int          END_OFFSET;
		
		/**
		 * Constructs a new token given a token type, start offset and
		 * end offset.
		 *
		 * @param tokenType The token's type.
		 * @param startOffset The token's start offset.
		 * @param endOffset The token's end offset.
		 */
		public Token(IElementType tokenType, int startOffset, int endOffset) {
			TOKEN_TYPE   = tokenType;
			START_OFFSET = startOffset;
			END_OFFSET   = endOffset;
		}
		
		/**
		 * Returns whether or not this token is equal to the given object.
		 *
		 * @param object The object to compare to this token.
		 * @return The result of the comparison.
		 */
		@Override
		public boolean equals(Object object) {
			
			if (!(object instanceof Token)) { return false; }
			else {
				
				Token token = (Token)object;
				
				return token.TOKEN_TYPE.toString().equals(TOKEN_TYPE.toString()) &&
					token.START_OFFSET == START_OFFSET && token.END_OFFSET == END_OFFSET;
				
			}
			
		}
		
		/**
		 * Returns a string representation of this token.
		 *
		 * @return A string representation of this token.
		 */
		@Override
		public String toString() {
			return "Token(" + TOKEN_TYPE + ", " + START_OFFSET + ", " + END_OFFSET + ")";
		}
		
	}
	
	/**
	 * Performs lexical analysis over the given text by running a single
	 * iteration of `advance` and returning the first token encountered.
	 *
	 * @param text The text over which to perform analysis.
	 * @return The first token in the given text.
	 */
	@Nullable
	public static Token firstToken(CharSequence text) {
		
		AdaLexer lexer = new AdaLexer();
		
		lexer.start(text, 0, text.length(), 0);
		
		return new Token(lexer.getTokenType(), lexer.getTokenStart(), lexer.getTokenEnd());
		
	}
	
	/**
	 * Returns a token iterator that can be used to perform lazy lexical
	 * analysis over the entire given text.
	 *
	 * @param text The text over which to perform analysis.
	 * @return A lazy iterator over the tokens in the given text.
	 */
	public static Iterator<Token> textTokens(CharSequence text) {
		
		final AdaLexer lexer = new AdaLexer();
		
		lexer.start(text, 0, text.length(), 0);
		
		return new Iterator<Token>() {
			
			/**
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() { return lexer.getTokenType() != null; }
			
			/**
			 * @see java.util.Iterator#next()
			 */
			@Override
			public Token next() {
			
				IElementType tokenType   = lexer.getTokenType();
				int          startOffset = lexer.getTokenStart();
				int          endOffset   = lexer.getTokenEnd();
				
				lexer.advance();
				
				return new Token(tokenType, startOffset, endOffset);
			
			}
			
		};
		
	}
	
}
