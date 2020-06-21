package com.adacore.adaintellij.analysis.lexical;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Token types for Ada 2012.
 */
public final class AdaTokenTypes {

	/**
	 * Ada line comment prefix.
	 */
	public static final String COMMENT_PREFIX = "--";

	/*
		Tokens
	*/

	/**
	 * Ada token representing a contiguous whitespace sequence.
	 */
	static final IElementType WHITESPACES               = TokenType.WHITE_SPACE;

	/**
	 * Ada token representing a syntactically invalid character.
	 */
	static final IElementType BAD_CHARACTER             = TokenType.BAD_CHARACTER;

	/**
	 * Ada tokens representing single-character delimiters.
	 */
	static final AdaFixedTokenType AMPERSAND            = new AdaFixedTokenType("AMPERSAND"          , "&");
	static final AdaFixedTokenType APOSTROPHE           = new AdaFixedTokenType("APOSTROPHE"         , "'");
	public static final AdaFixedTokenType LEFT_PARENTHESIS     	= new AdaFixedTokenType("LEFT_PARENTHESIS"   , "(");
	public static final AdaFixedTokenType RIGHT_PARENTHESIS    	= new AdaFixedTokenType("RIGHT_PARENTHESIS"  , ")");
	static final AdaFixedTokenType ASTERISK             = new AdaFixedTokenType("ASTERISK"           , "*");
	static final AdaFixedTokenType PLUS_SIGN            = new AdaFixedTokenType("PLUS_SIGN"          , "+");
	static final AdaFixedTokenType COMMA                = new AdaFixedTokenType("COMMA"              , ",");
	static final AdaFixedTokenType HYPHEN_MINUS         = new AdaFixedTokenType("HYPHEN_MINUS"       , "-");
	static final AdaFixedTokenType FULL_STOP            = new AdaFixedTokenType("FULL_STOP"          , ".");
	static final AdaFixedTokenType SOLIDUS              = new AdaFixedTokenType("SOLIDUS"            , "/");
	static final AdaFixedTokenType COLON                = new AdaFixedTokenType("COLON"              , ":");
	static final AdaFixedTokenType SEMICOLON            = new AdaFixedTokenType("SEMICOLON"          , ";");
	static final AdaFixedTokenType LESS_THAN_SIGN       = new AdaFixedTokenType("LESS_THAN_SIGN"     , "<");
	static final AdaFixedTokenType EQUALS_SIGN          = new AdaFixedTokenType("EQUALS_SIGN"        , "=");
	static final AdaFixedTokenType GREATER_THAN_SIGN    = new AdaFixedTokenType("GREATER_THAN_SIGN"  , ">");
	static final AdaFixedTokenType VERTICAL_LINE        = new AdaFixedTokenType("VERTICAL_LINE"      , "|");

	/**
	 * Ada tokens representing compound delimiters.
	 */
	static final AdaFixedTokenType ARROW                = new AdaFixedTokenType("ARROW"              , "=>");
	static final AdaFixedTokenType DOUBLE_DOT           = new AdaFixedTokenType("DOUBLE_DOT"         , "..");
	static final AdaFixedTokenType DOUBLE_ASTERISK      = new AdaFixedTokenType("DOUBLE_ASTERISK"    , "**");
	static final AdaFixedTokenType ASSIGNMENT           = new AdaFixedTokenType("ASSIGNMENT"         , ":=");
	static final AdaFixedTokenType NOT_EQUAL_SIGN       = new AdaFixedTokenType("NOT_EQUAL_SIGN"     , "/=");
	static final AdaFixedTokenType GREATER_EQUAL_SIGN   = new AdaFixedTokenType("GREATER_EQUAL_SIGN" , ">=");
	static final AdaFixedTokenType LESS_EQUAL_SIGN      = new AdaFixedTokenType("LESS_EQUAL_SIGN"    , "<=");
	static final AdaFixedTokenType LEFT_LABEL_BRACKET   = new AdaFixedTokenType("LEFT_LABEL_BRACKET" , "<<");
	static final AdaFixedTokenType RIGHT_LABEL_BRACKET  = new AdaFixedTokenType("RIGHT_LABEL_BRACKET", ">>");
	static final AdaFixedTokenType BOX_SIGN             = new AdaFixedTokenType("BOX_SIGN"           , "<>");

