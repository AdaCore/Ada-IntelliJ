package com.adacore.adaintellij.lexanalysis;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * Token types for Ada 2012.
 */
public interface AdaTokenTypes {
	
	/**
	 * Ada token representing a contiguous whitespace sequence.
	 */
	IElementType WHITE_SPACES         = TokenType.WHITE_SPACE;
	
	/**
	 * Ada token representing a syntactically invalid character.
	 */
	IElementType BAD_CHARACTER        = TokenType.BAD_CHARACTER;
	
	/**
	 * Ada tokens representing single-character delimiters.
	 */
	IElementType AMPERSAND            = new AdaTokenType("AMPERSAND");            // &
	IElementType APOSTROPHE           = new AdaTokenType("APOSTROPHE");           // '
	IElementType LEFT_PARENTHESIS     = new AdaTokenType("LEFT_PARENTHESIS");     // (
	IElementType RIGHT_PARENTHESIS    = new AdaTokenType("RIGHT_PARENTHESIS");    // )
	IElementType ASTERISK             = new AdaTokenType("ASTERISK");             // *
	IElementType PLUS_SIGN            = new AdaTokenType("PLUS_SIGN");            // +
	IElementType COMMA                = new AdaTokenType("COMMA");                // ,
	IElementType HYPHEN_MINUS         = new AdaTokenType("HYPHEN_MINUS");         // -
	IElementType FULL_STOP            = new AdaTokenType("FULL_STOP");            // .
	IElementType SOLIDUS              = new AdaTokenType("SOLIDUS");              // /
	IElementType COLON                = new AdaTokenType("COLON");                // :
	IElementType SEMICOLON            = new AdaTokenType("SEMICOLON");            // ;
	IElementType LESS_THAN_SIGN       = new AdaTokenType("LESS_THAN_SIGN");       // <
	IElementType EQUALS_SIGN          = new AdaTokenType("EQUALS_SIGN");          // =
	IElementType GREATER_THAN_SIGN    = new AdaTokenType("GREATER_THAN_SIGN");    // >
	IElementType VERTICAL_LINE        = new AdaTokenType("VERTICAL_LINE");        // |
	
	/**
	 * Ada tokens representing compound delimiters.
	 */
	IElementType ARROW                = new AdaTokenType("ARROW");                // =>
	IElementType DOUBLE_DOT           = new AdaTokenType("DOUBLE_DOT");           // ..
	IElementType DOUBLE_ASTERISK      = new AdaTokenType("DOUBLE_ASTERISK");      // **
	IElementType ASSIGNMENT           = new AdaTokenType("ASSIGNMENT");           // :=
	IElementType NOT_EQUAL_SIGN       = new AdaTokenType("NOT_EQUAL_SIGN");       // /=
	IElementType GREATER_EQUAL_SIGN   = new AdaTokenType("GREATER_EQUAL_SIGN");   // >=
	IElementType LESS_EQUAL_SIGN      = new AdaTokenType("LESS_EQUAL_SIGN");      // <=
	IElementType LEFT_LABEL_BRACKET   = new AdaTokenType("LEFT_LABEL_BRACKET");   // <<
	IElementType RIGHT_LABEL_BRACKET  = new AdaTokenType("RIGHT_LABEL_BRACKET");  // >>
	IElementType BOX_SIGN             = new AdaTokenType("BOX_SIGN");             // <>
	
	/**
	 * Ada tokens representing reserved keywords.
	 */
	IElementType ABORT_KEYWORD        = new AdaTokenType("ABORT_KEYWORD");        // abort
	IElementType ABS_KEYWORD          = new AdaTokenType("ABS_KEYWORD");          // abs
	IElementType ABSTRACT_KEYWORD     = new AdaTokenType("ABSTRACT_KEYWORD");     // abstract
	IElementType ACCEPT_KEYWORD       = new AdaTokenType("ACCEPT_KEYWORD");       // accept
	IElementType ACCESS_KEYWORD       = new AdaTokenType("ACCESS_KEYWORD");       // access
	IElementType ALIASED_KEYWORD      = new AdaTokenType("ALIASED_KEYWORD");      // aliased
	IElementType ALL_KEYWORD          = new AdaTokenType("ALL_KEYWORD");          // all
	IElementType AND_KEYWORD          = new AdaTokenType("AND_KEYWORD");          // and
	IElementType ARRAY_KEYWORD        = new AdaTokenType("ARRAY_KEYWORD");        // array
	IElementType AT_KEYWORD           = new AdaTokenType("AT_KEYWORD");           // at
	
