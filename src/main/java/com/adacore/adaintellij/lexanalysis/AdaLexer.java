package com.adacore.adaintellij.lexanalysis;

import java.util.*;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.lexanalysis.regex.*;

/**
 * Lexical Analyser for Ada 2012 (ISO/IEC 8652:2012(E)).
 *
 * TODO: Consider normalizing text?!
 */
public class AdaLexer extends LexerBase {
	
	// Constants
	
	/**
	 * Regex defining a sequence of whitespaces in Ada.
	 *
	 * TODO: Figure out how exactly to define the whitespaces regex
	 *       (the spec is not clear/explicit enough about whitespaces)
	 */
	private static final OORegex WHITE_SPACES_REGEX =
		new OneOrMoreRegex(
			UnionRegex.fromRegexes(
				new UnitRegex("\n"),
				new UnitRegex("\r"),
				new UnitRegex("\t"),
				new UnitRegex(" ")
			)
		);
	
	/**
	 * Unit regexes for matching Ada single delimiters.
	 */
	private static final OORegex AMPERSAND_REGEX         = new UnitRegex("&");
	private static final OORegex APOSTROPHE_REGEX        = new UnitRegex("'");
	private static final OORegex LEFT_PARENTHESIS_REGEX  = new UnitRegex("(");
	private static final OORegex RIGHT_PARENTHESIS_REGEX = new UnitRegex(")");
	private static final OORegex ASTERISK_REGEX          = new UnitRegex("*");
	private static final OORegex PLUS_SIGN_REGEX         = new UnitRegex("+");
	private static final OORegex COMMA_REGEX             = new UnitRegex(",");
	private static final OORegex HYPHEN_MINUS_REGEX      = new UnitRegex("-");
	private static final OORegex FULL_STOP_REGEX         = new UnitRegex(".");
	private static final OORegex SOLIDUS_REGEX           = new UnitRegex("/");
	private static final OORegex COLON_REGEX             = new UnitRegex(":");
	private static final OORegex SEMICOLON_REGEX         = new UnitRegex(";");
	private static final OORegex LESS_THAN_SIGN_REGEX    = new UnitRegex("<");
	private static final OORegex EQUALS_SIGN_REGEX       = new UnitRegex("=");
	private static final OORegex GREATER_THAN_SIGN_REGEX = new UnitRegex(">");
	private static final OORegex VERTICAL_LINE_REGEX     = new UnitRegex("|");
	
	/**
	 * Unit regexes for matching Ada compound delimiters.
	 */
	private static final OORegex ARROW_REGEX               = new UnitRegex("=>");
	private static final OORegex DOUBLE_DOT_REGEX          = new UnitRegex("..");
	private static final OORegex DOUBLE_ASTERISK_REGEX     = new UnitRegex("**");
	private static final OORegex ASSIGNMENT_REGEX          = new UnitRegex(":=");
	private static final OORegex NOT_EQUAL_SIGN_REGEX      = new UnitRegex("/=");
	private static final OORegex GREATER_EQUAL_SIGN_REGEX  = new UnitRegex(">=");
	private static final OORegex LESS_EQUAL_SIGN_REGEX     = new UnitRegex("<=");
	private static final OORegex LEFT_LABEL_BRACKET_REGEX  = new UnitRegex("<<");
	private static final OORegex RIGHT_LABEL_BRACKET_REGEX = new UnitRegex(">>");
	private static final OORegex BOX_SIGN_REGEX            = new UnitRegex("<>");
	
	// Character Categories
	
	/**
	 * Regexes matching characters based on their "General Category"
	 * as defined by the Unicode standard.
	 */
	private static final OORegex LETTER_UPPERCASE_REGEX       = new GeneralCategoryRegex("Lu");
	private static final OORegex LETTER_LOWERCASE_REGEX       = new GeneralCategoryRegex("Ll");
	private static final OORegex LETTER_TITLECASE_REGEX       = new GeneralCategoryRegex("Lt");
	private static final OORegex LETTER_MODIFIER_REGEX        = new GeneralCategoryRegex("Lm");
	private static final OORegex LETTER_OTHER_REGEX           = new GeneralCategoryRegex("Lo");
	private static final OORegex MARK_NON_SPACING_REGEX       = new GeneralCategoryRegex("Mn");
	private static final OORegex MARK_SPACING_COMBINING_REGEX = new GeneralCategoryRegex("Mc");
	private static final OORegex NUMBER_DECIMAL_REGEX         = new GeneralCategoryRegex("Nd");
	private static final OORegex NUMBER_LETTER_REGEX          = new GeneralCategoryRegex("Nl");
	private static final OORegex PUNCTUATION_CONNECTOR_REGEX  = new GeneralCategoryRegex("Pc");
	private static final OORegex OTHER_FORMAT_REGEX           = new GeneralCategoryRegex("Cf"); // Currently not used
	private static final OORegex SEPARATOR_SPACE_REGEX        = new GeneralCategoryRegex("Zs"); // Currently not used
	private static final OORegex SEPARATOR_LINE_REGEX         = new GeneralCategoryRegex("Zl");
	private static final OORegex SEPARATOR_PARAGRAPH_REGEX    = new GeneralCategoryRegex("Zp");
	private static final OORegex OTHER_PRIVATE_USE_REGEX      = new GeneralCategoryRegex("Co");
	private static final OORegex OTHER_SURROGATE_REGEX        = new GeneralCategoryRegex("Cs");
	