	/**
	 * Ada tokens representing identifiers and literals.
	 */
	static final AdaTokenType      IDENTIFIER           = new AdaTokenType("IDENTIFIER");        // ident3
	static final AdaTokenType      DECIMAL_LITERAL      = new AdaTokenType("DECIMAL_LITERAL");   // 3.14
	static final AdaTokenType      BASED_LITERAL        = new AdaTokenType("BASED_LITERAL");     // 16#F8#E1
	static final AdaTokenType      CHARACTER_LITERAL    = new AdaTokenType("CHARACTER_LITERAL"); // 'a'
	static final AdaTokenType      STRING_LITERAL       = new AdaTokenType("STRING_LITERAL");    // "hello :)"

	/**
	 * Ada token representing a single comment.
	 */
	static final AdaTokenType      COMMENT              = new AdaTokenType("COMMENT");           // -- Ada comment

	/**
	 * Ada tokens representing reserved keywords.
	 */
	static final AdaFixedTokenType ABORT_KEYWORD        = new AdaFixedTokenType("ABORT_KEYWORD"       , "abort");
	static final AdaFixedTokenType ABS_KEYWORD          = new AdaFixedTokenType("ABS_KEYWORD"         , "abs");
	static final AdaFixedTokenType ABSTRACT_KEYWORD     = new AdaFixedTokenType("ABSTRACT_KEYWORD"    , "abstract");
	static final AdaFixedTokenType ACCEPT_KEYWORD       = new AdaFixedTokenType("ACCEPT_KEYWORD"      , "accept");
	static final AdaFixedTokenType ACCESS_KEYWORD       = new AdaFixedTokenType("ACCESS_KEYWORD"      , "access");
	static final AdaFixedTokenType ALIASED_KEYWORD      = new AdaFixedTokenType("ALIASED_KEYWORD"     , "aliased");
	static final AdaFixedTokenType ALL_KEYWORD          = new AdaFixedTokenType("ALL_KEYWORD"         , "all");
	static final AdaFixedTokenType AND_KEYWORD          = new AdaFixedTokenType("AND_KEYWORD"         , "and");
	static final AdaFixedTokenType ARRAY_KEYWORD        = new AdaFixedTokenType("ARRAY_KEYWORD"       , "array");
	static final AdaFixedTokenType AT_KEYWORD           = new AdaFixedTokenType("AT_KEYWORD"          , "at");

	static final AdaFixedTokenType BEGIN_KEYWORD        = new AdaFixedTokenType("BEGIN_KEYWORD"       , "begin");
	static final AdaFixedTokenType BODY_KEYWORD         = new AdaFixedTokenType("BODY_KEYWORD"        , "body");

	static final AdaFixedTokenType CASE_KEYWORD         = new AdaFixedTokenType("CASE_KEYWORD"        , "case");
	static final AdaFixedTokenType CONSTANT_KEYWORD     = new AdaFixedTokenType("CONSTANT_KEYWORD"    , "constant");

	static final AdaFixedTokenType DECLARE_KEYWORD      = new AdaFixedTokenType("DECLARE_KEYWORD"     , "declare");
	static final AdaFixedTokenType DELAY_KEYWORD        = new AdaFixedTokenType("DELAY_KEYWORD"       , "delay");
	static final AdaFixedTokenType DELTA_KEYWORD        = new AdaFixedTokenType("DELTA_KEYWORD"       , "delta");
	static final AdaFixedTokenType DIGITS_KEYWORD       = new AdaFixedTokenType("DIGITS_KEYWORD"      , "digits");
	static final AdaFixedTokenType DO_KEYWORD           = new AdaFixedTokenType("DO_KEYWORD"          , "do");

