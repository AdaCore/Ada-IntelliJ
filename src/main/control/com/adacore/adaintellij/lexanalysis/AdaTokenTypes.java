package com.adacore.adaintellij.lexanalysis;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * Token types for Ada 2012.
 */
public final class AdaTokenTypes {
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private AdaTokenTypes() {}
	
	/**
	 * Ada token representing a contiguous whitespace sequence.
	 */
	static final IElementType WHITESPACES          = TokenType.WHITE_SPACE;
	
	/**
	 * Ada token representing a syntactically invalid character.
	 */
	static final IElementType BAD_CHARACTER        = TokenType.BAD_CHARACTER;
	
	/**
	 * Ada tokens representing single-character delimiters.
	 */
	static final IElementType AMPERSAND            = new AdaTokenType("AMPERSAND");            // &
	static final IElementType APOSTROPHE           = new AdaTokenType("APOSTROPHE");           // '
	static final IElementType LEFT_PARENTHESIS     = new AdaTokenType("LEFT_PARENTHESIS");     // (
	static final IElementType RIGHT_PARENTHESIS    = new AdaTokenType("RIGHT_PARENTHESIS");    // )
	static final IElementType ASTERISK             = new AdaTokenType("ASTERISK");             // *
	static final IElementType PLUS_SIGN            = new AdaTokenType("PLUS_SIGN");            // +
	static final IElementType COMMA                = new AdaTokenType("COMMA");                // ,
	static final IElementType HYPHEN_MINUS         = new AdaTokenType("HYPHEN_MINUS");         // -
	static final IElementType FULL_STOP            = new AdaTokenType("FULL_STOP");            // .
	static final IElementType SOLIDUS              = new AdaTokenType("SOLIDUS");              // /
	static final IElementType COLON                = new AdaTokenType("COLON");                // :
	static final IElementType SEMICOLON            = new AdaTokenType("SEMICOLON");            // ;
	static final IElementType LESS_THAN_SIGN       = new AdaTokenType("LESS_THAN_SIGN");       // <
	static final IElementType EQUALS_SIGN          = new AdaTokenType("EQUALS_SIGN");          // =
	static final IElementType GREATER_THAN_SIGN    = new AdaTokenType("GREATER_THAN_SIGN");    // >
	static final IElementType VERTICAL_LINE        = new AdaTokenType("VERTICAL_LINE");        // |
	
	/**
	 * Ada tokens representing compound delimiters.
	 */
	static final IElementType ARROW                = new AdaTokenType("ARROW");                // =>
	static final IElementType DOUBLE_DOT           = new AdaTokenType("DOUBLE_DOT");           // ..
	static final IElementType DOUBLE_ASTERISK      = new AdaTokenType("DOUBLE_ASTERISK");      // **
	static final IElementType ASSIGNMENT           = new AdaTokenType("ASSIGNMENT");           // :=
	static final IElementType NOT_EQUAL_SIGN       = new AdaTokenType("NOT_EQUAL_SIGN");       // /=
	static final IElementType GREATER_EQUAL_SIGN   = new AdaTokenType("GREATER_EQUAL_SIGN");   // >=
	static final IElementType LESS_EQUAL_SIGN      = new AdaTokenType("LESS_EQUAL_SIGN");      // <=
	static final IElementType LEFT_LABEL_BRACKET   = new AdaTokenType("LEFT_LABEL_BRACKET");   // <<
	static final IElementType RIGHT_LABEL_BRACKET  = new AdaTokenType("RIGHT_LABEL_BRACKET");  // >>
	static final IElementType BOX_SIGN             = new AdaTokenType("BOX_SIGN");             // <>
	
	/**
	 * Ada tokens representing identifiers and literals.
	 */
	static final IElementType IDENTIFIER           = new AdaTokenType("IDENTIFIER");           // ident3
	static final IElementType DECIMAL_LITERAL      = new AdaTokenType("DECIMAL_LITERAL");      // 3.14
	static final IElementType BASED_LITERAL        = new AdaTokenType("BASED_LITERAL");        // 16#F8#E1
	static final IElementType CHARACTER_LITERAL    = new AdaTokenType("CHARACTER_LITERAL");    // 'a'
	static final IElementType STRING_LITERAL       = new AdaTokenType("STRING_LITERAL");       // "hello :)"
	
