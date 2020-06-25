package com.adacore.adaintellij.analysis.lexical;

import java.util.*;
import java.util.stream.Collectors;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.lexical.regex.*;

import static com.adacore.adaintellij.analysis.lexical.AdaTokenTypes.*;

/**
 * Lexical analyser for Ada 2012 (ISO/IEC 8652:2012(E)).
 */
public final class AdaLexer extends Lexer {

	/*
		Constants
	*/

	// Delimiters

	/**
	 * Unit regexes for matching Ada single delimiters.
	 */
	private static final LexerRegex AMPERSAND_REGEX         = new UnitRegex(AMPERSAND.TOKEN_TEXT);
	private static final LexerRegex APOSTROPHE_REGEX        = new UnitRegex(APOSTROPHE.TOKEN_TEXT);
	private static final LexerRegex LEFT_PARENTHESIS_REGEX  = new UnitRegex(LEFT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex RIGHT_PARENTHESIS_REGEX = new UnitRegex(RIGHT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex SPEECH_MARK_REGEX 		= new UnitRegex(SPEECH_MARK.TOKEN_TEXT);
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
				HYPHEN_MINUS_REGEX,
				new ZeroOrOneRegex(PLUS_SIGN_REGEX)
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
					FULL_STOP_REGEX,
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
					FULL_STOP_REGEX,
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
			APOSTROPHE_REGEX,
			GRAPHIC_CHARACTER_REGEX,
			APOSTROPHE_REGEX
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
	 * The set of all root regexes except keyword regexes.
	 */
	private static final Set<LexerRegex> NON_KEYWORD_ROOT_REGEXES;

	/*
		Static Initializer
	*/

	static {

		// Populate the regex -> token-type map

		Map<LexerRegex, IElementType> regexTokenTypes = new HashMap<>();

		regexTokenTypes.put(WHITESPACES_REGEX         , WHITESPACES);

		regexTokenTypes.put(AMPERSAND_REGEX           , AMPERSAND);
		regexTokenTypes.put(APOSTROPHE_REGEX          , APOSTROPHE);
		regexTokenTypes.put(SPEECH_MARK_REGEX         , SPEECH_MARK);
		regexTokenTypes.put(LEFT_PARENTHESIS_REGEX    , LEFT_PARENTHESIS);
		regexTokenTypes.put(RIGHT_PARENTHESIS_REGEX   , RIGHT_PARENTHESIS);
		regexTokenTypes.put(ASTERISK_REGEX            , ASTERISK);
		regexTokenTypes.put(PLUS_SIGN_REGEX           , PLUS_SIGN);
		regexTokenTypes.put(COMMA_REGEX               , COMMA);
		regexTokenTypes.put(HYPHEN_MINUS_REGEX        , HYPHEN_MINUS);
		regexTokenTypes.put(FULL_STOP_REGEX           , FULL_STOP);
		regexTokenTypes.put(SOLIDUS_REGEX             , SOLIDUS);
		regexTokenTypes.put(COLON_REGEX               , COLON);
		regexTokenTypes.put(SEMICOLON_REGEX           , SEMICOLON);
		regexTokenTypes.put(LESS_THAN_SIGN_REGEX      , LESS_THAN_SIGN);
		regexTokenTypes.put(EQUALS_SIGN_REGEX         , EQUALS_SIGN);
		regexTokenTypes.put(GREATER_THAN_SIGN_REGEX   , GREATER_THAN_SIGN);
		regexTokenTypes.put(VERTICAL_LINE_REGEX       , VERTICAL_LINE);

		regexTokenTypes.put(ARROW_REGEX               , ARROW);
		regexTokenTypes.put(DOUBLE_DOT_REGEX          , DOUBLE_DOT);
		regexTokenTypes.put(DOUBLE_ASTERISK_REGEX     , DOUBLE_ASTERISK);
		regexTokenTypes.put(ASSIGNMENT_REGEX          , ASSIGNMENT);
		regexTokenTypes.put(NOT_EQUAL_SIGN_REGEX      , NOT_EQUAL_SIGN);
		regexTokenTypes.put(GREATER_EQUAL_SIGN_REGEX  , GREATER_EQUAL_SIGN);
		regexTokenTypes.put(LESS_EQUAL_SIGN_REGEX     , LESS_EQUAL_SIGN);
		regexTokenTypes.put(LEFT_LABEL_BRACKET_REGEX  , LEFT_LABEL_BRACKET);
		regexTokenTypes.put(RIGHT_LABEL_BRACKET_REGEX , RIGHT_LABEL_BRACKET);
		regexTokenTypes.put(BOX_SIGN_REGEX            , BOX_SIGN);

		regexTokenTypes.put(IDENTIFIER_REGEX          , IDENTIFIER);
		regexTokenTypes.put(DECIMAL_LITERAL_REGEX     , DECIMAL_LITERAL);
		regexTokenTypes.put(BASED_LITERAL_REGEX       , BASED_LITERAL);
		regexTokenTypes.put(CHARACTER_LITERAL_REGEX   , CHARACTER_LITERAL);
		regexTokenTypes.put(STRING_LITERAL_REGEX      , STRING_LITERAL);

		regexTokenTypes.put(COMMENT_REGEX             , COMMENT);

		regexTokenTypes.put(ABORT_KEYWORD_REGEX       , ABORT_KEYWORD);
		regexTokenTypes.put(ABS_KEYWORD_REGEX         , ABS_KEYWORD);
		regexTokenTypes.put(ABSTRACT_KEYWORD_REGEX    , ABSTRACT_KEYWORD);
		regexTokenTypes.put(ACCEPT_KEYWORD_REGEX      , ACCEPT_KEYWORD);
		regexTokenTypes.put(ACCESS_KEYWORD_REGEX      , ACCESS_KEYWORD);
		regexTokenTypes.put(ALIASED_KEYWORD_REGEX     , ALIASED_KEYWORD);
		regexTokenTypes.put(ALL_KEYWORD_REGEX         , ALL_KEYWORD);
		regexTokenTypes.put(AND_KEYWORD_REGEX         , AND_KEYWORD);
		regexTokenTypes.put(ARRAY_KEYWORD_REGEX       , ARRAY_KEYWORD);
		regexTokenTypes.put(AT_KEYWORD_REGEX          , AT_KEYWORD);

		regexTokenTypes.put(BEGIN_KEYWORD_REGEX       , BEGIN_KEYWORD);
		regexTokenTypes.put(BODY_KEYWORD_REGEX        , BODY_KEYWORD);

		regexTokenTypes.put(CASE_KEYWORD_REGEX        , CASE_KEYWORD);
		regexTokenTypes.put(CONSTANT_KEYWORD_REGEX    , CONSTANT_KEYWORD);

		regexTokenTypes.put(DECLARE_KEYWORD_REGEX     , DECLARE_KEYWORD);
		regexTokenTypes.put(DELAY_KEYWORD_REGEX       , DELAY_KEYWORD);
		regexTokenTypes.put(DELTA_KEYWORD_REGEX       , DELTA_KEYWORD);
		regexTokenTypes.put(DIGITS_KEYWORD_REGEX      , DIGITS_KEYWORD);
		regexTokenTypes.put(DO_KEYWORD_REGEX          , DO_KEYWORD);

		regexTokenTypes.put(ELSE_KEYWORD_REGEX        , ELSE_KEYWORD);
		regexTokenTypes.put(ELSIF_KEYWORD_REGEX       , ELSIF_KEYWORD);
		regexTokenTypes.put(END_KEYWORD_REGEX         , END_KEYWORD);
		regexTokenTypes.put(ENTRY_KEYWORD_REGEX       , ENTRY_KEYWORD);
		regexTokenTypes.put(EXCEPTION_KEYWORD_REGEX   , EXCEPTION_KEYWORD);
		regexTokenTypes.put(EXIT_KEYWORD_REGEX        , EXIT_KEYWORD);

		regexTokenTypes.put(FOR_KEYWORD_REGEX         , FOR_KEYWORD);
		regexTokenTypes.put(FUNCTION_KEYWORD_REGEX    , FUNCTION_KEYWORD);

		regexTokenTypes.put(GENERIC_KEYWORD_REGEX     , GENERIC_KEYWORD);
		regexTokenTypes.put(GOTO_KEYWORD_REGEX        , GOTO_KEYWORD);

		regexTokenTypes.put(IF_KEYWORD_REGEX          , IF_KEYWORD);
		regexTokenTypes.put(IN_KEYWORD_REGEX          , IN_KEYWORD);
		regexTokenTypes.put(INTERFACE_KEYWORD_REGEX   , INTERFACE_KEYWORD);
		regexTokenTypes.put(IS_KEYWORD_REGEX          , IS_KEYWORD);

		regexTokenTypes.put(LIMITED_KEYWORD_REGEX     , LIMITED_KEYWORD);
		regexTokenTypes.put(LOOP_KEYWORD_REGEX        , LOOP_KEYWORD);

		regexTokenTypes.put(MOD_KEYWORD_REGEX         , MOD_KEYWORD);

		regexTokenTypes.put(NEW_KEYWORD_REGEX         , NEW_KEYWORD);
		regexTokenTypes.put(NOT_KEYWORD_REGEX         , NOT_KEYWORD);
		regexTokenTypes.put(NULL_KEYWORD_REGEX        , NULL_KEYWORD);

		regexTokenTypes.put(OF_KEYWORD_REGEX          , OF_KEYWORD);
		regexTokenTypes.put(OR_KEYWORD_REGEX          , OR_KEYWORD);
		regexTokenTypes.put(OTHERS_KEYWORD_REGEX      , OTHERS_KEYWORD);
		regexTokenTypes.put(OUT_KEYWORD_REGEX         , OUT_KEYWORD);
		regexTokenTypes.put(OVERRIDING_KEYWORD_REGEX  , OVERRIDING_KEYWORD);

		regexTokenTypes.put(PACKAGE_KEYWORD_REGEX     , PACKAGE_KEYWORD);
		regexTokenTypes.put(PRAGMA_KEYWORD_REGEX      , PRAGMA_KEYWORD);
		regexTokenTypes.put(PRIVATE_KEYWORD_REGEX     , PRIVATE_KEYWORD);
		regexTokenTypes.put(PROCEDURE_KEYWORD_REGEX   , PROCEDURE_KEYWORD);
		regexTokenTypes.put(PROTECTED_KEYWORD_REGEX   , PROTECTED_KEYWORD);

		regexTokenTypes.put(RAISE_KEYWORD_REGEX       , RAISE_KEYWORD);
		regexTokenTypes.put(RANGE_KEYWORD_REGEX       , RANGE_KEYWORD);
		regexTokenTypes.put(RECORD_KEYWORD_REGEX      , RECORD_KEYWORD);
		regexTokenTypes.put(REM_KEYWORD_REGEX         , REM_KEYWORD);
		regexTokenTypes.put(RENAMES_KEYWORD_REGEX     , RENAMES_KEYWORD);
		regexTokenTypes.put(REQUEUE_KEYWORD_REGEX     , REQUEUE_KEYWORD);
		regexTokenTypes.put(RETURN_KEYWORD_REGEX      , RETURN_KEYWORD);
		regexTokenTypes.put(REVERSE_KEYWORD_REGEX     , REVERSE_KEYWORD);

		regexTokenTypes.put(SELECT_KEYWORD_REGEX      , SELECT_KEYWORD);
		regexTokenTypes.put(SEPARATE_KEYWORD_REGEX    , SEPARATE_KEYWORD);
		regexTokenTypes.put(SOME_KEYWORD_REGEX        , SOME_KEYWORD);
		regexTokenTypes.put(SUBTYPE_KEYWORD_REGEX     , SUBTYPE_KEYWORD);
		regexTokenTypes.put(SYNCHRONIZED_KEYWORD_REGEX, SYNCHRONIZED_KEYWORD);

		regexTokenTypes.put(TAGGED_KEYWORD_REGEX      , TAGGED_KEYWORD);
		regexTokenTypes.put(TASK_KEYWORD_REGEX        , TASK_KEYWORD);
		regexTokenTypes.put(TERMINATE_KEYWORD_REGEX   , TERMINATE_KEYWORD);
		regexTokenTypes.put(THEN_KEYWORD_REGEX        , THEN_KEYWORD);
		regexTokenTypes.put(TYPE_KEYWORD_REGEX        , TYPE_KEYWORD);

		regexTokenTypes.put(UNTIL_KEYWORD_REGEX       , UNTIL_KEYWORD);
		regexTokenTypes.put(USE_KEYWORD_REGEX         , USE_KEYWORD);

		regexTokenTypes.put(WHEN_KEYWORD_REGEX        , WHEN_KEYWORD);
		regexTokenTypes.put(WHILE_KEYWORD_REGEX       , WHILE_KEYWORD);
		regexTokenTypes.put(WITH_KEYWORD_REGEX        , WITH_KEYWORD);

		regexTokenTypes.put(XOR_KEYWORD_REGEX         , XOR_KEYWORD);

		REGEX_TOKEN_TYPES = Collections.unmodifiableMap(regexTokenTypes);

		// Populate the non-keyword root regex set

		NON_KEYWORD_ROOT_REGEXES = REGEX_TOKEN_TYPES.keySet()
			.stream()
			.filter(regex -> !KEYWORD_TOKEN_SET.contains(REGEX_TOKEN_TYPES.get(regex)))
			.collect(Collectors.toSet());

	}

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.Lexer#badCharacterTokenType()
	 */
	@NotNull
	@Override
	protected IElementType badCharacterTokenType() { return BAD_CHARACTER; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.Lexer#regexTokenTypeMap()
	 */
	@NotNull
	@Override
	protected Map<LexerRegex, IElementType> regexTokenTypeMap() { return REGEX_TOKEN_TYPES; }

	/**
	 * @see com.adacore.adaintellij.analysis.lexical.Lexer#getLexingStartingRegexes()
	 */
	@NotNull
	@Override
	protected Set<LexerRegex> getLexingStartingRegexes() {
		return getTokenType() == APOSTROPHE ?
			NON_KEYWORD_ROOT_REGEXES : super.getLexingStartingRegexes();
	}

	/**
	 * @see com.intellij.lexer.Lexer#advance()
	 */
	@Override
	public void advance() {

		// If the end of the text is reached, then call the
		// original `advance` method to handle it properly

		if (reachedEndOfText()) {
			super.advance();
			return;
		}

		// The next character to be analysed
		Character nextCharacter = nextCharacter();

		// If the next character is an apostrophe and the last token
		// was an identifier, then immediately mark this token as an
		// apostrophe token and return

		if (nextCharacter != null && nextCharacter == '\'' && getTokenType() == IDENTIFIER) {

			tokenStart = tokenEnd;

			lexingOffset = tokenEnd = tokenStart + 1;

			tokenType = APOSTROPHE;

			return;

		}

		super.advance();

	}

}