	IElementType BEGIN_KEYWORD        = new AdaTokenType("BEGIN_KEYWORD");        // begin
	IElementType BODY_KEYWORD         = new AdaTokenType("BODY_KEYWORD");         // body
	
	IElementType CASE_KEYWORD         = new AdaTokenType("CASE_KEYWORD");         // case
	IElementType CONSTANT_KEYWORD     = new AdaTokenType("CONSTANT_KEYWORD");     // constant
	
	IElementType DECLARE_KEYWORD      = new AdaTokenType("DECLARE_KEYWORD");      // declare
	IElementType DELAY_KEYWORD        = new AdaTokenType("DELAY_KEYWORD");        // delay
	IElementType DELTA_KEYWORD        = new AdaTokenType("DELTA_KEYWORD");        // delta
	IElementType DIGITS_KEYWORD       = new AdaTokenType("DIGITS_KEYWORD");       // digits
	IElementType DO_KEYWORD           = new AdaTokenType("DO_KEYWORD");           // do
	
	IElementType ELSE_KEYWORD         = new AdaTokenType("ELSE_KEYWORD");         // else
	IElementType ELSIF_KEYWORD        = new AdaTokenType("ELSIF_KEYWORD");        // elsif
	IElementType END_KEYWORD          = new AdaTokenType("END_KEYWORD");          // end
	IElementType ENTRY_KEYWORD        = new AdaTokenType("ENTRY_KEYWORD");        // entry
	IElementType EXCEPTION_KEYWORD    = new AdaTokenType("EXCEPTION_KEYWORD");    // exception
	IElementType EXIT_KEYWORD         = new AdaTokenType("EXIT_KEYWORD");         // exit
	
	IElementType FOR_KEYWORD          = new AdaTokenType("FOR_KEYWORD");          // for
	IElementType FUNCTION_KEYWORD     = new AdaTokenType("FUNCTION_KEYWORD");     // function
	
	IElementType GENERIC_KEYWORD      = new AdaTokenType("GENERIC_KEYWORD");      // generic
	IElementType GOTO_KEYWORD         = new AdaTokenType("GOTO_KEYWORD");         // goto
	
	IElementType IF_KEYWORD           = new AdaTokenType("IF_KEYWORD");           // if
	IElementType IN_KEYWORD           = new AdaTokenType("IN_KEYWORD");           // in
	IElementType INTERFACE_KEYWORD    = new AdaTokenType("INTERFACE_KEYWORD");    // interface
	IElementType IS_KEYWORD           = new AdaTokenType("IS_KEYWORD");           // is
	
	IElementType LIMITED_KEYWORD      = new AdaTokenType("LIMITED_KEYWORD");      // limited
	IElementType LOOP_KEYWORD         = new AdaTokenType("LOOP_KEYWORD");         // loop
	
	IElementType MOD_KEYWORD          = new AdaTokenType("MOD_KEYWORD");          // mod
	
	IElementType NEW_KEYWORD          = new AdaTokenType("NEW_KEYWORD");          // new
	IElementType NOT_KEYWORD          = new AdaTokenType("NOT_KEYWORD");          // not
	IElementType NULL_KEYWORD         = new AdaTokenType("NULL_KEYWORD");         // null
	
	IElementType OF_KEYWORD           = new AdaTokenType("OF_KEYWORD");           // of
	IElementType OR_KEYWORD           = new AdaTokenType("OR_KEYWORD");           // or
	IElementType OTHERS_KEYWORD       = new AdaTokenType("OTHERS_KEYWORD");       // others
	IElementType OUT_KEYWORD          = new AdaTokenType("OUT_KEYWORD");          // out
	IElementType OVERRIDING_KEYWORD   = new AdaTokenType("OVERRIDING_KEYWORD");   // overriding
	