	static final AdaFixedTokenType ELSE_KEYWORD         = new AdaFixedTokenType("ELSE_KEYWORD"        , "else");
	static final AdaFixedTokenType ELSIF_KEYWORD        = new AdaFixedTokenType("ELSIF_KEYWORD"       , "elsif");
	static final AdaFixedTokenType END_KEYWORD          = new AdaFixedTokenType("END_KEYWORD"         , "end");
	static final AdaFixedTokenType ENTRY_KEYWORD        = new AdaFixedTokenType("ENTRY_KEYWORD"       , "entry");
	static final AdaFixedTokenType EXCEPTION_KEYWORD    = new AdaFixedTokenType("EXCEPTION_KEYWORD"   , "exception");
	static final AdaFixedTokenType EXIT_KEYWORD         = new AdaFixedTokenType("EXIT_KEYWORD"        , "exit");

	static final AdaFixedTokenType FOR_KEYWORD          = new AdaFixedTokenType("FOR_KEYWORD"         , "for");
	static final AdaFixedTokenType FUNCTION_KEYWORD     = new AdaFixedTokenType("FUNCTION_KEYWORD"    , "function");

	static final AdaFixedTokenType GENERIC_KEYWORD      = new AdaFixedTokenType("GENERIC_KEYWORD"     , "generic");
	static final AdaFixedTokenType GOTO_KEYWORD         = new AdaFixedTokenType("GOTO_KEYWORD"        , "goto");

	static final AdaFixedTokenType IF_KEYWORD           = new AdaFixedTokenType("IF_KEYWORD"          , "if");
	static final AdaFixedTokenType IN_KEYWORD           = new AdaFixedTokenType("IN_KEYWORD"          , "in");
	static final AdaFixedTokenType INTERFACE_KEYWORD    = new AdaFixedTokenType("INTERFACE_KEYWORD"   , "interface");
	static final AdaFixedTokenType IS_KEYWORD           = new AdaFixedTokenType("IS_KEYWORD"          , "is");

	static final AdaFixedTokenType LIMITED_KEYWORD      = new AdaFixedTokenType("LIMITED_KEYWORD"     , "limited");
	static final AdaFixedTokenType LOOP_KEYWORD         = new AdaFixedTokenType("LOOP_KEYWORD"        , "loop");

	static final AdaFixedTokenType MOD_KEYWORD          = new AdaFixedTokenType("MOD_KEYWORD"         , "mod");

	static final AdaFixedTokenType NEW_KEYWORD          = new AdaFixedTokenType("NEW_KEYWORD"         , "new");
	static final AdaFixedTokenType NOT_KEYWORD          = new AdaFixedTokenType("NOT_KEYWORD"         , "not");
	static final AdaFixedTokenType NULL_KEYWORD         = new AdaFixedTokenType("NULL_KEYWORD"        , "null");

	static final AdaFixedTokenType OF_KEYWORD           = new AdaFixedTokenType("OF_KEYWORD"          , "of");
	static final AdaFixedTokenType OR_KEYWORD           = new AdaFixedTokenType("OR_KEYWORD"          , "or");
	static final AdaFixedTokenType OTHERS_KEYWORD       = new AdaFixedTokenType("OTHERS_KEYWORD"      , "others");
	static final AdaFixedTokenType OUT_KEYWORD          = new AdaFixedTokenType("OUT_KEYWORD"         , "out");
	static final AdaFixedTokenType OVERRIDING_KEYWORD   = new AdaFixedTokenType("OVERRIDING_KEYWORD"  , "overriding");

	static final AdaFixedTokenType PACKAGE_KEYWORD      = new AdaFixedTokenType("PACKAGE_KEYWORD"     , "package");
	static final AdaFixedTokenType PRAGMA_KEYWORD       = new AdaFixedTokenType("PRAGMA_KEYWORD"      , "pragma");
	static final AdaFixedTokenType PRIVATE_KEYWORD      = new AdaFixedTokenType("PRIVATE_KEYWORD"     , "private");
	static final AdaFixedTokenType PROCEDURE_KEYWORD    = new AdaFixedTokenType("PROCEDURE_KEYWORD"   , "procedure");
	static final AdaFixedTokenType PROTECTED_KEYWORD    = new AdaFixedTokenType("PROTECTED_KEYWORD"   , "protected");

