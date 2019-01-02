package com.adacore.adaintellij.analysis.lexical;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Syntax highlighter for GPR files.
 */
public final class GPRFileSyntaxHighlighter implements SyntaxHighlighter {
	
	/**
	 * Color attribute keys.
	 */
	private static final TextAttributesKey DELIMITER_COLOR =
		createTextAttributesKey("GPR_FILE_DELIMITER", DefaultLanguageHighlighterColors.OPERATION_SIGN);
	private static final TextAttributesKey IDENTIFIER_COLOR =
		createTextAttributesKey("GPR_FILE_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
	private static final TextAttributesKey STRING_LITERAL_COLOR =
		createTextAttributesKey("GPR_FILE_TEXTUAL_LITERAL", DefaultLanguageHighlighterColors.STRING);
	private static final TextAttributesKey KEYWORD_QUALIFIER_COLOR =
		createTextAttributesKey("GPR_FILE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
	private static final TextAttributesKey COMMENT_COLOR =
		createTextAttributesKey("GPR_FILE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
	private static final TextAttributesKey BAD_CHARACTER_COLOR =
		createTextAttributesKey("GPR_FILE_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
	
	/**
	 * Text attribute key arrays.
	 */
	private static final TextAttributesKey[] DELIMITER_KEYS         = new TextAttributesKey[]{ DELIMITER_COLOR         };
	private static final TextAttributesKey[] IDENTIFIER_KEYS        = new TextAttributesKey[]{ IDENTIFIER_COLOR        };
	private static final TextAttributesKey[] STRING_LITERAL_KEYS    = new TextAttributesKey[]{ STRING_LITERAL_COLOR    };
	private static final TextAttributesKey[] KEYWORD_QUALIFIER_KEYS = new TextAttributesKey[]{ KEYWORD_QUALIFIER_COLOR };
	private static final TextAttributesKey[] COMMENT_KEYS           = new TextAttributesKey[]{ COMMENT_COLOR           };
	private static final TextAttributesKey[] BAD_CHARACTER_KEYS     = new TextAttributesKey[]{ BAD_CHARACTER_COLOR     };
	private static final TextAttributesKey[] EMPTY_KEYS             = new TextAttributesKey[0];
	
	/**
	 * @see com.intellij.openapi.fileTypes.SyntaxHighlighter#getHighlightingLexer()
	 */
	@NotNull
	@Override
	public Lexer getHighlightingLexer() { return new GPRFileLexer(); }
	
	/**
	 * @see com.intellij.openapi.fileTypes.SyntaxHighlighter#getTokenHighlights(IElementType)
	 */
	@NotNull
	@Override
	public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
		
		// Delimiters
		if (GPRFileTokenTypes.DELIMITER_TOKEN_SET.contains(tokenType)) {
			return DELIMITER_KEYS;
		}
		
		// Identifiers
		else if (GPRFileTokenTypes.IDENTIFIER_TOKEN_SET.contains(tokenType)) {
			return IDENTIFIER_KEYS;
		}
		
		// String literals
		else if (GPRFileTokenTypes.STRING_LITERAL_TOKEN_SET.contains(tokenType)) {
			return STRING_LITERAL_KEYS;
		}
		
		// Comments
		else if (GPRFileTokenTypes.COMMENT_TOKEN_SET.contains(tokenType)) {
			return COMMENT_KEYS;
		}
		
		// Keywords
		else if (
			GPRFileTokenTypes.KEYWORD_TOKEN_SET.contains(tokenType) ||
			GPRFileTokenTypes.QUALIFIER_TOKEN_SET.contains(tokenType)
		) {
			return KEYWORD_QUALIFIER_KEYS;
		}
		
		// Invalid tokens
		else if (tokenType == GPRFileTokenTypes.BAD_CHARACTER) { return BAD_CHARACTER_KEYS; }
		
		// Whitespaces
		else { return EMPTY_KEYS; }
		
	}
	
}
