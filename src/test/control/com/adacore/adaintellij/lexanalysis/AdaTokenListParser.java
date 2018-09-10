package com.adacore.adaintellij.lexanalysis;

import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.adacore.adaintellij.AdaTestUtils;

/**
 * Parser for token list files used for testing.
 * A token list file specifies the list of tokens that constitute an
 * Ada source file, in the order they must be returned by an Ada lexer,
 * including the start and end offset of each token in the source file.
 * The general format of token files is:
 *
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 * TOKEN_1_DEBUG_NAME TOKEN_2_START_OFFSET TOKEN_3_END_OFFSET
 *                             .
 *                             .
 *                             .
 *
 * For possible token debug names:
 * @see com.adacore.adaintellij.lexanalysis.AdaTokenTypes
 *
 * A token list files can also contains comments starting with "--",
 * either on a separate line or at the end of a standard line.
 * More formally, a token list file has the following grammar:
 *
 * token_list_file ::= {token_list_line | line_feed_character}
 *
 * token_list_line ::= [no_whitespace_string integer integer] [comment]
 *
 * integer ::= [digit] {digit}
 *
 * digit ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
 *
 * comment ::= --{no_whitespace_string}
 *
 * Notes:
 * - line_feed_character is the character with code point \u000a
 * - no_whitespace_string can be any string of characters not separated
 *   by spaces or line feeds
 */
final class AdaTokenListParser {
	
	/**
	 * A single token data object, storing a token's debug name,
	 * start and end offsets in a specific Ada source file.
	 */
	static class TokenData {
		
		/**
		 * The debug name, start and end offsets of the token.
		 */
		final String TOKEN_TYPE_DEBUG_NAME;
		final int    TOKEN_START;
		final int    TOKEN_END;
		
		/**
		 * Constructs a new token data object given a set of data.
		 *
		 * @param tokenTypeDebugName The debug name of the token.
		 * @param tokenStart The start offset of the token.
		 * @param tokenEnd The end offset of the token.
		 */
		TokenData(@NotNull String tokenTypeDebugName, int tokenStart, int tokenEnd) {
			TOKEN_TYPE_DEBUG_NAME = tokenTypeDebugName;
			TOKEN_START           = tokenStart;
			TOKEN_END             = tokenEnd;
		}
		
		/**
		 * Returns whether or not this token data object is equal
		 * to the given object.
		 *
		 * @param object The object to compare to this object.
		 * @return The result of the comparison.
		 */
		@Override
		public boolean equals(Object object) {
			
			if (!(object instanceof TokenData)) { return false; }
			else {
				
				TokenData tokenData = (TokenData)object;
				
				return tokenData.TOKEN_TYPE_DEBUG_NAME.equals(TOKEN_TYPE_DEBUG_NAME) &&
					tokenData.TOKEN_START == TOKEN_START && tokenData.TOKEN_END == TOKEN_END;
				
			}
			
		}
		
		/**
		 * Returns a string representation of this token data
		 * object.
		 *
		 * @return A string representation of this object.
		 */
		@Override
		public String toString() {
			return "TokenData(" + TOKEN_TYPE_DEBUG_NAME + ", " + TOKEN_START + ", " + TOKEN_END + ")";
		}
		
	}
	
	/**
	 * Parses the token list file at the given URI and returns
	 * the list of tokens described by the list file as an iterator
	 * over token data objects.
	 *
	 * @param fileURI The URI of the token list file.
	 * @return An iterator over the tokens.
	 * @throws Exception If a problem occurs while reading the file
	 *                   or if its syntax is invalid.
	 */
	static Iterator<TokenData> parseTokenListFile(URI fileURI) throws Exception {
		
		String tokenListText = AdaTestUtils.getFileText(fileURI);
		
		String[] lines = tokenListText.split("\n");
		
		List<TokenData> expectedTokens = new LinkedList<>();
		
		for (int i = 0 ; i < lines.length ; i++) {
			
			String line = lines[i];
			
			String[] lineComponents = line.split(" +");
			int lineComponentsSize = 0;
			
			for (String component : lineComponents) {
				if (component.startsWith("--")) { break; }
				lineComponentsSize++;
			}
			
			if (line.length() == 0 || lineComponentsSize == 0) { continue; }
			else if (lineComponentsSize != 3) {
				throw new Exception("Invalid token list file: line " + (i + 1) +
					" does not have exactly 3 components.");
			}
			
			String tokenName = lineComponents[0];
			int tokenStart, tokenEnd;
			
			try {
				
				tokenStart = Integer.parseInt(lineComponents[1]);
				tokenEnd   = Integer.parseInt(lineComponents[2]);
				
			} catch (NumberFormatException exception) {
				
				throw new Exception("Invalid token list file: line " + (i + 1) +
					" contains a number that is not parsable as an integer.");
				
			}
			
			expectedTokens.add(new TokenData(tokenName, tokenStart, tokenEnd));
			
		}
		
		return expectedTokens.iterator();
		
	}

}
