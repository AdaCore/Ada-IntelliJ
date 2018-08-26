package com.adacore.adaintellij.lexanalysis;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Syntax highlighter for Ada 2012.
 *
 * TODO: Rethink attribute/color associations?!
 */
public class AdaSyntaxHighlighter extends SyntaxHighlighterBase {
	
	/**
	 * Color attribute keys.
	 */
	private static final TextAttributesKey DELIMITER_COLOR =
		createTextAttributesKey("ADA_DELIMITER", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	private static final TextAttributesKey IDENTIFIER_COLOR =
		createTextAttributesKey("ADA_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
	private static final TextAttributesKey NUMERIC_LITERAL_COLOR =
		createTextAttributesKey("ADA_NUMERIC_LITERAL", DefaultLanguageHighlighterColors.NUMBER);
	private static final TextAttributesKey TEXTUAL_LITERAL_COLOR =
		createTextAttributesKey("ADA_TEXTUAL_LITERAL", DefaultLanguageHighlighterColors.STRING);
	private static final TextAttributesKey KEYWORD_COLOR =
		createTextAttributesKey("ADA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	private static final TextAttributesKey COMMENT_COLOR =
		createTextAttributesKey("ADA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	private static final TextAttributesKey PRAGMA_COLOR =
		createTextAttributesKey("ADA_PRAGMA", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR); // TODO: Change color here?!
	private static final TextAttributesKey BAD_CHARACTER_COLOR =
		createTextAttributesKey("ADA_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
	
	/**
	 * Text attribute key arrays.
	 */
	private static final TextAttributesKey[] DELIMITER_KEYS       = new TextAttributesKey[]{ DELIMITER_COLOR       };
	private static final TextAttributesKey[] IDENTIFIER_KEYS      = new TextAttributesKey[]{ IDENTIFIER_COLOR      };
	private static final TextAttributesKey[] NUMERIC_LITERAL_KEYS = new TextAttributesKey[]{ NUMERIC_LITERAL_COLOR };
	private static final TextAttributesKey[] TEXTUAL_LITERAL_KEYS = new TextAttributesKey[]{ TEXTUAL_LITERAL_COLOR };
	private static final TextAttributesKey[] KEYWORD_KEYS         = new TextAttributesKey[]{ KEYWORD_COLOR         };
	private static final TextAttributesKey[] COMMENT_KEYS         = new TextAttributesKey[]{ COMMENT_COLOR         };
	private static final TextAttributesKey[] PRAGMA_KEYS          = new TextAttributesKey[]{ PRAGMA_COLOR          };
	private static final TextAttributesKey[] BAD_CHARACTER_KEYS   = new TextAttributesKey[]{ BAD_CHARACTER_COLOR   };
	private static final TextAttributesKey[] EMPTY_KEYS           = new TextAttributesKey[0];
	
	/**
	 * @see com.intellij.openapi.fileTypes.SyntaxHighlighter#getHighlightingLexer()
	 */
	@NotNull
	@Override
	public Lexer getHighlightingLexer() { return new AdaLexer(); }
	
	/**
	 * @see com.intellij.openapi.fileTypes.SyntaxHighlighter#getTokenHighlights(IElementType)
	 */
	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		
		// Single-character and compound delimiters
		if (
			
			tokenType == AdaTokenTypes.AMPERSAND           ||
			tokenType == AdaTokenTypes.APOSTROPHE          ||
			tokenType == AdaTokenTypes.LEFT_PARENTHESIS    ||
			tokenType == AdaTokenTypes.RIGHT_PARENTHESIS   ||
			tokenType == AdaTokenTypes.ASTERISK            ||
			tokenType == AdaTokenTypes.PLUS_SIGN           ||
			tokenType == AdaTokenTypes.COMMA               ||
			tokenType == AdaTokenTypes.HYPHEN_MINUS        ||
			tokenType == AdaTokenTypes.FULL_STOP           ||
			tokenType == AdaTokenTypes.SOLIDUS             ||
			tokenType == AdaTokenTypes.COLON               ||
			tokenType == AdaTokenTypes.SEMICOLON           ||
			tokenType == AdaTokenTypes.LESS_THAN_SIGN      ||
			tokenType == AdaTokenTypes.EQUALS_SIGN         ||
			tokenType == AdaTokenTypes.GREATER_THAN_SIGN   ||
			tokenType == AdaTokenTypes.VERTICAL_LINE       ||
			
			tokenType == AdaTokenTypes.ARROW               ||
			tokenType == AdaTokenTypes.DOUBLE_DOT          ||
			tokenType == AdaTokenTypes.DOUBLE_ASTERISK     ||
			tokenType == AdaTokenTypes.ASSIGNMENT          ||
			tokenType == AdaTokenTypes.NOT_EQUAL_SIGN      ||
			tokenType == AdaTokenTypes.GREATER_EQUAL_SIGN  ||
			tokenType == AdaTokenTypes.LESS_EQUAL_SIGN     ||
			tokenType == AdaTokenTypes.LEFT_LABEL_BRACKET  ||
			tokenType == AdaTokenTypes.RIGHT_LABEL_BRACKET ||
			tokenType == AdaTokenTypes.BOX_SIGN
			
		) { return DELIMITER_KEYS; }
		
		// Identifiers
		else if (tokenType == AdaTokenTypes.IDENTIFIER) { return IDENTIFIER_KEYS; }
		
		// Numeric literals
		else if (
			
			tokenType == AdaTokenTypes.DECIMAL_LITERAL ||
			tokenType == AdaTokenTypes.BASED_LITERAL
			
		) { return NUMERIC_LITERAL_KEYS; }
		
		// Textual literals
		else if (
			
			tokenType == AdaTokenTypes.CHARACTER_LITERAL ||
			tokenType == AdaTokenTypes.STRING_LITERAL
			
		) { return TEXTUAL_LITERAL_KEYS; }
		
		// Comments
		else if (tokenType == AdaTokenTypes.COMMENT) { return COMMENT_KEYS; }
		
		// Keywords
		else if (
			
			tokenType == AdaTokenTypes.ABORT_KEYWORD        ||
			tokenType == AdaTokenTypes.ABS_KEYWORD          ||
			tokenType == AdaTokenTypes.ABSTRACT_KEYWORD     ||
			tokenType == AdaTokenTypes.ACCEPT_KEYWORD       ||
			tokenType == AdaTokenTypes.ACCESS_KEYWORD       ||
			tokenType == AdaTokenTypes.ALIASED_KEYWORD      ||
			tokenType == AdaTokenTypes.ALL_KEYWORD          ||
			tokenType == AdaTokenTypes.AND_KEYWORD          ||
			tokenType == AdaTokenTypes.ARRAY_KEYWORD        ||
			tokenType == AdaTokenTypes.AT_KEYWORD           ||
			
			tokenType == AdaTokenTypes.BEGIN_KEYWORD        ||
			tokenType == AdaTokenTypes.BODY_KEYWORD         ||
			
			tokenType == AdaTokenTypes.CASE_KEYWORD         ||
			tokenType == AdaTokenTypes.CONSTANT_KEYWORD     ||
			
			tokenType == AdaTokenTypes.DECLARE_KEYWORD      ||
			tokenType == AdaTokenTypes.DELAY_KEYWORD        ||
			tokenType == AdaTokenTypes.DELTA_KEYWORD        ||
			tokenType == AdaTokenTypes.DIGITS_KEYWORD       ||
			tokenType == AdaTokenTypes.DO_KEYWORD           ||
			
			tokenType == AdaTokenTypes.ELSE_KEYWORD         ||
			tokenType == AdaTokenTypes.ELSIF_KEYWORD        ||
			tokenType == AdaTokenTypes.END_KEYWORD          ||
			tokenType == AdaTokenTypes.ENTRY_KEYWORD        ||
			tokenType == AdaTokenTypes.EXCEPTION_KEYWORD    ||
			tokenType == AdaTokenTypes.EXIT_KEYWORD         ||
			
			tokenType == AdaTokenTypes.FOR_KEYWORD          ||
			tokenType == AdaTokenTypes.FUNCTION_KEYWORD     ||
			
			tokenType == AdaTokenTypes.GENERIC_KEYWORD      ||
			tokenType == AdaTokenTypes.GOTO_KEYWORD         ||
			
			tokenType == AdaTokenTypes.IF_KEYWORD           ||
			tokenType == AdaTokenTypes.IN_KEYWORD           ||
			tokenType == AdaTokenTypes.INTERFACE_KEYWORD    ||
			tokenType == AdaTokenTypes.IS_KEYWORD           ||
			
			tokenType == AdaTokenTypes.LIMITED_KEYWORD      ||
			tokenType == AdaTokenTypes.LOOP_KEYWORD         ||
			
			tokenType == AdaTokenTypes.MOD_KEYWORD          ||
			
			tokenType == AdaTokenTypes.NEW_KEYWORD          ||
			tokenType == AdaTokenTypes.NOT_KEYWORD          ||
			tokenType == AdaTokenTypes.NULL_KEYWORD         ||
			
			tokenType == AdaTokenTypes.OF_KEYWORD           ||
			tokenType == AdaTokenTypes.OR_KEYWORD           ||
			tokenType == AdaTokenTypes.OTHERS_KEYWORD       ||
			tokenType == AdaTokenTypes.OUT_KEYWORD          ||
			tokenType == AdaTokenTypes.OVERRIDING_KEYWORD   ||
			
			tokenType == AdaTokenTypes.PACKAGE_KEYWORD      ||
			tokenType == AdaTokenTypes.PRAGMA_KEYWORD       ||
			tokenType == AdaTokenTypes.PRIVATE_KEYWORD      ||
			tokenType == AdaTokenTypes.PROCEDURE_KEYWORD    ||
			tokenType == AdaTokenTypes.PROTECTED_KEYWORD    ||
			
			tokenType == AdaTokenTypes.RAISE_KEYWORD        ||
			tokenType == AdaTokenTypes.RANGE_KEYWORD        ||
			tokenType == AdaTokenTypes.RECORD_KEYWORD       ||
			tokenType == AdaTokenTypes.REM_KEYWORD          ||
			tokenType == AdaTokenTypes.RENAMES_KEYWORD      ||
			tokenType == AdaTokenTypes.REQUEUE_KEYWORD      ||
			tokenType == AdaTokenTypes.RETURN_KEYWORD       ||
			tokenType == AdaTokenTypes.REVERSE_KEYWORD      ||
			
			tokenType == AdaTokenTypes.SELECT_KEYWORD       ||
			tokenType == AdaTokenTypes.SEPARATE_KEYWORD     ||
			tokenType == AdaTokenTypes.SOME_KEYWORD         ||
			tokenType == AdaTokenTypes.SUBTYPE_KEYWORD      ||
			tokenType == AdaTokenTypes.SYNCHRONIZED_KEYWORD ||
			
			tokenType == AdaTokenTypes.TAGGED_KEYWORD       ||
			tokenType == AdaTokenTypes.TASK_KEYWORD         ||
			tokenType == AdaTokenTypes.TERMINATE_KEYWORD    ||
			tokenType == AdaTokenTypes.THEN_KEYWORD         ||
			tokenType == AdaTokenTypes.TYPE_KEYWORD         ||
			
			tokenType == AdaTokenTypes.UNTIL_KEYWORD        ||
			tokenType == AdaTokenTypes.USE_KEYWORD          ||
			
			tokenType == AdaTokenTypes.WHEN_KEYWORD         ||
			tokenType == AdaTokenTypes.WHILE_KEYWORD        ||
			tokenType == AdaTokenTypes.WITH_KEYWORD         ||
			
			tokenType == AdaTokenTypes.XOR_KEYWORD
			
		) { return KEYWORD_KEYS; }
		
		// Pragmas
		else if (tokenType == AdaTokenTypes.PRAGMA) { return PRAGMA_KEYS; }
		
		// Invalid tokens
		else if (tokenType == AdaTokenTypes.BAD_CHARACTER) { return BAD_CHARACTER_KEYS; }
		
		// Whitespaces
		else { return EMPTY_KEYS; }
		
	}
	
}