	static final AdaFixedTokenType RAISE_KEYWORD        = new AdaFixedTokenType("RAISE_KEYWORD"       , "raise");
	static final AdaFixedTokenType RANGE_KEYWORD        = new AdaFixedTokenType("RANGE_KEYWORD"       , "range");
	static final AdaFixedTokenType RECORD_KEYWORD       = new AdaFixedTokenType("RECORD_KEYWORD"      , "record");
	static final AdaFixedTokenType REM_KEYWORD          = new AdaFixedTokenType("REM_KEYWORD"         , "rem");
	static final AdaFixedTokenType RENAMES_KEYWORD      = new AdaFixedTokenType("RENAMES_KEYWORD"     , "renames");
	static final AdaFixedTokenType REQUEUE_KEYWORD      = new AdaFixedTokenType("REQUEUE_KEYWORD"     , "requeue");
	static final AdaFixedTokenType RETURN_KEYWORD       = new AdaFixedTokenType("RETURN_KEYWORD"      , "return");
	static final AdaFixedTokenType REVERSE_KEYWORD      = new AdaFixedTokenType("REVERSE_KEYWORD"     , "reverse");

	static final AdaFixedTokenType SELECT_KEYWORD       = new AdaFixedTokenType("SELECT_KEYWORD"      , "select");
	static final AdaFixedTokenType SEPARATE_KEYWORD     = new AdaFixedTokenType("SEPARATE_KEYWORD"    , "separate");
	static final AdaFixedTokenType SOME_KEYWORD         = new AdaFixedTokenType("SOME_KEYWORD"        , "some");
	static final AdaFixedTokenType SUBTYPE_KEYWORD      = new AdaFixedTokenType("SUBTYPE_KEYWORD"     , "subtype");
	static final AdaFixedTokenType SYNCHRONIZED_KEYWORD = new AdaFixedTokenType("SYNCHRONIZED_KEYWORD", "synchronized");

	static final AdaFixedTokenType TAGGED_KEYWORD       = new AdaFixedTokenType("TAGGED_KEYWORD"      , "tagged");
	static final AdaFixedTokenType TASK_KEYWORD         = new AdaFixedTokenType("TASK_KEYWORD"        , "task");
	static final AdaFixedTokenType TERMINATE_KEYWORD    = new AdaFixedTokenType("TERMINATE_KEYWORD"   , "terminate");
	static final AdaFixedTokenType THEN_KEYWORD         = new AdaFixedTokenType("THEN_KEYWORD"        , "then");
	static final AdaFixedTokenType TYPE_KEYWORD         = new AdaFixedTokenType("TYPE_KEYWORD"        , "type");

	static final AdaFixedTokenType UNTIL_KEYWORD        = new AdaFixedTokenType("UNTIL_KEYWORD"       , "until");
	static final AdaFixedTokenType USE_KEYWORD          = new AdaFixedTokenType("USE_KEYWORD"         , "use");

	static final AdaFixedTokenType WHEN_KEYWORD         = new AdaFixedTokenType("WHEN_KEYWORD"        , "when");
	static final AdaFixedTokenType WHILE_KEYWORD        = new AdaFixedTokenType("WHILE_KEYWORD"       , "while");
	static final AdaFixedTokenType WITH_KEYWORD         = new AdaFixedTokenType("WITH_KEYWORD"        , "with");

	static final AdaFixedTokenType XOR_KEYWORD          = new AdaFixedTokenType("XOR_KEYWORD"         , "xor");

	/*
		Token Sets
	*/

	/**
	 * Token set representing Ada whitespaces.
	 */
	public static final TokenSet WHITESPACE_TOKEN_SET = TokenSet.create(WHITESPACES);

	/**
	 * Token set representing Ada delimiters.
	 */
	public static final TokenSet DELIMITER_TOKEN_SET = TokenSet.create(

		AMPERSAND, APOSTROPHE, LEFT_PARENTHESIS, RIGHT_PARENTHESIS, ASTERISK, PLUS_SIGN,
		COMMA, HYPHEN_MINUS, FULL_STOP, SOLIDUS, COLON, SEMICOLON, LESS_THAN_SIGN,
		EQUALS_SIGN, GREATER_THAN_SIGN, VERTICAL_LINE,

		ARROW, DOUBLE_DOT, DOUBLE_ASTERISK, ASSIGNMENT, NOT_EQUAL_SIGN, GREATER_EQUAL_SIGN,
		LESS_EQUAL_SIGN, LEFT_LABEL_BRACKET, RIGHT_LABEL_BRACKET, BOX_SIGN

	);