	/**
	 * Ada token representing a single comment.
	 */
	static final IElementType COMMENT              = new AdaTokenType("COMMENT");              // -- Ada comment
	
	/**
	 * Ada tokens representing reserved keywords.
	 */
	static final IElementType ABORT_KEYWORD        = new AdaTokenType("ABORT_KEYWORD");        // abort
	static final IElementType ABS_KEYWORD          = new AdaTokenType("ABS_KEYWORD");          // abs
	static final IElementType ABSTRACT_KEYWORD     = new AdaTokenType("ABSTRACT_KEYWORD");     // abstract
	static final IElementType ACCEPT_KEYWORD       = new AdaTokenType("ACCEPT_KEYWORD");       // accept
	static final IElementType ACCESS_KEYWORD       = new AdaTokenType("ACCESS_KEYWORD");       // access
	static final IElementType ALIASED_KEYWORD      = new AdaTokenType("ALIASED_KEYWORD");      // aliased
	static final IElementType ALL_KEYWORD          = new AdaTokenType("ALL_KEYWORD");          // all
	static final IElementType AND_KEYWORD          = new AdaTokenType("AND_KEYWORD");          // and
	static final IElementType ARRAY_KEYWORD        = new AdaTokenType("ARRAY_KEYWORD");        // array
	static final IElementType AT_KEYWORD           = new AdaTokenType("AT_KEYWORD");           // at
	
	static final IElementType BEGIN_KEYWORD        = new AdaTokenType("BEGIN_KEYWORD");        // begin
	static final IElementType BODY_KEYWORD         = new AdaTokenType("BODY_KEYWORD");         // body
	
	static final IElementType CASE_KEYWORD         = new AdaTokenType("CASE_KEYWORD");         // case
	static final IElementType CONSTANT_KEYWORD     = new AdaTokenType("CONSTANT_KEYWORD");     // constant
	
	static final IElementType DECLARE_KEYWORD      = new AdaTokenType("DECLARE_KEYWORD");      // declare
	static final IElementType DELAY_KEYWORD        = new AdaTokenType("DELAY_KEYWORD");        // delay
	static final IElementType DELTA_KEYWORD        = new AdaTokenType("DELTA_KEYWORD");        // delta
	static final IElementType DIGITS_KEYWORD       = new AdaTokenType("DIGITS_KEYWORD");       // digits
	static final IElementType DO_KEYWORD           = new AdaTokenType("DO_KEYWORD");           // do
	
	static final IElementType ELSE_KEYWORD         = new AdaTokenType("ELSE_KEYWORD");         // else
	static final IElementType ELSIF_KEYWORD        = new AdaTokenType("ELSIF_KEYWORD");        // elsif
	static final IElementType END_KEYWORD          = new AdaTokenType("END_KEYWORD");          // end
	static final IElementType ENTRY_KEYWORD        = new AdaTokenType("ENTRY_KEYWORD");        // entry
	static final IElementType EXCEPTION_KEYWORD    = new AdaTokenType("EXCEPTION_KEYWORD");    // exception
	static final IElementType EXIT_KEYWORD         = new AdaTokenType("EXIT_KEYWORD");         // exit
	
	static final IElementType FOR_KEYWORD          = new AdaTokenType("FOR_KEYWORD");          // for
	static final IElementType FUNCTION_KEYWORD     = new AdaTokenType("FUNCTION_KEYWORD");     // function
	
	static final IElementType GENERIC_KEYWORD      = new AdaTokenType("GENERIC_KEYWORD");      // generic
	static final IElementType GOTO_KEYWORD         = new AdaTokenType("GOTO_KEYWORD");         // goto
	
	static final IElementType IF_KEYWORD           = new AdaTokenType("IF_KEYWORD");           // if
	static final IElementType IN_KEYWORD           = new AdaTokenType("IN_KEYWORD");           // in
	static final IElementType INTERFACE_KEYWORD    = new AdaTokenType("INTERFACE_KEYWORD");    // interface
	static final IElementType IS_KEYWORD           = new AdaTokenType("IS_KEYWORD");           // is
	
	static final IElementType LIMITED_KEYWORD      = new AdaTokenType("LIMITED_KEYWORD");      // limited
	static final IElementType LOOP_KEYWORD         = new AdaTokenType("LOOP_KEYWORD");         // loop
	
	static final IElementType MOD_KEYWORD          = new AdaTokenType("MOD_KEYWORD");          // mod
	
