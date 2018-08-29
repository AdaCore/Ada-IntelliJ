package com.adacore.adaintellij.lexanalysis;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

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
	public static final IElementType WHITESPACES          = TokenType.WHITE_SPACE;
	
	/**
	 * Ada token representing a syntactically invalid character.
	 */
	public static final IElementType BAD_CHARACTER        = TokenType.BAD_CHARACTER;
	
	/**
	 * Ada tokens representing single-character delimiters.
	 */
	public static final IElementType AMPERSAND            = new AdaTokenType("AMPERSAND");            // &
	public static final IElementType APOSTROPHE           = new AdaTokenType("APOSTROPHE");           // '
	public static final IElementType LEFT_PARENTHESIS     = new AdaTokenType("LEFT_PARENTHESIS");     // (
	public static final IElementType RIGHT_PARENTHESIS    = new AdaTokenType("RIGHT_PARENTHESIS");    // )
	public static final IElementType ASTERISK             = new AdaTokenType("ASTERISK");             // *
	public static final IElementType PLUS_SIGN            = new AdaTokenType("PLUS_SIGN");            // +
	public static final IElementType COMMA                = new AdaTokenType("COMMA");                // ,
	public static final IElementType HYPHEN_MINUS         = new AdaTokenType("HYPHEN_MINUS");         // -
	public static final IElementType FULL_STOP            = new AdaTokenType("FULL_STOP");            // .
	public static final IElementType SOLIDUS              = new AdaTokenType("SOLIDUS");              // /
	public static final IElementType COLON                = new AdaTokenType("COLON");                // :
	public static final IElementType SEMICOLON            = new AdaTokenType("SEMICOLON");            // ;
	public static final IElementType LESS_THAN_SIGN       = new AdaTokenType("LESS_THAN_SIGN");       // <
	public static final IElementType EQUALS_SIGN          = new AdaTokenType("EQUALS_SIGN");          // =
	public static final IElementType GREATER_THAN_SIGN    = new AdaTokenType("GREATER_THAN_SIGN");    // >
	public static final IElementType VERTICAL_LINE        = new AdaTokenType("VERTICAL_LINE");        // |
	
	/**
	 * Ada tokens representing compound delimiters.
	 */
	public static final IElementType ARROW                = new AdaTokenType("ARROW");                // =>
	public static final IElementType DOUBLE_DOT           = new AdaTokenType("DOUBLE_DOT");           // ..
	public static final IElementType DOUBLE_ASTERISK      = new AdaTokenType("DOUBLE_ASTERISK");      // **
	public static final IElementType ASSIGNMENT           = new AdaTokenType("ASSIGNMENT");           // :=
	public static final IElementType NOT_EQUAL_SIGN       = new AdaTokenType("NOT_EQUAL_SIGN");       // /=
	public static final IElementType GREATER_EQUAL_SIGN   = new AdaTokenType("GREATER_EQUAL_SIGN");   // >=
	public static final IElementType LESS_EQUAL_SIGN      = new AdaTokenType("LESS_EQUAL_SIGN");      // <=
	public static final IElementType LEFT_LABEL_BRACKET   = new AdaTokenType("LEFT_LABEL_BRACKET");   // <<
	public static final IElementType RIGHT_LABEL_BRACKET  = new AdaTokenType("RIGHT_LABEL_BRACKET");  // >>
	public static final IElementType BOX_SIGN             = new AdaTokenType("BOX_SIGN");             // <>
	
	/**
	 * Ada tokens representing identifiers and literals.
	 */
	public static final IElementType IDENTIFIER           = new AdaTokenType("IDENTIFIER");           // ident3
	public static final IElementType DECIMAL_LITERAL      = new AdaTokenType("DECIMAL_LITERAL");      // 3.14
	public static final IElementType BASED_LITERAL        = new AdaTokenType("BASED_LITERAL");        // 16#F8#E1
	public static final IElementType CHARACTER_LITERAL    = new AdaTokenType("CHARACTER_LITERAL");    // 'a'
	public static final IElementType STRING_LITERAL       = new AdaTokenType("STRING_LITERAL");       // "hello :)"
	
	/**
	 * Ada token representing a single comment.
	 */
	public static final IElementType COMMENT              = new AdaTokenType("COMMENT");              // -- Ada comment
	
	/**
	 * Ada token representing a pragma directive.
	 *
	 * TODO: Figure out if this should even be a thing
	 * @see AdaLexer#PRAGMA_REGEX for explanation
	 */
	public static final IElementType PRAGMA               = new AdaTokenType("PRAGMA");               // pragma Optimize(Off);
	
	/**
	 * Ada tokens representing reserved keywords.
	 */
	public static final IElementType ABORT_KEYWORD        = new AdaTokenType("ABORT_KEYWORD");        // abort
	public static final IElementType ABS_KEYWORD          = new AdaTokenType("ABS_KEYWORD");          // abs
	public static final IElementType ABSTRACT_KEYWORD     = new AdaTokenType("ABSTRACT_KEYWORD");     // abstract
	public static final IElementType ACCEPT_KEYWORD       = new AdaTokenType("ACCEPT_KEYWORD");       // accept
	public static final IElementType ACCESS_KEYWORD       = new AdaTokenType("ACCESS_KEYWORD");       // access
	public static final IElementType ALIASED_KEYWORD      = new AdaTokenType("ALIASED_KEYWORD");      // aliased
	public static final IElementType ALL_KEYWORD          = new AdaTokenType("ALL_KEYWORD");          // all
	public static final IElementType AND_KEYWORD          = new AdaTokenType("AND_KEYWORD");          // and
	public static final IElementType ARRAY_KEYWORD        = new AdaTokenType("ARRAY_KEYWORD");        // array
	public static final IElementType AT_KEYWORD           = new AdaTokenType("AT_KEYWORD");           // at
	
	public static final IElementType BEGIN_KEYWORD        = new AdaTokenType("BEGIN_KEYWORD");        // begin
	public static final IElementType BODY_KEYWORD         = new AdaTokenType("BODY_KEYWORD");         // body
	
	public static final IElementType CASE_KEYWORD         = new AdaTokenType("CASE_KEYWORD");         // case
	public static final IElementType CONSTANT_KEYWORD     = new AdaTokenType("CONSTANT_KEYWORD");     // constant
	
	public static final IElementType DECLARE_KEYWORD      = new AdaTokenType("DECLARE_KEYWORD");      // declare
	public static final IElementType DELAY_KEYWORD        = new AdaTokenType("DELAY_KEYWORD");        // delay
	public static final IElementType DELTA_KEYWORD        = new AdaTokenType("DELTA_KEYWORD");        // delta
	public static final IElementType DIGITS_KEYWORD       = new AdaTokenType("DIGITS_KEYWORD");       // digits
	public static final IElementType DO_KEYWORD           = new AdaTokenType("DO_KEYWORD");           // do
	
	public static final IElementType ELSE_KEYWORD         = new AdaTokenType("ELSE_KEYWORD");         // else
	public static final IElementType ELSIF_KEYWORD        = new AdaTokenType("ELSIF_KEYWORD");        // elsif
	public static final IElementType END_KEYWORD          = new AdaTokenType("END_KEYWORD");          // end
	public static final IElementType ENTRY_KEYWORD        = new AdaTokenType("ENTRY_KEYWORD");        // entry
	public static final IElementType EXCEPTION_KEYWORD    = new AdaTokenType("EXCEPTION_KEYWORD");    // exception
	public static final IElementType EXIT_KEYWORD         = new AdaTokenType("EXIT_KEYWORD");         // exit
	
	public static final IElementType FOR_KEYWORD          = new AdaTokenType("FOR_KEYWORD");          // for
	public static final IElementType FUNCTION_KEYWORD     = new AdaTokenType("FUNCTION_KEYWORD");     // function
	
	public static final IElementType GENERIC_KEYWORD      = new AdaTokenType("GENERIC_KEYWORD");      // generic
	public static final IElementType GOTO_KEYWORD         = new AdaTokenType("GOTO_KEYWORD");         // goto
	
	public static final IElementType IF_KEYWORD           = new AdaTokenType("IF_KEYWORD");           // if
	public static final IElementType IN_KEYWORD           = new AdaTokenType("IN_KEYWORD");           // in
	public static final IElementType INTERFACE_KEYWORD    = new AdaTokenType("INTERFACE_KEYWORD");    // interface
	public static final IElementType IS_KEYWORD           = new AdaTokenType("IS_KEYWORD");           // is
	
	public static final IElementType LIMITED_KEYWORD      = new AdaTokenType("LIMITED_KEYWORD");      // limited
	public static final IElementType LOOP_KEYWORD         = new AdaTokenType("LOOP_KEYWORD");         // loop
	
	public static final IElementType MOD_KEYWORD          = new AdaTokenType("MOD_KEYWORD");          // mod
	
	public static final IElementType NEW_KEYWORD          = new AdaTokenType("NEW_KEYWORD");          // new
	public static final IElementType NOT_KEYWORD          = new AdaTokenType("NOT_KEYWORD");          // not
	public static final IElementType NULL_KEYWORD         = new AdaTokenType("NULL_KEYWORD");         // null
	
	public static final IElementType OF_KEYWORD           = new AdaTokenType("OF_KEYWORD");           // of
	public static final IElementType OR_KEYWORD           = new AdaTokenType("OR_KEYWORD");           // or
	public static final IElementType OTHERS_KEYWORD       = new AdaTokenType("OTHERS_KEYWORD");       // others
	public static final IElementType OUT_KEYWORD          = new AdaTokenType("OUT_KEYWORD");          // out
	public static final IElementType OVERRIDING_KEYWORD   = new AdaTokenType("OVERRIDING_KEYWORD");   // overriding
	
	public static final IElementType PACKAGE_KEYWORD      = new AdaTokenType("PACKAGE_KEYWORD");      // package
	public static final IElementType PRAGMA_KEYWORD       = new AdaTokenType("PRAGMA_KEYWORD");       // pragma
	public static final IElementType PRIVATE_KEYWORD      = new AdaTokenType("PRIVATE_KEYWORD");      // private
	public static final IElementType PROCEDURE_KEYWORD    = new AdaTokenType("PROCEDURE_KEYWORD");    // procedure
	public static final IElementType PROTECTED_KEYWORD    = new AdaTokenType("PROTECTED_KEYWORD");    // protected
	
	public static final IElementType RAISE_KEYWORD        = new AdaTokenType("RAISE_KEYWORD");        // raise
	public static final IElementType RANGE_KEYWORD        = new AdaTokenType("RANGE_KEYWORD");        // range
	public static final IElementType RECORD_KEYWORD       = new AdaTokenType("RECORD_KEYWORD");       // record
	public static final IElementType REM_KEYWORD          = new AdaTokenType("REM_KEYWORD");          // rem
	public static final IElementType RENAMES_KEYWORD      = new AdaTokenType("RENAMES_KEYWORD");      // renames
	public static final IElementType REQUEUE_KEYWORD      = new AdaTokenType("REQUEUE_KEYWORD");      // requeue
	public static final IElementType RETURN_KEYWORD       = new AdaTokenType("RETURN_KEYWORD");       // return
	public static final IElementType REVERSE_KEYWORD      = new AdaTokenType("REVERSE_KEYWORD");      // reverse
	
	public static final IElementType SELECT_KEYWORD       = new AdaTokenType("SELECT_KEYWORD");       // select
	public static final IElementType SEPARATE_KEYWORD     = new AdaTokenType("SEPARATE_KEYWORD");     // separate
	public static final IElementType SOME_KEYWORD         = new AdaTokenType("SOME_KEYWORD");         // some
	public static final IElementType SUBTYPE_KEYWORD      = new AdaTokenType("SUBTYPE_KEYWORD");      // subtype
	public static final IElementType SYNCHRONIZED_KEYWORD = new AdaTokenType("SYNCHRONIZED_KEYWORD"); // synchronized
	
	public static final IElementType TAGGED_KEYWORD       = new AdaTokenType("TAGGED_KEYWORD");       // tagged
	public static final IElementType TASK_KEYWORD         = new AdaTokenType("TASK_KEYWORD");         // task
	public static final IElementType TERMINATE_KEYWORD    = new AdaTokenType("TERMINATE_KEYWORD");    // terminate
	public static final IElementType THEN_KEYWORD         = new AdaTokenType("THEN_KEYWORD");         // then
	public static final IElementType TYPE_KEYWORD         = new AdaTokenType("TYPE_KEYWORD");         // type
	
	public static final IElementType UNTIL_KEYWORD        = new AdaTokenType("UNTIL_KEYWORD");        // until
	public static final IElementType USE_KEYWORD          = new AdaTokenType("USE_KEYWORD");          // use
	
	public static final IElementType WHEN_KEYWORD         = new AdaTokenType("WHEN_KEYWORD");         // when
	public static final IElementType WHILE_KEYWORD        = new AdaTokenType("WHILE_KEYWORD");        // while
	public static final IElementType WITH_KEYWORD         = new AdaTokenType("WITH_KEYWORD");         // with
	
	public static final IElementType XOR_KEYWORD          = new AdaTokenType("XOR_KEYWORD");          // xor
	
}
