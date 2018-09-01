package com.adacore.adaintellij.lexanalysis;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.*;

import com.intellij.lexer.Lexer;
import com.intellij.psi.tree.IElementType;

import com.adacore.adaintellij.AdaTestUtils;
import com.adacore.adaintellij.lexanalysis.AdaTokenListParser.TokenData;

import org.junit.jupiter.api.Test;

/**
 * JUnit test class for the AdaLexer class.
 */
final class AdaLexerTest {
	
	private Class classObject = getClass();
	
	/**
	 * Asserts that the token sequence returned by an AdaLexer analysing
	 * the Ada source file at the given URI is identical to the one
	 * described by the token list file at the given URI.
	 *
	 * @param sourceFileURI The URI of the Ada source file.
	 * @param tokenListFileURI The URI of the token list file.
	 * @throws Exception If a problem occurs while reading files, or while
	 *                   parsing the token list file.
	 */
	private static void assertSourceFileLexedCorrectly(
		URI sourceFileURI,
		URI tokenListFileURI
	) throws Exception {
		
		// Initialization
		
		String sourceText = AdaTestUtils.getFileText(sourceFileURI);
		
		Iterator<TokenData> expectedTokens = AdaTokenListParser.parseTokenListFile(tokenListFileURI);
		
		Lexer lexer = new AdaLexer();
		
		// Testing
		
		lexer.start(sourceText, 0, sourceText.length(), 0);
		
		IElementType tokenType = lexer.getTokenType();
		
		while (tokenType != null) {
			
			TokenData lexerTokenData =
				new TokenData(tokenType.toString(), lexer.getTokenStart(), lexer.getTokenEnd());
			
			if (!expectedTokens.hasNext()) {
				fail("Reached end of token list but lexer generated another token:\n\t" + lexerTokenData);
			}
			
			assertEquals(expectedTokens.next(), lexerTokenData);
			
			lexer.advance();
			
			tokenType = lexer.getTokenType();
			
		}
		
		if (expectedTokens.hasNext()) {
			fail("Lexer finished generating tokens but token list" +
				" contains more tokens:\n\t" + expectedTokens.next());
		}
		
	}
	
	// Testing lexing an empty source file
	
	@Test
	void empty_source_file_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/empty.adb").toURI(),
			classObject.getResource("/ada-sources/empty.adb.token-list").toURI()
		);
	}
	
	// Testing lexing hello-world programs
	
	@Test
	void hello_world_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/hello-world.adb").toURI(),
			classObject.getResource("/ada-sources/hello-world.adb.token-list").toURI()
		);
	}
	
	@Test
	void hello_world_mixed_case_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/hello-world-mixed-case.adb").toURI(),
			classObject.getResource("/ada-sources/hello-world.adb.token-list").toURI()
		);
	}
	
	// Testing lexing comments
	
	@Test
	void source_file_with_comments_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/code-with-comments.adb").toURI(),
			classObject.getResource("/ada-sources/code-with-comments.adb.token-list").toURI()
		);
	}
	
	// Testing lexing literals
	
	@Test
	void source_file_with_literals_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/literals.adb").toURI(),
			classObject.getResource("/ada-sources/literals.adb.token-list").toURI()
		);
	}
	
	// Testing lexing bad syntax
	
	@Test
	void source_file_with_bad_syntax_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/bad-syntax.adb").toURI(),
			classObject.getResource("/ada-sources/bad-syntax.adb.token-list").toURI()
		);
	}
	
}
