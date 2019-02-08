package com.adacore.adaintellij.analysis.lexical;

import java.util.*;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.lexical.regex.*;

/**
 * Base lexical analyser for Ada and related languages.
 */
abstract class Lexer extends LexerBase {

	/*
		Constants
	*/

	// Whitespaces

	/**
	 * Regexes matching different whitespace characters.
	 */
	private static final LexerRegex HORIZONTAL_TABULATION_REGEX = new UnitRegex("\t");
	private static final LexerRegex LINE_FEED_REGEX             = new UnitRegex("\n");
	private static final LexerRegex VERTICAL_TABULATION_REGEX   = new UnitRegex("\u000b");
	private static final LexerRegex FORM_FEED_REGEX             = new UnitRegex("\f");
	private static final LexerRegex CARRIAGE_RETURN_REGEX       = new UnitRegex("\r");
	private static final LexerRegex SPACE_REGEX                 = new UnitRegex("\u0020");
	private static final LexerRegex NEXT_LINE_REGEX             = new UnitRegex("\u0085");
	private static final LexerRegex NO_BREAK_SPACE_REGEX        = new UnitRegex("\u00a0");

	/**
	 * Regex defining a sequence of whitespaces in Ada.
	 */
	protected static final LexerRegex WHITESPACES_REGEX =
		new OneOrMoreRegex(
			UnionRegex.fromRegexes(
				HORIZONTAL_TABULATION_REGEX,
				LINE_FEED_REGEX,
				VERTICAL_TABULATION_REGEX,
				FORM_FEED_REGEX,
				CARRIAGE_RETURN_REGEX,
				SPACE_REGEX,
				NEXT_LINE_REGEX,
				NO_BREAK_SPACE_REGEX
			)
		);

	// Character Categories

	/**
	 * Regexes matching characters based on their "General Category"
	 * as defined by the Unicode standard.
	 */
	protected static final LexerRegex LETTER_UPPERCASE_REGEX       = new GeneralCategoryRegex("Lu");
	protected static final LexerRegex LETTER_LOWERCASE_REGEX       = new GeneralCategoryRegex("Ll");
	protected static final LexerRegex LETTER_TITLECASE_REGEX       = new GeneralCategoryRegex("Lt");
	protected static final LexerRegex LETTER_MODIFIER_REGEX        = new GeneralCategoryRegex("Lm");
	protected static final LexerRegex LETTER_OTHER_REGEX           = new GeneralCategoryRegex("Lo");
	protected static final LexerRegex MARK_NON_SPACING_REGEX       = new GeneralCategoryRegex("Mn");
	protected static final LexerRegex MARK_SPACING_COMBINING_REGEX = new GeneralCategoryRegex("Mc");
	protected static final LexerRegex NUMBER_DECIMAL_REGEX         = new GeneralCategoryRegex("Nd");
	protected static final LexerRegex NUMBER_LETTER_REGEX          = new GeneralCategoryRegex("Nl");
	protected static final LexerRegex PUNCTUATION_CONNECTOR_REGEX  = new GeneralCategoryRegex("Pc");
	protected static final LexerRegex OTHER_FORMAT_REGEX           = new GeneralCategoryRegex("Cf"); // Currently not used
	protected static final LexerRegex SEPARATOR_SPACE_REGEX        = new GeneralCategoryRegex("Zs"); // Currently not used
	protected static final LexerRegex SEPARATOR_LINE_REGEX         = new GeneralCategoryRegex("Zl");
	protected static final LexerRegex SEPARATOR_PARAGRAPH_REGEX    = new GeneralCategoryRegex("Zp");
	protected static final LexerRegex OTHER_PRIVATE_USE_REGEX      = new GeneralCategoryRegex("Co");
	protected static final LexerRegex OTHER_SURROGATE_REGEX        = new GeneralCategoryRegex("Cs");

	/**
	 * Regexes matching various character classes defined by
	 * the Ada 2012 specification.
	 */
	protected static final LexerRegex FORMAT_EFFECTOR_REGEX =
		UnionRegex.fromRegexes(
			HORIZONTAL_TABULATION_REGEX,
			LINE_FEED_REGEX,
			VERTICAL_TABULATION_REGEX,
			FORM_FEED_REGEX,
			CARRIAGE_RETURN_REGEX,
			NEXT_LINE_REGEX,
			SEPARATOR_LINE_REGEX,
			SEPARATOR_PARAGRAPH_REGEX
		);

