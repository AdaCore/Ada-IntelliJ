package com.adacore.adaintellij.analysis.lexical;

import java.util.*;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.lexical.regex.*;

import static com.adacore.adaintellij.analysis.lexical.GPRFileTokenTypes.*;

/**
 * Lexical Analyser for GPR files.
 */
public final class GPRFileLexer extends Lexer {
	
	// Delimiters
	
	/**
	 * Unit regexes for matching GPR file delimiters.
	 */
	private static final LexerRegex AMPERSAND_REGEX         = new UnitRegex(AMPERSAND.TOKEN_TEXT);
	private static final LexerRegex LEFT_PARENTHESIS_REGEX  = new UnitRegex(LEFT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex RIGHT_PARENTHESIS_REGEX = new UnitRegex(RIGHT_PARENTHESIS.TOKEN_TEXT);
	private static final LexerRegex COMMA_REGEX             = new UnitRegex(COMMA.TOKEN_TEXT);
	private static final LexerRegex SEMICOLON_REGEX         = new UnitRegex(SEMICOLON.TOKEN_TEXT);
	
	// Keywords
	
	/**
	 * Unit regexes for matching GPR file keywords.
	 */
	private static final LexerRegex ABSTRACT_KEYWORD_REGEX         = new UnitRegex(ABSTRACT_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex ALL_KEYWORD_REGEX              = new UnitRegex(ALL_KEYWORD.TOKEN_TEXT             , 1);
	private static final LexerRegex AT_KEYWORD_REGEX               = new UnitRegex(AT_KEYWORD.TOKEN_TEXT              , 1);
	
	private static final LexerRegex CASE_KEYWORD_REGEX             = new UnitRegex(CASE_KEYWORD.TOKEN_TEXT            , 1);
	
	private static final LexerRegex END_KEYWORD_REGEX              = new UnitRegex(END_KEYWORD.TOKEN_TEXT             , 1);
	private static final LexerRegex EXTENDS_KEYWORD_REGEX          = new UnitRegex(EXTENDS_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex EXTERNAL_KEYWORD_REGEX         = new UnitRegex(EXTERNAL_KEYWORD.TOKEN_TEXT        , 1);
	private static final LexerRegex EXTERNAL_AS_LIST_KEYWORD_REGEX = new UnitRegex(EXTERNAL_AS_LIST_KEYWORD.TOKEN_TEXT, 1);
	
	private static final LexerRegex FOR_KEYWORD_REGEX              = new UnitRegex(FOR_KEYWORD.TOKEN_TEXT             , 1);
	
	private static final LexerRegex IS_KEYWORD_REGEX               = new UnitRegex(IS_KEYWORD.TOKEN_TEXT              , 1);
	
	private static final LexerRegex LIMITED_KEYWORD_REGEX          = new UnitRegex(LIMITED_KEYWORD.TOKEN_TEXT         , 1);
	
	private static final LexerRegex NULL_KEYWORD_REGEX             = new UnitRegex(NULL_KEYWORD.TOKEN_TEXT            , 1);
	
	private static final LexerRegex OTHERS_KEYWORD_REGEX           = new UnitRegex(OTHERS_KEYWORD.TOKEN_TEXT          , 1);
	
	private static final LexerRegex PACKAGE_KEYWORD_REGEX          = new UnitRegex(PACKAGE_KEYWORD.TOKEN_TEXT         , 1);
	private static final LexerRegex PROJECT_KEYWORD_REGEX          = new UnitRegex(PROJECT_KEYWORD.TOKEN_TEXT         , 1);
	
	private static final LexerRegex RENAMES_KEYWORD_REGEX          = new UnitRegex(RENAMES_KEYWORD.TOKEN_TEXT         , 1);
	
	private static final LexerRegex TYPE_KEYWORD_REGEX             = new UnitRegex(TYPE_KEYWORD.TOKEN_TEXT            , 1);
	
	private static final LexerRegex USE_KEYWORD_REGEX              = new UnitRegex(USE_KEYWORD.TOKEN_TEXT             , 1);
	
	private static final LexerRegex WHEN_KEYWORD_REGEX             = new UnitRegex(WHEN_KEYWORD.TOKEN_TEXT            , 1);
	private static final LexerRegex WITH_KEYWORD_REGEX             = new UnitRegex(WITH_KEYWORD.TOKEN_TEXT            , 1);
	
	/**
	 * Unit regexes for matching GPR file qualifiers.
	 */
	private static final LexerRegex AGGREGATE_KEYWORD_REGEX        = new UnitRegex(AGGREGATE_KEYWORD.TOKEN_TEXT       , 1);
	private static final LexerRegex LIBRARY_KEYWORD_REGEX          = new UnitRegex(LIBRARY_KEYWORD.TOKEN_TEXT         , 1);
	
	// Lexer data
	
	/**
	 * A map associating root regexes with the token types
	 * they represent.
	 */
	private static final Map<LexerRegex, IElementType> REGEX_TOKEN_TYPES;
	
	/*
		Static Initializer
	*/
	
	static {
		
		// Populate the regex -> token-type map
		
		Map<LexerRegex, IElementType> regexTokenTypes = new HashMap<>();
		
		regexTokenTypes.put(WHITESPACES_REGEX             , WHITESPACES);
		
		regexTokenTypes.put(AMPERSAND_REGEX               , AMPERSAND);
		regexTokenTypes.put(LEFT_PARENTHESIS_REGEX        , LEFT_PARENTHESIS);
		regexTokenTypes.put(RIGHT_PARENTHESIS_REGEX       , RIGHT_PARENTHESIS);
		regexTokenTypes.put(COMMA_REGEX                   , COMMA);
		regexTokenTypes.put(SEMICOLON_REGEX               , SEMICOLON);
		
		regexTokenTypes.put(IDENTIFIER_REGEX              , IDENTIFIER);
		regexTokenTypes.put(STRING_LITERAL_REGEX          , STRING_LITERAL);
		
		regexTokenTypes.put(COMMENT_REGEX                 , COMMENT);
		
		regexTokenTypes.put(ABSTRACT_KEYWORD_REGEX        , ABSTRACT_KEYWORD);
		regexTokenTypes.put(ALL_KEYWORD_REGEX             , ALL_KEYWORD);
		regexTokenTypes.put(AT_KEYWORD_REGEX              , AT_KEYWORD);
		
		regexTokenTypes.put(CASE_KEYWORD_REGEX            , CASE_KEYWORD);
		
		regexTokenTypes.put(END_KEYWORD_REGEX             , END_KEYWORD);
		regexTokenTypes.put(EXTENDS_KEYWORD_REGEX         , EXTENDS_KEYWORD);
		regexTokenTypes.put(EXTERNAL_KEYWORD_REGEX        , EXTERNAL_KEYWORD);
		regexTokenTypes.put(EXTERNAL_AS_LIST_KEYWORD_REGEX, EXTERNAL_AS_LIST_KEYWORD);
		
		regexTokenTypes.put(FOR_KEYWORD_REGEX             , FOR_KEYWORD);
		
		regexTokenTypes.put(IS_KEYWORD_REGEX              , IS_KEYWORD);
		
		regexTokenTypes.put(LIMITED_KEYWORD_REGEX         , LIMITED_KEYWORD);
		
		regexTokenTypes.put(NULL_KEYWORD_REGEX            , NULL_KEYWORD);
		
		regexTokenTypes.put(OTHERS_KEYWORD_REGEX          , OTHERS_KEYWORD);
		
		regexTokenTypes.put(PACKAGE_KEYWORD_REGEX         , PACKAGE_KEYWORD);
		regexTokenTypes.put(PROJECT_KEYWORD_REGEX         , PROJECT_KEYWORD);
		
		regexTokenTypes.put(RENAMES_KEYWORD_REGEX         , RENAMES_KEYWORD);
		
		regexTokenTypes.put(TYPE_KEYWORD_REGEX            , TYPE_KEYWORD);
		
		regexTokenTypes.put(USE_KEYWORD_REGEX             , USE_KEYWORD);
		
		regexTokenTypes.put(WHEN_KEYWORD_REGEX            , WHEN_KEYWORD);
		regexTokenTypes.put(WITH_KEYWORD_REGEX            , WITH_KEYWORD);
		
		regexTokenTypes.put(AGGREGATE_KEYWORD_REGEX       , AGGREGATE_KEYWORD);
		regexTokenTypes.put(LIBRARY_KEYWORD_REGEX         , LIBRARY_KEYWORD);
		
		REGEX_TOKEN_TYPES = Collections.unmodifiableMap(regexTokenTypes);
		
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
	
}
