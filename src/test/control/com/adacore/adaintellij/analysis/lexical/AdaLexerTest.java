package com.adacore.adaintellij.analysis.lexical;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.Test;

import com.adacore.adaintellij.AdaTestUtils;

import static org.junit.jupiter.api.Assertions.*;

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

		Iterator<AdaLexer.Token> expectedTokens = AdaTokenListParser.parseTokenListFile(tokenListFileURI);
		Iterator<AdaLexer.Token> textTokens     = AdaLexer.textTokens(sourceText);

		// Testing

		while (textTokens.hasNext()) {

			AdaLexer.Token lexerToken =
				textTokens.next();

			if (!expectedTokens.hasNext()) {
				fail("Reached end of token list but lexer generated another token:\n\t" + lexerToken);
			}

			assertEquals(expectedTokens.next(), lexerToken);

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

	// Testing lexing delimiters

	@Test
	void source_file_with_delimiters_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/delimiters.adb").toURI(),
			classObject.getResource("/ada-sources/delimiters.adb.token-list").toURI()
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

	// Testing lexing keywords

	@Test
	void source_file_with_keywords_lexed_correctly() throws Exception {
		assertSourceFileLexedCorrectly(
			classObject.getResource("/ada-sources/keywords.adb").toURI(),
			classObject.getResource("/ada-sources/keywords.adb.token-list").toURI()
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

}