	protected static final LexerRegex OTHER_CONTROL_REGEX =
		new IntersectionRegex(
			new GeneralCategoryRegex("Cc"),
			new NotRegex(FORMAT_EFFECTOR_REGEX)
		);

	protected static final LexerRegex GRAPHIC_CHARACTER_REGEX =
		new NotRegex(
			UnionRegex.fromRegexes(
				OTHER_CONTROL_REGEX,
				OTHER_PRIVATE_USE_REGEX,
				OTHER_SURROGATE_REGEX,
				FORMAT_EFFECTOR_REGEX,
				new UnitRegex("\ufffe"),
				new UnitRegex("\uffff")
			)
		);

	// Identifiers

	/**
	 * Regex defining the first character of an Ada identifier.
	 *
	 * identifier_start ::=
	 *     letter_uppercase
	 *   | letter_lowercase
	 *   | letter_titlecase
	 *   | letter_modifier
	 *   | letter_other
	 *   | number_letter
	 */
	private static final LexerRegex IDENTIFIER_START_REGEX =
		UnionRegex.fromRegexes(
			LETTER_UPPERCASE_REGEX,
			LETTER_LOWERCASE_REGEX,
			LETTER_TITLECASE_REGEX,
			LETTER_MODIFIER_REGEX,
			LETTER_OTHER_REGEX,
			NUMBER_LETTER_REGEX
		);

	/**
	 * Regex defining the additional character categories allowed
	 * for non-first characters in an Ada identifier.
	 *
	 * identifier_extend ::=
	 *     mark_non_spacing
	 *   | mark_spacing_combining
	 *   | number_decimal
	 *   | punctuation_connector
	 */
	private static final LexerRegex IDENTIFIER_EXTEND_REGEX =
		UnionRegex.fromRegexes(
			MARK_NON_SPACING_REGEX,
			MARK_SPACING_COMBINING_REGEX,
			NUMBER_DECIMAL_REGEX,
			PUNCTUATION_CONNECTOR_REGEX
		);

	/**
	 * Regex defining an Ada identifier.
	 *
	 * identifier ::=
	 *     identifier_start {identifier_start | identifier_extend}
	 */
	protected static final LexerRegex IDENTIFIER_REGEX =
		new ConcatenationRegex(
			IDENTIFIER_START_REGEX,
			new ZeroOrMoreRegex(
				new UnionRegex(
					IDENTIFIER_START_REGEX,
					IDENTIFIER_EXTEND_REGEX
				)
			)
		);

	// Comments

	/**
	 * Regex defining a non-end-of-line character (useed to define comments).
	 */
	private static final LexerRegex NON_END_OF_LINE_CHARACTER_REGEX =
		new NotRegex(
			UnionRegex.fromRegexes(
				LINE_FEED_REGEX,
				VERTICAL_TABULATION_REGEX,
				FORM_FEED_REGEX,
				CARRIAGE_RETURN_REGEX,
				NEXT_LINE_REGEX,
				SEPARATOR_LINE_REGEX,
				SEPARATOR_PARAGRAPH_REGEX
			)
		);