	IElementType PACKAGE_KEYWORD      = new AdaTokenType("PACKAGE_KEYWORD");      // package
	IElementType PRAGMA_KEYWORD       = new AdaTokenType("PRAGMA_KEYWORD");       // pragma
	IElementType PRIVATE_KEYWORD      = new AdaTokenType("PRIVATE_KEYWORD");      // private
	IElementType PROCEDURE_KEYWORD    = new AdaTokenType("PROCEDURE_KEYWORD");    // procedure
	IElementType PROTECTED_KEYWORD    = new AdaTokenType("PROTECTED_KEYWORD");    // protected
	
	IElementType RAISE_KEYWORD        = new AdaTokenType("RAISE_KEYWORD");        // raise
	IElementType RANGE_KEYWORD        = new AdaTokenType("RANGE_KEYWORD");        // range
	IElementType RECORD_KEYWORD       = new AdaTokenType("RECORD_KEYWORD");       // record
	IElementType REM_KEYWORD          = new AdaTokenType("REM_KEYWORD");          // rem
	IElementType RENAMES_KEYWORD      = new AdaTokenType("RENAMES_KEYWORD");      // renames
	IElementType REQUEUE_KEYWORD      = new AdaTokenType("REQUEUE_KEYWORD");      // requeue
	IElementType RETURN_KEYWORD       = new AdaTokenType("RETURN_KEYWORD");       // return
	IElementType REVERSE_KEYWORD      = new AdaTokenType("REVERSE_KEYWORD");      // reverse
	
	IElementType SELECT_KEYWORD       = new AdaTokenType("SELECT_KEYWORD");       // select
	IElementType SEPARATE_KEYWORD     = new AdaTokenType("SEPARATE_KEYWORD");     // separate
	IElementType SOME_KEYWORD         = new AdaTokenType("SOME_KEYWORD");         // some
	IElementType SUBTYPE_KEYWORD      = new AdaTokenType("SUBTYPE_KEYWORD");      // subtype
	IElementType SYNCHRONIZED_KEYWORD = new AdaTokenType("SYNCHRONIZED_KEYWORD"); // synchronized
	
	IElementType TAGGED_KEYWORD       = new AdaTokenType("TAGGED_KEYWORD");       // tagged
	IElementType TASK_KEYWORD         = new AdaTokenType("TASK_KEYWORD");         // task
	IElementType TERMINATE_KEYWORD    = new AdaTokenType("TERMINATE_KEYWORD");    // terminate
	IElementType THEN_KEYWORD         = new AdaTokenType("THEN_KEYWORD");         // then
	IElementType TYPE_KEYWORD         = new AdaTokenType("TYPE_KEYWORD");         // type
	
	IElementType UNTIL_KEYWORD        = new AdaTokenType("UNTIL_KEYWORD");        // until
	IElementType USE_KEYWORD          = new AdaTokenType("USE_KEYWORD");          // use
	
	IElementType WHEN_KEYWORD         = new AdaTokenType("WHEN_KEYWORD");         // when
	IElementType WHILE_KEYWORD        = new AdaTokenType("WHILE_KEYWORD");        // while
	IElementType WITH_KEYWORD         = new AdaTokenType("WITH_KEYWORD");         // with
	
	IElementType XOR_KEYWORD          = new AdaTokenType("XOR_KEYWORD");          // xor
	
	/**
	 * Ada tokens representing identifiers and literals.
	 */
	IElementType IDENTIFIER           = new AdaTokenType("IDENTIFIER");           // ident3
	IElementType DECIMAL_LITERAL      = new AdaTokenType("DECIMAL_LITERAL");      // 3.14
	IElementType BASED_LITERAL        = new AdaTokenType("BASED_LITERAL");        // 16#F8#E1
	IElementType CHARACTER_LITERAL    = new AdaTokenType("CHARACTER_LITERAL");    // 'a'
	IElementType STRING_LITERAL       = new AdaTokenType("STRING_LITERAL");       // "hello :)"
	
	/**
	 * Ada token representing a single comment.
	 */
	IElementType COMMENT              = new AdaTokenType("COMMENT");              // -- Ada comment
	
	/**
	 * Ada token representing a pragma directive.
	 *
	 * TODO: Figure out if this should even be a thing
	 * @see AdaLexer#PRAGMA_REGEX for explanation
	 */
	IElementType PRAGMA               = new AdaTokenType("PRAGMA");               // pragma Optimize(Off);
	
}