	/**
	 * Regexes matching various character classes defined by
	 * the Ada 2012 specification.
	 */
	private static final OORegex FORMAT_EFFECTOR_REGEX =
		UnionRegex.fromRegexes(
			new UnitRegex("\u0009"),
			new UnitRegex("\n"),
			new UnitRegex("\u000b"),
			new UnitRegex("\u000c"),
			new UnitRegex("\r"),
			new UnitRegex("\u0085"),
			SEPARATOR_LINE_REGEX,
			SEPARATOR_PARAGRAPH_REGEX
		);
	
	private static final OORegex OTHER_CONTROL_REGEX =
		new IntersectionRegex(
			new GeneralCategoryRegex("Cc"),
			new NotRegex(FORMAT_EFFECTOR_REGEX)
		);
	
	private static final OORegex GRAPHIC_CHARACTER_REGEX =
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
	private static final OORegex IDENTIFIER_START_REGEX =
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
	private static final OORegex IDENTIFIER_EXTEND_REGEX =
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
	private static final OORegex IDENTIFIER_REGEX =
		new ConcatRegex(
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
	private static final OORegex DIGIT_REGEX = UnionRegex.fromRange('0', '9');
	
	/**
	 * Regex defining a numeral (used to define numeric literals).
	 *
	 * numeral ::= digit {[underline] digit}
	 */
	private static final OORegex NUMERAL_REGEX =
		new ConcatRegex(
			DIGIT_REGEX,
			new ZeroOrMoreRegex(
				new ConcatRegex(
					new ZeroOrOneRegex(new UnitRegex("_")),
					DIGIT_REGEX
				)
			)
		);
	
	/**
	 * Regex defining an exponent (used to define numeric literals).
	 *
	 * exponent ::= E [+] numeral | E – numeral
	 */
	private static final OORegex EXPONENT_REGEX =
		ConcatRegex.fromRegexes(
			new UnitRegex("E"),
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
	private static final OORegex DECIMAL_LITERAL_REGEX =
		ConcatRegex.fromRegexes(
			NUMERAL_REGEX,
			new ZeroOrOneRegex(
				new ConcatRegex(
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
	private static final OORegex BASE_REGEX = NUMERAL_REGEX;
	
	/**
	 * Regex defining an extended digit (hexadecimal digit).
	 *
	 * extended_digit ::= digit | A | B | C | D | E | F | a | b | c | d | e | f
	 */
	private static final OORegex EXTENDED_DIGIT_REGEX =
		UnionRegex.fromRegexes(
			DIGIT_REGEX,
			new UnitRegex("A"),
			new UnitRegex("B"),
			new UnitRegex("C"),
			new UnitRegex("D"),
			new UnitRegex("E"),
			new UnitRegex("F"),
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
	private static final OORegex BASED_NUMERAL_REGEX =
		new ConcatRegex(
			EXTENDED_DIGIT_REGEX,
			new ZeroOrMoreRegex(
				new ConcatRegex(
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
	private static final OORegex BASED_LITERAL_REGEX =
		ConcatRegex.fromRegexes(
			BASE_REGEX,
			new UnitRegex("#"),
			BASED_NUMERAL_REGEX,
			new ZeroOrOneRegex(
				new ConcatRegex(
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
	private static final OORegex CHARACTER_LITERAL_REGEX =
		ConcatRegex.fromRegexes(
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
	private static final OORegex NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX =
		new IntersectionRegex(
			GRAPHIC_CHARACTER_REGEX,
			new NotRegex(new UnitRegex("\""))
		);
	
	/**
	 * Regex defining a string element (used to define string literals).
	 *
	 * string_element ::= "" | non_quotation_mark_graphic_character
	 */
	private static final OORegex STRING_ELEMENT_REGEX =
		new UnionRegex(
			new UnitRegex("\"\""),
			NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX
		);
	
	/**
	 * Regex defining an Ada string literal.
	 *
	 * string_literal ::= "{string_element}"
	 */
	private static final OORegex STRING_LITERAL_REGEX =
		ConcatRegex.fromRegexes(
			new UnitRegex("\""),
			new ZeroOrMoreRegex(STRING_ELEMENT_REGEX),
			new UnitRegex("\"")
		);
	
	// Comments
	
	/**
	 * Regex defining a non-end-of-line character (useed to define comments).
	 */
	private static final OORegex NON_END_OF_LINE_CHARACTER_REGEX =
		new NotRegex(
			new UnionRegex(
				new UnitRegex("\r"),
				new UnitRegex("\n")
			)
		);
	
	/**
	 * Regex defining an Ada comment.
	 *
	 * comment ::= --{non_end_of_line_character}
	 */
	private static final OORegex COMMENT_REGEX =
		ConcatRegex.fromRegexes(
			new UnitRegex("--"),
			new ZeroOrMoreRegex(NON_END_OF_LINE_CHARACTER_REGEX)
		);
	
	// Pragmas
	
	/**
	 * Regex defining an Ada pragma.
	 *
	 * TODO: Figure out whether or not a pragma as a whole should be
	 *       analysed at Lexer level.
	 *       Note: The pragma keyword is already analysed by the lexer at which
	 *       point it returns a token corresponding to the keyword. The question
	 *       here is whether the lexer needs to return a single token for the
	 *       pragma directive as a whole, as may be implied by the inclusion of
	 *       the pragma directive syntax in the "Lexical Elements" chapter of
	 *       the Ada spec (section 2.8), or whether handling pragma directives
	 *       should be left to the parser, which seems more logical to me.
	 */
	private static final OORegex PRAGMA_REGEX = null;
	
	/**
	 * Unit regexes for matching Ada keywords.
	 */
	private static final OORegex ABORT_KEYWORD_REGEX        = new UnitRegex("abort"       , 1);
	private static final OORegex ABS_KEYWORD_REGEX          = new UnitRegex("abs"         , 1);
	private static final OORegex ABSTRACT_KEYWORD_REGEX     = new UnitRegex("abstract"    , 1);
	private static final OORegex ACCEPT_KEYWORD_REGEX       = new UnitRegex("accept"      , 1);
	private static final OORegex ACCESS_KEYWORD_REGEX       = new UnitRegex("access"      , 1);
	private static final OORegex ALIASED_KEYWORD_REGEX      = new UnitRegex("aliased"     , 1);
	private static final OORegex ALL_KEYWORD_REGEX          = new UnitRegex("all"         , 1);
	private static final OORegex AND_KEYWORD_REGEX          = new UnitRegex("and"         , 1);
	private static final OORegex ARRAY_KEYWORD_REGEX        = new UnitRegex("array"       , 1);
	private static final OORegex AT_KEYWORD_REGEX           = new UnitRegex("at"          , 1);
	
	private static final OORegex BEGIN_KEYWORD_REGEX        = new UnitRegex("begin"       , 1);
	private static final OORegex BODY_KEYWORD_REGEX         = new UnitRegex("body"        , 1);
	
	private static final OORegex CASE_KEYWORD_REGEX         = new UnitRegex("case"        , 1);
	private static final OORegex CONSTANT_KEYWORD_REGEX     = new UnitRegex("constant"    , 1);
	
	private static final OORegex DECLARE_KEYWORD_REGEX      = new UnitRegex("declare"     , 1);
	private static final OORegex DELAY_KEYWORD_REGEX        = new UnitRegex("delay"       , 1);
	private static final OORegex DELTA_KEYWORD_REGEX        = new UnitRegex("delta"       , 1);
	private static final OORegex DIGITS_KEYWORD_REGEX       = new UnitRegex("digits"      , 1);
	private static final OORegex DO_KEYWORD_REGEX           = new UnitRegex("do"          , 1);
	
	private static final OORegex ELSE_KEYWORD_REGEX         = new UnitRegex("else"        , 1);
	private static final OORegex ELSIF_KEYWORD_REGEX        = new UnitRegex("elsif"       , 1);
	private static final OORegex END_KEYWORD_REGEX          = new UnitRegex("end"         , 1);
	private static final OORegex ENTRY_KEYWORD_REGEX        = new UnitRegex("entry"       , 1);
	private static final OORegex EXCEPTION_KEYWORD_REGEX    = new UnitRegex("exception"   , 1);
	private static final OORegex EXIT_KEYWORD_REGEX         = new UnitRegex("exit"        , 1);
	
	private static final OORegex FOR_KEYWORD_REGEX          = new UnitRegex("for"         , 1);
	private static final OORegex FUNCTION_KEYWORD_REGEX     = new UnitRegex("function"    , 1);
	
	private static final OORegex GENERIC_KEYWORD_REGEX      = new UnitRegex("generic"     , 1);
	private static final OORegex GOTO_KEYWORD_REGEX         = new UnitRegex("goto"        , 1);
	
	private static final OORegex IF_KEYWORD_REGEX           = new UnitRegex("if"          , 1);
	private static final OORegex IN_KEYWORD_REGEX           = new UnitRegex("in"          , 1);
	private static final OORegex INTERFACE_KEYWORD_REGEX    = new UnitRegex("interface"   , 1);
	private static final OORegex IS_KEYWORD_REGEX           = new UnitRegex("is"          , 1);
	
	private static final OORegex LIMITED_KEYWORD_REGEX      = new UnitRegex("limited"     , 1);
	private static final OORegex LOOP_KEYWORD_REGEX         = new UnitRegex("loop"        , 1);
	
	private static final OORegex MOD_KEYWORD_REGEX          = new UnitRegex("mod"         , 1);
	
	private static final OORegex NEW_KEYWORD_REGEX          = new UnitRegex("new"         , 1);
	private static final OORegex NOT_KEYWORD_REGEX          = new UnitRegex("not"         , 1);
	private static final OORegex NULL_KEYWORD_REGEX         = new UnitRegex("null"        , 1);
	
	private static final OORegex OF_KEYWORD_REGEX           = new UnitRegex("of"          , 1);
	private static final OORegex OR_KEYWORD_REGEX           = new UnitRegex("or"          , 1);
	private static final OORegex OTHERS_KEYWORD_REGEX       = new UnitRegex("others"      , 1);
	private static final OORegex OUT_KEYWORD_REGEX          = new UnitRegex("out"         , 1);
	private static final OORegex OVERRIDING_KEYWORD_REGEX   = new UnitRegex("overriding"  , 1);
	
	private static final OORegex PACKAGE_KEYWORD_REGEX      = new UnitRegex("package"     , 1);
	private static final OORegex PRAGMA_KEYWORD_REGEX       = new UnitRegex("pragma"      , 1);
	private static final OORegex PRIVATE_KEYWORD_REGEX      = new UnitRegex("private"     , 1);
	private static final OORegex PROCEDURE_KEYWORD_REGEX    = new UnitRegex("procedure"   , 1);
	private static final OORegex PROTECTED_KEYWORD_REGEX    = new UnitRegex("protected"   , 1);
	
	private static final OORegex RAISE_KEYWORD_REGEX        = new UnitRegex("raise"       , 1);
	private static final OORegex RANGE_KEYWORD_REGEX        = new UnitRegex("range"       , 1);
	private static final OORegex RECORD_KEYWORD_REGEX       = new UnitRegex("record"      , 1);
	private static final OORegex REM_KEYWORD_REGEX          = new UnitRegex("rem"         , 1);
	private static final OORegex RENAMES_KEYWORD_REGEX      = new UnitRegex("renames"     , 1);
	private static final OORegex REQUEUE_KEYWORD_REGEX      = new UnitRegex("requeue"     , 1);
	private static final OORegex RETURN_KEYWORD_REGEX       = new UnitRegex("return"      , 1);
	private static final OORegex REVERSE_KEYWORD_REGEX      = new UnitRegex("reverse"     , 1);
	
	private static final OORegex SELECT_KEYWORD_REGEX       = new UnitRegex("select"      , 1);
	private static final OORegex SEPARATE_KEYWORD_REGEX     = new UnitRegex("separate"    , 1);
	private static final OORegex SOME_KEYWORD_REGEX         = new UnitRegex("some"        , 1);
	private static final OORegex SUBTYPE_KEYWORD_REGEX      = new UnitRegex("subtype"     , 1);
	private static final OORegex SYNCHRONIZED_KEYWORD_REGEX = new UnitRegex("synchronized", 1);
	
	private static final OORegex TAGGED_KEYWORD_REGEX       = new UnitRegex("tagged"      , 1);
	private static final OORegex TASK_KEYWORD_REGEX         = new UnitRegex("task"        , 1);
	private static final OORegex TERMINATE_KEYWORD_REGEX    = new UnitRegex("terminate"   , 1);
	private static final OORegex THEN_KEYWORD_REGEX         = new UnitRegex("then"        , 1);
	private static final OORegex TYPE_KEYWORD_REGEX         = new UnitRegex("type"        , 1);
	
	private static final OORegex UNTIL_KEYWORD_REGEX        = new UnitRegex("until"       , 1);
	private static final OORegex USE_KEYWORD_REGEX          = new UnitRegex("use"         , 1);
	
	private static final OORegex WHEN_KEYWORD_REGEX         = new UnitRegex("when"        , 1);
	private static final OORegex WHILE_KEYWORD_REGEX        = new UnitRegex("while"       , 1);
	private static final OORegex WITH_KEYWORD_REGEX         = new UnitRegex("with"        , 1);
	
	private static final OORegex XOR_KEYWORD_REGEX          = new UnitRegex("xor"         , 1);
	
	/**
	 * A map associating root regexes with the token types
	 * they represent.
	 */
	private static final Map<OORegex, IElementType> REGEX_TOKEN_TYPES;
	
	/**
	 * The set of all root regexes (defined above).
	 */
	private static final Set<OORegex> ROOT_REGEXES;
	
	// Static Initializer
	
	static {
		
		// Populate the regex -> token-type map
		
		REGEX_TOKEN_TYPES = new HashMap<>();
		
		REGEX_TOKEN_TYPES.put(WHITE_SPACES_REGEX        , AdaTokenTypes.WHITE_SPACES);
		
		REGEX_TOKEN_TYPES.put(AMPERSAND_REGEX           , AdaTokenTypes.AMPERSAND);
		REGEX_TOKEN_TYPES.put(APOSTROPHE_REGEX          , AdaTokenTypes.APOSTROPHE);
		REGEX_TOKEN_TYPES.put(LEFT_PARENTHESIS_REGEX    , AdaTokenTypes.LEFT_PARENTHESIS);
		REGEX_TOKEN_TYPES.put(RIGHT_PARENTHESIS_REGEX   , AdaTokenTypes.RIGHT_PARENTHESIS);
		REGEX_TOKEN_TYPES.put(ASTERISK_REGEX            , AdaTokenTypes.ASTERISK);
		REGEX_TOKEN_TYPES.put(PLUS_SIGN_REGEX           , AdaTokenTypes.PLUS_SIGN);
		REGEX_TOKEN_TYPES.put(COMMA_REGEX               , AdaTokenTypes.COMMA);
		REGEX_TOKEN_TYPES.put(HYPHEN_MINUS_REGEX        , AdaTokenTypes.HYPHEN_MINUS);
		REGEX_TOKEN_TYPES.put(FULL_STOP_REGEX           , AdaTokenTypes.FULL_STOP);
		REGEX_TOKEN_TYPES.put(SOLIDUS_REGEX             , AdaTokenTypes.SOLIDUS);
		REGEX_TOKEN_TYPES.put(COLON_REGEX               , AdaTokenTypes.COLON);
		REGEX_TOKEN_TYPES.put(SEMICOLON_REGEX           , AdaTokenTypes.SEMICOLON);
		REGEX_TOKEN_TYPES.put(LESS_THAN_SIGN_REGEX      , AdaTokenTypes.LESS_THAN_SIGN);
		REGEX_TOKEN_TYPES.put(EQUALS_SIGN_REGEX         , AdaTokenTypes.EQUALS_SIGN);
		REGEX_TOKEN_TYPES.put(GREATER_THAN_SIGN_REGEX   , AdaTokenTypes.GREATER_THAN_SIGN);
		REGEX_TOKEN_TYPES.put(VERTICAL_LINE_REGEX       , AdaTokenTypes.VERTICAL_LINE);
		
		REGEX_TOKEN_TYPES.put(ARROW_REGEX               , AdaTokenTypes.ARROW);
		REGEX_TOKEN_TYPES.put(DOUBLE_DOT_REGEX          , AdaTokenTypes.DOUBLE_DOT);
		REGEX_TOKEN_TYPES.put(DOUBLE_ASTERISK_REGEX     , AdaTokenTypes.DOUBLE_ASTERISK);
		REGEX_TOKEN_TYPES.put(ASSIGNMENT_REGEX          , AdaTokenTypes.ASSIGNMENT);
		REGEX_TOKEN_TYPES.put(NOT_EQUAL_SIGN_REGEX      , AdaTokenTypes.NOT_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(GREATER_EQUAL_SIGN_REGEX  , AdaTokenTypes.GREATER_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(LESS_EQUAL_SIGN_REGEX     , AdaTokenTypes.LESS_EQUAL_SIGN);
		REGEX_TOKEN_TYPES.put(LEFT_LABEL_BRACKET_REGEX  , AdaTokenTypes.LEFT_LABEL_BRACKET);
		REGEX_TOKEN_TYPES.put(RIGHT_LABEL_BRACKET_REGEX , AdaTokenTypes.RIGHT_LABEL_BRACKET);
		REGEX_TOKEN_TYPES.put(BOX_SIGN_REGEX            , AdaTokenTypes.BOX_SIGN);
		
		REGEX_TOKEN_TYPES.put(IDENTIFIER_REGEX          , AdaTokenTypes.IDENTIFIER);
		REGEX_TOKEN_TYPES.put(DECIMAL_LITERAL_REGEX     , AdaTokenTypes.DECIMAL_LITERAL);
		REGEX_TOKEN_TYPES.put(BASED_LITERAL_REGEX       , AdaTokenTypes.BASED_LITERAL);
		REGEX_TOKEN_TYPES.put(CHARACTER_LITERAL_REGEX   , AdaTokenTypes.CHARACTER_LITERAL);
		REGEX_TOKEN_TYPES.put(STRING_LITERAL_REGEX      , AdaTokenTypes.STRING_LITERAL);
		
		REGEX_TOKEN_TYPES.put(COMMENT_REGEX             , AdaTokenTypes.COMMENT);
		
		REGEX_TOKEN_TYPES.put(ABORT_KEYWORD_REGEX       , AdaTokenTypes.ABORT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ABS_KEYWORD_REGEX         , AdaTokenTypes.ABS_KEYWORD);
		REGEX_TOKEN_TYPES.put(ABSTRACT_KEYWORD_REGEX    , AdaTokenTypes.ABSTRACT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ACCEPT_KEYWORD_REGEX      , AdaTokenTypes.ACCEPT_KEYWORD);
		REGEX_TOKEN_TYPES.put(ACCESS_KEYWORD_REGEX      , AdaTokenTypes.ACCESS_KEYWORD);
		REGEX_TOKEN_TYPES.put(ALIASED_KEYWORD_REGEX     , AdaTokenTypes.ALIASED_KEYWORD);
		REGEX_TOKEN_TYPES.put(ALL_KEYWORD_REGEX         , AdaTokenTypes.ALL_KEYWORD);
		REGEX_TOKEN_TYPES.put(AND_KEYWORD_REGEX         , AdaTokenTypes.AND_KEYWORD);
		REGEX_TOKEN_TYPES.put(ARRAY_KEYWORD_REGEX       , AdaTokenTypes.ARRAY_KEYWORD);
		REGEX_TOKEN_TYPES.put(AT_KEYWORD_REGEX          , AdaTokenTypes.AT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(BEGIN_KEYWORD_REGEX       , AdaTokenTypes.BEGIN_KEYWORD);
		REGEX_TOKEN_TYPES.put(BODY_KEYWORD_REGEX        , AdaTokenTypes.BODY_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(CASE_KEYWORD_REGEX        , AdaTokenTypes.CASE_KEYWORD);
		REGEX_TOKEN_TYPES.put(CONSTANT_KEYWORD_REGEX    , AdaTokenTypes.CONSTANT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(DECLARE_KEYWORD_REGEX     , AdaTokenTypes.DECLARE_KEYWORD);
		REGEX_TOKEN_TYPES.put(DELAY_KEYWORD_REGEX       , AdaTokenTypes.DELAY_KEYWORD);
		REGEX_TOKEN_TYPES.put(DELTA_KEYWORD_REGEX       , AdaTokenTypes.DELTA_KEYWORD);
		REGEX_TOKEN_TYPES.put(DIGITS_KEYWORD_REGEX      , AdaTokenTypes.DIGITS_KEYWORD);
		REGEX_TOKEN_TYPES.put(DO_KEYWORD_REGEX          , AdaTokenTypes.DO_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(ELSE_KEYWORD_REGEX        , AdaTokenTypes.ELSE_KEYWORD);
		REGEX_TOKEN_TYPES.put(ELSIF_KEYWORD_REGEX       , AdaTokenTypes.ELSIF_KEYWORD);
		REGEX_TOKEN_TYPES.put(END_KEYWORD_REGEX         , AdaTokenTypes.END_KEYWORD);
		REGEX_TOKEN_TYPES.put(ENTRY_KEYWORD_REGEX       , AdaTokenTypes.ENTRY_KEYWORD);
		REGEX_TOKEN_TYPES.put(EXCEPTION_KEYWORD_REGEX   , AdaTokenTypes.EXCEPTION_KEYWORD);
		REGEX_TOKEN_TYPES.put(EXIT_KEYWORD_REGEX        , AdaTokenTypes.EXIT_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(FOR_KEYWORD_REGEX         , AdaTokenTypes.FOR_KEYWORD);
		REGEX_TOKEN_TYPES.put(FUNCTION_KEYWORD_REGEX    , AdaTokenTypes.FUNCTION_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(GENERIC_KEYWORD_REGEX     , AdaTokenTypes.GENERIC_KEYWORD);
		REGEX_TOKEN_TYPES.put(GOTO_KEYWORD_REGEX        , AdaTokenTypes.GOTO_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(IF_KEYWORD_REGEX          , AdaTokenTypes.IF_KEYWORD);
		REGEX_TOKEN_TYPES.put(IN_KEYWORD_REGEX          , AdaTokenTypes.IN_KEYWORD);
		REGEX_TOKEN_TYPES.put(INTERFACE_KEYWORD_REGEX   , AdaTokenTypes.INTERFACE_KEYWORD);
		REGEX_TOKEN_TYPES.put(IS_KEYWORD_REGEX          , AdaTokenTypes.IS_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(LIMITED_KEYWORD_REGEX     , AdaTokenTypes.LIMITED_KEYWORD);
		REGEX_TOKEN_TYPES.put(LOOP_KEYWORD_REGEX        , AdaTokenTypes.LOOP_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(MOD_KEYWORD_REGEX         , AdaTokenTypes.MOD_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(NEW_KEYWORD_REGEX         , AdaTokenTypes.NEW_KEYWORD);
		REGEX_TOKEN_TYPES.put(NOT_KEYWORD_REGEX         , AdaTokenTypes.NOT_KEYWORD);
		REGEX_TOKEN_TYPES.put(NULL_KEYWORD_REGEX        , AdaTokenTypes.NULL_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(OF_KEYWORD_REGEX          , AdaTokenTypes.OF_KEYWORD);
		REGEX_TOKEN_TYPES.put(OR_KEYWORD_REGEX          , AdaTokenTypes.OR_KEYWORD);
		REGEX_TOKEN_TYPES.put(OTHERS_KEYWORD_REGEX      , AdaTokenTypes.OTHERS_KEYWORD);
		REGEX_TOKEN_TYPES.put(OUT_KEYWORD_REGEX         , AdaTokenTypes.OUT_KEYWORD);
		REGEX_TOKEN_TYPES.put(OVERRIDING_KEYWORD_REGEX  , AdaTokenTypes.OVERRIDING_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(PACKAGE_KEYWORD_REGEX     , AdaTokenTypes.PACKAGE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PRAGMA_KEYWORD_REGEX      , AdaTokenTypes.PRAGMA_KEYWORD);
		REGEX_TOKEN_TYPES.put(PRIVATE_KEYWORD_REGEX     , AdaTokenTypes.PRIVATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PROCEDURE_KEYWORD_REGEX   , AdaTokenTypes.PROCEDURE_KEYWORD);
		REGEX_TOKEN_TYPES.put(PROTECTED_KEYWORD_REGEX   , AdaTokenTypes.PROTECTED_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(RAISE_KEYWORD_REGEX       , AdaTokenTypes.RAISE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RANGE_KEYWORD_REGEX       , AdaTokenTypes.RANGE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RECORD_KEYWORD_REGEX      , AdaTokenTypes.RECORD_KEYWORD);
		REGEX_TOKEN_TYPES.put(REM_KEYWORD_REGEX         , AdaTokenTypes.REM_KEYWORD);
		REGEX_TOKEN_TYPES.put(RENAMES_KEYWORD_REGEX     , AdaTokenTypes.RENAMES_KEYWORD);
		REGEX_TOKEN_TYPES.put(REQUEUE_KEYWORD_REGEX     , AdaTokenTypes.REQUEUE_KEYWORD);
		REGEX_TOKEN_TYPES.put(RETURN_KEYWORD_REGEX      , AdaTokenTypes.RETURN_KEYWORD);
		REGEX_TOKEN_TYPES.put(REVERSE_KEYWORD_REGEX     , AdaTokenTypes.REVERSE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(SELECT_KEYWORD_REGEX      , AdaTokenTypes.SELECT_KEYWORD);
		REGEX_TOKEN_TYPES.put(SEPARATE_KEYWORD_REGEX    , AdaTokenTypes.SEPARATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(SOME_KEYWORD_REGEX        , AdaTokenTypes.SOME_KEYWORD);
		REGEX_TOKEN_TYPES.put(SUBTYPE_KEYWORD_REGEX     , AdaTokenTypes.SUBTYPE_KEYWORD);
		REGEX_TOKEN_TYPES.put(SYNCHRONIZED_KEYWORD_REGEX, AdaTokenTypes.SYNCHRONIZED_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(TAGGED_KEYWORD_REGEX      , AdaTokenTypes.TAGGED_KEYWORD);
		REGEX_TOKEN_TYPES.put(TASK_KEYWORD_REGEX        , AdaTokenTypes.TASK_KEYWORD);
		REGEX_TOKEN_TYPES.put(TERMINATE_KEYWORD_REGEX   , AdaTokenTypes.TERMINATE_KEYWORD);
		REGEX_TOKEN_TYPES.put(THEN_KEYWORD_REGEX        , AdaTokenTypes.THEN_KEYWORD);
		REGEX_TOKEN_TYPES.put(TYPE_KEYWORD_REGEX        , AdaTokenTypes.TYPE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(UNTIL_KEYWORD_REGEX       , AdaTokenTypes.UNTIL_KEYWORD);
		REGEX_TOKEN_TYPES.put(USE_KEYWORD_REGEX         , AdaTokenTypes.USE_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(WHEN_KEYWORD_REGEX        , AdaTokenTypes.WHEN_KEYWORD);
		REGEX_TOKEN_TYPES.put(WHILE_KEYWORD_REGEX       , AdaTokenTypes.WHILE_KEYWORD);
		REGEX_TOKEN_TYPES.put(WITH_KEYWORD_REGEX        , AdaTokenTypes.WITH_KEYWORD);
		
		REGEX_TOKEN_TYPES.put(XOR_KEYWORD_REGEX         , AdaTokenTypes.XOR_KEYWORD);
		
		// Set the root regexes set from the regex -> token-type map's keyset
		
		ROOT_REGEXES = Collections.unmodifiableSet(REGEX_TOKEN_TYPES.keySet());
		
	}
	
	// Fields
	
	/**
	 * The text to be analysed.
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
	
	// Constructor
	
	/**
	 * Constructs a new Ada Lexer.
	 */
	protected AdaLexer() {}
	
	// Methods
	
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
		
		text            = buffer;
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
		
		Set<OORegex> regexes         = new HashSet<>(ROOT_REGEXES);
		Set<OORegex> matchingRegexes = new HashSet<>();
		
		// A map specifying the root regex from which every regex originates
		// by a series of calls to regex.advanced(char)
		Map<OORegex, OORegex> regexLineages = new HashMap<>();
		
		// The next character to be analysed
		char nextCharacter = text.charAt(lexingOffset);
		
		// While the next token has not been determined...
		
		characterLoop: // label only used for reference in comments
		while (tokenEnd == tokenStart) {
			
			// The set of regexes that advanced successfully in this
			// iteration of characterLoop
			Set<OORegex> advancedRegexes = new HashSet<>();
			
			Iterator<OORegex> regexIterator = regexes.iterator();
			
			// For each regex that successfully advanced by all
			// characters so far...
			
			while (regexIterator.hasNext()) {
			
				OORegex regex = regexIterator.next();
				
				// Try to advance the regex
				
				OORegex advancedRegex = regex.advanced(nextCharacter);
				
				// If the regex advanced successfully, store it for the next
				// iteration of characterLoop, and keep track of the root
				// regex that is the ancestor of the advanced regex
				
				if (advancedRegex != null) {
					
					advancedRegexes.add(advancedRegex);
					
					OORegex ancestor = regexLineages.remove(regex);
					regexLineages.put(advancedRegex, ancestor == null ? regex : ancestor);
					
				}
				
			}
			
			// Set the regex set to be the advanced regex set
			
			regexes = advancedRegexes;
			
			int remainingRegexCount = regexes.size();
			
			// If no remaining matching regexes exist, or the last character
			// of the text is reached...
			
			if (remainingRegexCount == 0 || lexingOffset == lexingEndOffset - 1) {
				
				Iterator<OORegex> matchingRegexIterator;
				
				// If no remaining matching regexes exist, then choose a regex
				// from those that matched during the previous iteration of
				// characterLoop
				
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
				
				OORegex highestPriorityRegex = null;
				
				// Find the matching regex with the highest priority
				// The chosen regex still has to be nullable, which prevents for
				// example the word "proc" at the end of an Ada file from being
				// assigned the token of the procedure keyword, as its regex for
				// that has a higher priority than the identifier regex, and it
				// does match the sequence "proc" (but it should not be chosen
				// as its advanced regex at that point is not nullable, in other
				// words it still requires the sequence "edure" to "fully match")
				
				while (matchingRegexIterator.hasNext()) {
					
					OORegex regex = matchingRegexIterator.next();
					
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
					
					OORegex rootRegex =
						regexLineages.getOrDefault(highestPriorityRegex, highestPriorityRegex);
					
					tokenType =
						REGEX_TOKEN_TYPES.get(rootRegex);
					
				}
				
				// Otherwise, set the token type to BAD_CHARACTER
				
				else {
					
					tokenType = AdaTokenTypes.BAD_CHARACTER;
					
					// If this is a single-character, then the lexing offset
					// needs to be advanced manually to avoid infinite calls
					// to advance()
					
					if (lexingOffset == tokenStart) { lexingOffset++; }
					
				}
				
				// Set the token end offset to the lexing offset
				// This will also break the execution of characterLoop
				
				tokenEnd = lexingOffset;
				
			}
			
			// Otherwise, there are still matching regexes and the last character
			// in the text was not yet reached, so get the next character and
			// execute the next iteration of characterLoop
			
			else {
				
				// Advance the lexer to the next character
				
				nextCharacter = text.charAt(++lexingOffset);
				
				// Store the set of regexes that advanced successfully in this
				// iteration of characterLoop (useful for the next iteration)
				
				matchingRegexes = new HashSet<>(regexes);
				
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
	
}