	/**
	 * Regex defining an Ada comment.
	 *
	 * comment ::= --{non_end_of_line_character}
	 */
	protected static final LexerRegex COMMENT_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex(AdaTokenTypes.COMMENT_PREFIX),
			new ZeroOrMoreRegex(NON_END_OF_LINE_CHARACTER_REGEX)
		);

	// String Literals

	/**
	 * Regex defining a non-quotation-mark graphic character (used
	 * to define string literals).
	 *
	 * A non-quotation-mark graphic character is defined as any
	 * graphic_character other than the quotation mark character '"'
	 */
	private static final LexerRegex NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX =
		new IntersectionRegex(
			GRAPHIC_CHARACTER_REGEX,
			new NotRegex(new UnitRegex("\""))
		);

	/**
	 * Regex defining a string element (used to define string literals).
	 *
	 * string_element ::= "" | non_quotation_mark_graphic_character
	 */
	private static final LexerRegex STRING_ELEMENT_REGEX =
		new UnionRegex(
			new UnitRegex("\"\""),
			NON_QUOTATION_MARK_GRAPHIC_CHARACTER_REGEX
		);

	/**
	 * Regex defining an Ada string literal.
	 *
	 * string_literal ::= "{string_element}"
	 */
	protected static final LexerRegex STRING_LITERAL_REGEX =
		ConcatenationRegex.fromRegexes(
			new UnitRegex("\""),
			new ZeroOrMoreRegex(STRING_ELEMENT_REGEX),
			new UnitRegex("\"")
		);

	// Lexer data

	/**
	 * A map associating root regexes with the token types
	 * they represent.
	 */
	private final Map<LexerRegex, IElementType> REGEX_TOKEN_TYPES;

	/**
	 * The set of all root regexes (defined above).
	 */
	private final Set<LexerRegex> ROOT_REGEXES;

	/*
		Instance Initializer
	*/

	{

		// Set the regex -> token-type map

		REGEX_TOKEN_TYPES = Collections.unmodifiableMap(regexTokenTypeMap());

		// Set the root regexes set from the regex -> token-type map's keyset

		ROOT_REGEXES = Collections.unmodifiableSet(REGEX_TOKEN_TYPES.keySet());

	}

	/*
		Fields
	*/

	/**
	 * The lowercase version of the text to be analysed.
	 */
	protected CharSequence text;

	/**
	 * The end of the lexing range.
	 */
	protected int lexingEndOffset;

	/**
	 * The current position of the Lexer in the text.
	 */
	protected int lexingOffset;

	/**
	 * The current state of the Lexer.
	 */
	protected int state;

	/**
	 * The type of the last analysed token.
	 */
	protected IElementType tokenType;

	/**
	 * The start offset of the last analysed token.
	 */
	protected int tokenStart;

	/**
	 * The end offset of the last analysed token.
	 */
	protected int tokenEnd;

	/*
		Methods
	*/

	/**
	 * Returns the token type to use for lexically invalid characters.
	 *
	 * @return The "bad-character" token type.
	 */
	@NotNull
	protected abstract IElementType badCharacterTokenType();

	/**
	 * Returns the mapping from root-regexes to token-types to be used
	 * for this lexer.
	 *
	 * @return The regex -> token-type mapping of this lexer.
	 */
	@NotNull
	protected abstract Map<LexerRegex, IElementType> regexTokenTypeMap();

	/**
	 * Returns the set of root regexes to use at the start of a token
	 * lexing round. This method may be overridden by subclasses to
	 * limit the set of regexes that may match in a lexing round based
	 * on the state of the lexer.
	 *
	 * @return The set of root regexes to use when lexing a token.
	 */
	@NotNull
	protected Set<LexerRegex> getLexingStartingRegexes() {
		return ROOT_REGEXES;
	}

	/**
	 * Returns whether or not this lexer has reached the end of
	 * the text being analysed.
	 *
	 * @return Whether or not the end of the text was reached.
	 */
	protected boolean reachedEndOfText() {
		return lexingOffset == lexingEndOffset;
	}

	/**
	 * Returns the next character to be analysed.
	 *
	 * @return The next character to be analysed.
	 */
	@Nullable
	protected Character nextCharacter() {
		return lexingOffset < 0 || lexingOffset >= lexingEndOffset ?
			null : text.charAt(lexingOffset);
	}

	/**
	 * @see com.intellij.lexer.Lexer#start(CharSequence, int, int, int)
	 */
	@Override
	public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {

		// Check lexing bounds

		if (startOffset < 0) {

			throw new IndexOutOfBoundsException("Illegal negative lexing start offset: " + startOffset);

		} else if (endOffset > buffer.length()) {

			throw new IndexOutOfBoundsException("Illegal lexing end offset greater than total text length: " + endOffset);

		} else if (startOffset > endOffset) {

			throw new IndexOutOfBoundsException("Illegal lexing bounds: " + startOffset + " to " + endOffset);

		}

		// Initialize lexer fields

		text            = buffer.toString().toLowerCase(Locale.ROOT);

		lexingEndOffset = endOffset;
		lexingOffset    = startOffset;
		state           = initialState;

		tokenType       = null;
		tokenStart      = startOffset;
		tokenEnd        = startOffset;

		// Analyse the first token
		// Note: It seems that IntelliJ expects the first call to getTokenType()
		//       to return the type of the first token, but it does not call
		//       advance() itself for some reason...

		advance();

	}

	/**
	 * @see com.intellij.lexer.Lexer#getState()
	 */
	@Override
	public int getState() { return state; }

	/**
	 * @see com.intellij.lexer.Lexer#getTokenType()
	 */
	@Override
	@Nullable
	public IElementType getTokenType() { return tokenType; }

	/**
	 * @see com.intellij.lexer.Lexer#getTokenStart()
	 */
	@Override
	public int getTokenStart() { return tokenStart; }

	/**
	 * @see com.intellij.lexer.Lexer#getTokenEnd()
	 */
	@Override
	public int getTokenEnd() { return tokenEnd; }

	/**
	 * @see com.intellij.lexer.Lexer#advance()
	 */
	@Override
	public void advance() {

		// If the end of the text is reached, set the token type to null
		// and return
		// Note: It is important to set the token type to null as IntelliJ
		//       seems to rely on this to conclude its token fetching
		//       procedure properly

		if (reachedEndOfText()) {
			tokenType = null;
			return;
		}

		// Set the start of the next token to the end of the previous one

		tokenStart = tokenEnd;

		// The offset by which the lexer needs to be rolled back before
		// marking the end of the matched token. This happens for example
		// when lexing the sequence "'Access" where:
		// 1. After the "'" character, only the following regexes advance:
		//    * APOSTROPHE_REGEX and it is nullable at this point
		//    * CHARACTER_LITERAL_REGEX and it is not nullable at this point
		// 2. After the "A" character, only the following regex advances:
		//    * CHARACTER_LITERAL_REGEX and it is not nullable at this point
		// 3. After the first "c" character, no regexes advance
		// At this point, the matching regex is the nullable APOSTROPHE_REGEX
		// obtained at step 1, so the lexer needs to "mark" the sequence "'"
		// as the apostrophe token and roll back to the "A" character in
		// order to start from there during the next call to `advance`
		int rollBackOffset = 0;

		// The set of regexes that successfully advanced so far
		Set<LexerRegex> regexes = new HashSet<>(getLexingStartingRegexes());

		// The last set of regexes, resulting from an iteration of
		// characterLoop, that contained at least one nullable regex
		// (see rollBackOffset description for an example)
		Set<LexerRegex> matchingRegexes = new HashSet<>();

		// A map specifying the root regex from which every regex originates
		// by a series of calls to regex.advanced(char)
		final Map<LexerRegex, LexerRegex> regexLineages = new HashMap<>();

		// The next character to be analysed
		Character nextCharacter = nextCharacter();

		// While the next token has not been determined...

		characterLoop: // label only used for reference in comments
		while (tokenEnd == tokenStart) {

			final Character character = nextCharacter;

			// The set of regexes that will have advanced successfully
			// at the end of this iteration of characterLoop
			final Set<LexerRegex> advancedRegexes = new HashSet<>();

			// For each regex that successfully advanced by all
			// characters so far...

			if (character != null) {

				regexes.forEach(regex -> {

					// Try to advance the regex

					LexerRegex advancedRegex = regex.advanced(character);

					// If the regex advanced successfully, store it for the next
					// iteration of characterLoop, and keep track of the root
					// regex that is the ancestor of the advanced regex

					if (advancedRegex != null) {

						advancedRegexes.add(advancedRegex);

						LexerRegex ancestor = regexLineages.get(regex);

						if (advancedRegex.nullable() || !regex.nullable()) {
							regexLineages.remove(regex);
						}

						regexLineages.put(advancedRegex, ancestor == null ? regex : ancestor);

					}

				});

			}

			// Set the regex set to be the advanced regex set

			regexes = advancedRegexes;

			// If no remaining matching regexes exist, then choose a regex
			// from those that last matched and had at least one nullable
			// regex (or the empty set if either that was never the case, or
			// this is the first iteration of characterLoop)

			if (regexes.size() == 0) {

				// Find the matching regex with the highest priority
				// The chosen regex still has to be nullable, which prevents for
				// example the word "proc" at the end of an Ada file from being
				// assigned the token of the procedure keyword, as its regex for
				// that has a higher priority than the identifier regex, and it
				// does match the sequence "proc" (but it should not be chosen
				// as its advanced regex at that point is not nullable, in other
				// words it still requires the sequence "edure" to "fully match")

				LexerRegex highestPriorityRegex = null;

				for (LexerRegex regex : matchingRegexes) {

					if (
						regex.nullable() &&
							(
								highestPriorityRegex == null ||
									regex.PRIORITY > highestPriorityRegex.PRIORITY
							)
					) {
						highestPriorityRegex = regex;
					}

				}

				// If a non nullable regex (with highest priority) was found,
				// then get the root regex from which this regex originates,
				// get the token type corresponding to that root regex and set
				// the lexer token type to that type

				if (highestPriorityRegex != null) {

					LexerRegex rootRegex =
						regexLineages.getOrDefault(highestPriorityRegex, highestPriorityRegex);

					tokenType =
						REGEX_TOKEN_TYPES.get(rootRegex);

				}

				// Otherwise, set the token type to BAD_CHARACTER

				else {

					tokenType = badCharacterTokenType();

					// If this is a single-character, then the lexing offset
					// needs to be advanced manually to avoid infinite calls
					// to `advance`

					if (lexingOffset == tokenStart) { lexingOffset++; }

					// Reset the rollback offset

					rollBackOffset = 0;

				}

				// Roll the lexer back by the necessary offset

				lexingOffset -= rollBackOffset;

				// Set the token end offset to the lexing offset
				// This will also break the execution of characterLoop

				tokenEnd = lexingOffset;

			}

			// Otherwise, there are still matching regexes and the last character
			// in the text was not yet reached, so get the next character and
			// execute the next iteration of characterLoop

			else {

				// If at least one of the regexes that advanced successfully in
				// this iteration of characterLoop is nullable, then store that
				// set of regexes to be potentially used in the next iteration
				// to find matching regexes, and reset the rollback offset

				if (regexes.stream().anyMatch(LexerRegex::nullable)) {

					matchingRegexes = new HashSet<>(regexes);

					rollBackOffset = 0;

				}

				// Otherwise, do not overwrite the last set of regexes with at
				// least one nullable regex and increase the rollback offset

				else {
					rollBackOffset++;
				}

				// Advance the lexer to the next character

				lexingOffset++;

				nextCharacter = lexingOffset == lexingEndOffset ?
					null : text.charAt(lexingOffset);

			}

		}

	}

	/**
	 * @see com.intellij.lexer.Lexer#getBufferSequence()
	 */
	@NotNull
	@Override
	public CharSequence getBufferSequence() { return text; }

	/**
	 * @see com.intellij.lexer.Lexer#getBufferEnd()
	 */
	@Override
	public int getBufferEnd() { return lexingEndOffset; }

	/*
		Convenience Classes and Methods
	*/

	/**
	 * Simple data class representing a token.
	 */
	public static class Token {

		/**
		 * The token's properties.
		 */
		public final IElementType TOKEN_TYPE;
		public final int          START_OFFSET;
		public final int          END_OFFSET;

		/**
		 * Constructs a new token given a token type, start offset and
		 * end offset.
		 *
		 * @param tokenType The token's type.
		 * @param startOffset The token's start offset.
		 * @param endOffset The token's end offset.
		 */
		public Token(IElementType tokenType, int startOffset, int endOffset) {
			TOKEN_TYPE   = tokenType;
			START_OFFSET = startOffset;
			END_OFFSET   = endOffset;
		}

		/**
		 * Returns whether or not this token is equal to the given object.
		 *
		 * @param object The object to compare to this token.
		 * @return The result of the comparison.
		 */
		@Override
		public boolean equals(Object object) {

			if (!(object instanceof Token)) { return false; }
			else {

				Token token = (Token)object;

				return token.TOKEN_TYPE.toString().equals(TOKEN_TYPE.toString()) &&
					token.START_OFFSET == START_OFFSET && token.END_OFFSET == END_OFFSET;

			}

		}

		/**
		 * Returns a string representation of this token.
		 *
		 * @return A string representation of this token.
		 */
		@Override
		public String toString() {
			return "Token(" + TOKEN_TYPE + ", " + START_OFFSET + ", " + END_OFFSET + ")";
		}

	}

	/**
	 * Performs lexical analysis over the given text by running a single
	 * iteration of `advance` and returning the first token encountered.
	 *
	 * @param text The text over which to perform analysis.
	 * @return The first token in the given text.
	 */
	@Nullable
	public static Token firstToken(CharSequence text) {

		AdaLexer lexer = new AdaLexer();

		lexer.start(text, 0, text.length(), 0);

		return new Token(lexer.getTokenType(), lexer.getTokenStart(), lexer.getTokenEnd());

	}

	/**
	 * Returns a token iterator that can be used to perform lazy lexical
	 * analysis over the entire given text.
	 *
	 * @param text The text over which to perform analysis.
	 * @return A lazy iterator over the tokens in the given text.
	 */
	public static Iterator<Token> textTokens(CharSequence text) {

		final AdaLexer lexer = new AdaLexer();

		lexer.start(text, 0, text.length(), 0);

		return new Iterator<Token>() {

			/**
			 * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext() { return lexer.getTokenType() != null; }

			/**
			 * @see java.util.Iterator#next()
			 */
			@Override
			public Token next() {

				IElementType tokenType   = lexer.getTokenType();
				int          startOffset = lexer.getTokenStart();
				int          endOffset   = lexer.getTokenEnd();

				lexer.advance();

				return new Token(tokenType, startOffset, endOffset);

			}

		};

	}

}
