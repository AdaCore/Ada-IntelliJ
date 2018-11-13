package com.adacore.adaintellij.analysis.lexical;

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
 */
public final class AdaSyntaxHighlighter extends SyntaxHighlighterBase {
	
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
		if (AdaTokenTypes.DELIMITER_TOKEN_SET.contains(tokenType)) {
			return DELIMITER_KEYS;
		}
		
		// Identifiers
		else if (AdaTokenTypes.IDENTIFIER_TOKEN_SET.contains(tokenType)) {
			return IDENTIFIER_KEYS;
		}
		
		// Numeric literals
		else if (AdaTokenTypes.NUMERIC_LITERAL_TOKEN_SET.contains(tokenType)) {
			return NUMERIC_LITERAL_KEYS;
		}
		
		// Textual literals
		else if (AdaTokenTypes.TEXTUAl_LITERAL_TOKEN_SET.contains(tokenType)) {
			return TEXTUAL_LITERAL_KEYS;
		}
		
		// Comments
		else if (AdaTokenTypes.COMMENT_TOKEN_SET.contains(tokenType)) {
			return COMMENT_KEYS;
		}
		
		// Keywords
		else if (AdaTokenTypes.KEYWORD_TOKEN_SET.contains(tokenType)) {
			return KEYWORD_KEYS;
		}
		
		// Invalid tokens
		else if (tokenType == AdaTokenTypes.BAD_CHARACTER) { return BAD_CHARACTER_KEYS; }
		
		// Whitespaces
		else { return EMPTY_KEYS; }
		
	}
	
}