	/**
	 * Token sets representing Ada identifiers and literals.
	 */
	public static final TokenSet IDENTIFIER_TOKEN_SET      = TokenSet.create(IDENTIFIER);
	public static final TokenSet STRING_LITERAL_TOKEN_SET  = TokenSet.create(STRING_LITERAL);
	public static final TokenSet NUMERIC_LITERAL_TOKEN_SET = TokenSet.create(DECIMAL_LITERAL, BASED_LITERAL);

	public static final TokenSet TEXTUAl_LITERAL_TOKEN_SET = TokenSet.orSet(
		TokenSet.create(CHARACTER_LITERAL), STRING_LITERAL_TOKEN_SET);

	public static final TokenSet LITERAL_TOKEN_SET         = TokenSet.orSet(
		NUMERIC_LITERAL_TOKEN_SET, TEXTUAl_LITERAL_TOKEN_SET);

	/**
	 * Token set representing Ada comments.
	 */
	public static final TokenSet COMMENT_TOKEN_SET = TokenSet.create(COMMENT);

	/**
	 * Token set representing Ada reserved keywords.
	 */
	public static final TokenSet KEYWORD_TOKEN_SET = TokenSet.create(

		ABORT_KEYWORD, ABS_KEYWORD, ABSTRACT_KEYWORD, ACCEPT_KEYWORD, ACCESS_KEYWORD,
		ALIASED_KEYWORD, ALL_KEYWORD, AND_KEYWORD, ARRAY_KEYWORD, AT_KEYWORD,

		BEGIN_KEYWORD, BODY_KEYWORD,

		CASE_KEYWORD, CONSTANT_KEYWORD,

		DECLARE_KEYWORD, DELAY_KEYWORD, DELTA_KEYWORD, DIGITS_KEYWORD, DO_KEYWORD,

		ELSE_KEYWORD, ELSIF_KEYWORD, END_KEYWORD, ENTRY_KEYWORD, EXCEPTION_KEYWORD,
		EXIT_KEYWORD,

		FOR_KEYWORD, FUNCTION_KEYWORD,

		GENERIC_KEYWORD, GOTO_KEYWORD,

		IF_KEYWORD, IN_KEYWORD, INTERFACE_KEYWORD, IS_KEYWORD,

		LIMITED_KEYWORD, LOOP_KEYWORD,

		MOD_KEYWORD,

		NEW_KEYWORD, NOT_KEYWORD, NULL_KEYWORD,

		OF_KEYWORD, OR_KEYWORD, OTHERS_KEYWORD, OUT_KEYWORD, OVERRIDING_KEYWORD,

		PACKAGE_KEYWORD, PRAGMA_KEYWORD, PRIVATE_KEYWORD, PROCEDURE_KEYWORD,
		PROTECTED_KEYWORD,

		RAISE_KEYWORD, RANGE_KEYWORD, RECORD_KEYWORD, REM_KEYWORD, RENAMES_KEYWORD,
		REQUEUE_KEYWORD, RETURN_KEYWORD, REVERSE_KEYWORD,

		SELECT_KEYWORD, SEPARATE_KEYWORD, SOME_KEYWORD, SUBTYPE_KEYWORD,
		SYNCHRONIZED_KEYWORD,

		TAGGED_KEYWORD, TASK_KEYWORD, TERMINATE_KEYWORD, THEN_KEYWORD, TYPE_KEYWORD,

		UNTIL_KEYWORD, USE_KEYWORD,

		WHEN_KEYWORD, WHILE_KEYWORD, WITH_KEYWORD,

		XOR_KEYWORD

	);

	/**
	 * Token set representing all valid Ada tokens.
	 */
	public static final TokenSet ALL_VALID_TOKENS = TokenSet.orSet(

		WHITESPACE_TOKEN_SET, COMMENT_TOKEN_SET,

		DELIMITER_TOKEN_SET,

		IDENTIFIER_TOKEN_SET, LITERAL_TOKEN_SET,

		KEYWORD_TOKEN_SET

	);

	/**
	 * Private default constructor to prevent instantiation.
	 */
	private AdaTokenTypes() {}

}