	static final IElementType NEW_KEYWORD          = new AdaTokenType("NEW_KEYWORD");          // new
	static final IElementType NOT_KEYWORD          = new AdaTokenType("NOT_KEYWORD");          // not
	static final IElementType NULL_KEYWORD         = new AdaTokenType("NULL_KEYWORD");         // null
	
	static final IElementType OF_KEYWORD           = new AdaTokenType("OF_KEYWORD");           // of
	static final IElementType OR_KEYWORD           = new AdaTokenType("OR_KEYWORD");           // or
	static final IElementType OTHERS_KEYWORD       = new AdaTokenType("OTHERS_KEYWORD");       // others
	static final IElementType OUT_KEYWORD          = new AdaTokenType("OUT_KEYWORD");          // out
	static final IElementType OVERRIDING_KEYWORD   = new AdaTokenType("OVERRIDING_KEYWORD");   // overriding
	
	static final IElementType PACKAGE_KEYWORD      = new AdaTokenType("PACKAGE_KEYWORD");      // package
	static final IElementType PRAGMA_KEYWORD       = new AdaTokenType("PRAGMA_KEYWORD");       // pragma
	static final IElementType PRIVATE_KEYWORD      = new AdaTokenType("PRIVATE_KEYWORD");      // private
	static final IElementType PROCEDURE_KEYWORD    = new AdaTokenType("PROCEDURE_KEYWORD");    // procedure
	static final IElementType PROTECTED_KEYWORD    = new AdaTokenType("PROTECTED_KEYWORD");    // protected
	
	static final IElementType RAISE_KEYWORD        = new AdaTokenType("RAISE_KEYWORD");        // raise
	static final IElementType RANGE_KEYWORD        = new AdaTokenType("RANGE_KEYWORD");        // range
	static final IElementType RECORD_KEYWORD       = new AdaTokenType("RECORD_KEYWORD");       // record
	static final IElementType REM_KEYWORD          = new AdaTokenType("REM_KEYWORD");          // rem
	static final IElementType RENAMES_KEYWORD      = new AdaTokenType("RENAMES_KEYWORD");      // renames
	static final IElementType REQUEUE_KEYWORD      = new AdaTokenType("REQUEUE_KEYWORD");      // requeue
	static final IElementType RETURN_KEYWORD       = new AdaTokenType("RETURN_KEYWORD");       // return
	static final IElementType REVERSE_KEYWORD      = new AdaTokenType("REVERSE_KEYWORD");      // reverse
	
	static final IElementType SELECT_KEYWORD       = new AdaTokenType("SELECT_KEYWORD");       // select
	static final IElementType SEPARATE_KEYWORD     = new AdaTokenType("SEPARATE_KEYWORD");     // separate
	static final IElementType SOME_KEYWORD         = new AdaTokenType("SOME_KEYWORD");         // some
	static final IElementType SUBTYPE_KEYWORD      = new AdaTokenType("SUBTYPE_KEYWORD");      // subtype
	static final IElementType SYNCHRONIZED_KEYWORD = new AdaTokenType("SYNCHRONIZED_KEYWORD"); // synchronized
	
	static final IElementType TAGGED_KEYWORD       = new AdaTokenType("TAGGED_KEYWORD");       // tagged
	static final IElementType TASK_KEYWORD         = new AdaTokenType("TASK_KEYWORD");         // task
	static final IElementType TERMINATE_KEYWORD    = new AdaTokenType("TERMINATE_KEYWORD");    // terminate
	static final IElementType THEN_KEYWORD         = new AdaTokenType("THEN_KEYWORD");         // then
	static final IElementType TYPE_KEYWORD         = new AdaTokenType("TYPE_KEYWORD");         // type
	
	static final IElementType UNTIL_KEYWORD        = new AdaTokenType("UNTIL_KEYWORD");        // until
	static final IElementType USE_KEYWORD          = new AdaTokenType("USE_KEYWORD");          // use
	
	static final IElementType WHEN_KEYWORD         = new AdaTokenType("WHEN_KEYWORD");         // when
	static final IElementType WHILE_KEYWORD        = new AdaTokenType("WHILE_KEYWORD");        // while
	static final IElementType WITH_KEYWORD         = new AdaTokenType("WITH_KEYWORD");         // with
	
	static final IElementType XOR_KEYWORD          = new AdaTokenType("XOR_KEYWORD");          // xor
	
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
	
}
