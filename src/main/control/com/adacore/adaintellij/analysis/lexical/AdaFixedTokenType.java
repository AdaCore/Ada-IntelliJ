package com.adacore.adaintellij.analysis.lexical;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Ada 2012 token type with a fixed text representation.
 */
public final class AdaFixedTokenType extends AdaTokenType {
	
	/**
	 * Text representation of this token type.
	 */
	@NonNls
	@NotNull
	public final String TOKEN_TEXT;
	
	/**
	 * Constructs a new Ada token type with the given token text.
	 * Used for fixed-text tokens, such as keywords and delimiters.
	 *
	 * @param debugName The name of the token type, used for debugging purposes.
	 * @param tokenText The text representation of the constructed token type.
	 */
	AdaFixedTokenType(
		@NotNull @NonNls String debugName,
		@NotNull @NonNls String tokenText
	) {
		
		super(debugName);
		
		TOKEN_TEXT = tokenText;
		
	}
	
}
