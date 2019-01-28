package com.adacore.adaintellij.analysis.semantic;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for the Ada language.
 *
 * The Ada-IntelliJ plugin does not implement a full-fledged Ada parser, and
 * instead relies on the Ada Language Server (ALS) to provide all sorts of
 * semantic features such as error reporting/highlighting, reference search,
 * code completion, code navigation, etc.
 *
 * However, these operations still need to go through an AST in the format
 * defined by the IntelliJ platform, which consists of the various classes
 * from the the PSI family.
 *
 * To address this, the Ada-IntelliJ plugin provides some sort of dummy parser
 * for Ada files, that simply outputs an AST in the form of a flat tree of nodes
 * that directly map to the tokens produced by the Ada lexer.
 *
 * Consider the following example:
 *
 *       Code            Lexer Tokens                   Parsed PSI Tree
 *  ===========================================================================
 *  ...                      ...       -------             ...---|
 *  declare            DECLARE_KEYWORD -------   AdaPsiElement---|
 *     X : Integer;      WHITE_SPACE   -------   AdaPsiElement---|
 *  begin                IDENTIFIER    ------- AdaPsiReference---|
 *     X := A + 1;       WHITE_SPACE   -------   AdaPsiElement---|
 *  end;                    COLON      -------   AdaPsiElement---|
 *  ...                  WHITE_SPACE   -------   AdaPsiElement---|
 *                       IDENTIFIER    ------- AdaPsiReference---|
 *                        SEMICOLON    -------   AdaPsiElement---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                      BEGIN_KEYWORD  -------   AdaPsiElement---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                       IDENTIFIER    ------- AdaPsiReference---|---AdaPsiFile
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                       ASSIGNMENT    -------   AdaPsiElement---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                       IDENTIFIER    ------- AdaPsiReference---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                        PLUS_SIGN    -------   AdaPsiElement---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                     DECIMAL_LITERAL -------   AdaPsiElement---|
 *                        SEMICOLON    -------   AdaPsiElement---|
 *                       WHITE_SPACE   -------   AdaPsiElement---|
 *                       END_KEYWORD   -------   AdaPsiElement---|
 *                        SEMICOLON    -------   AdaPsiElement---|
 *                           ...       -------             ...---|
 *
 * As this example demonstrates, the final abstract trees returned by this
 * parser are not ASTs in the proper, traditional sense of the word, namely:
 *  - They maintain ALL elements of the source code, including whitespaces,
 *    comments, delimiters, etc.
 *  - They have absolutely no semantic dimension to them
 * Their only purpose is to act as placeholders that enable the Ada-IntelliJ
 * plugin to override operations defined on their nodes and "outsource" the
 * actual work required for those operations to the ALS.
 *
 * Note that, as an intermediate step, before building the final AST consisting
 * of PSI elements, the PSI builder builds a tree consisting of instances of the
 * `ASTNode` interface, which counter-intuitively is not a proper AST either.
 * The underlying node of a PSI element is accessible through the `getNode`
 * method.
 *
 * See this diagram from the IntelliJ platform SDK tutorial:
 * https://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/img/PsiBuilder.gif
 *
 * The intermediate tree does not necessarily have a one-to-one mapping to the
 * final PSI tree. In fact, for every "leaf" marker (except for whitespace and
 * comments), the PSI builder seems to add to the intermediate tree a
 * `CompositeElement` containing a single child `LeafElement`. In the final PSI
 * tree, the `CompositeElement` is mapped to an `AdaPsiElement` or an
 * `AdaPsiReference` and the `LeafElement` is mapped to some other instance of a
 * class implementing `PsiElement`. This means that, unlike what is shown in the
 * example above, the actual final parsed PSI tree has a depth of 2. Here is an
 * example diagram with arrows illustrating references:
 *
 *                                  AdaPsiFile
 *                                   ^ ^  ^ ^
 *                                   | |  | |
 *             ----------------------- |  | ----------------------
 *             |                --------  --------               |
 *             |                |                |               |
 *             v                v                v               v
 *       AdaPsiElement   AdaPsiReference   AdaPsiElement   AdaPsiElement
 *             ^                ^                ^               ^
 *             |                |                |               |
 *         PsiElement      PsiElement      PsiElement      PsiElement
 *
 * As you can see, Ada PSI elements do not hold references to their "child"
 * nodes since they are implemented as leaf nodes.
 * It is important to keep this structure in mind as the IntelliJ platform API
 * deals with instances of `PsiElement`, and in some cases those instances
 * happen to be the the "hidden" leaves of the PSI tree when the Ada-IntelliJ
 * plugin actually needs to work with the first-level `AdaPsiElement` leaves. In
 * those cases, you should use the static method `AdaPsiElement.getFrom` to
 * ensure that the element you are working with is an Ada PSI element.
 */
public final class AdaParser implements PsiParser {
	
	/**
	 * @see com.intellij.lang.PsiParser#parse(IElementType, PsiBuilder)
	 */
	@NotNull
	@Override
	public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
		
		// Set the root marker for the whole source text
		// Note: This absolutely needs to be done before any calls to `builder.advance`
		//       *AND* `builder.getTokenType`. In the case of `PsiBuilderImpl`, the
		//       latter, even though it looks like a pure method, will in reality
		//       internally advance the builder if the next encountered token is a
		//       whitespace or comment token. This will result in the builder not
		//       covering the entire source file, which will cause the IDE to throw an
		//       exception every time a source file starting with whitespace or a
		//       comment is loaded.
		
		PsiBuilder.Marker rootMarker = builder.mark();
		
		// Get the first token
		
		IElementType tokenType = builder.getTokenType();
		
		// Set the leaf node marker
		
		PsiBuilder.Marker marker = builder.mark();
		
		// While the lexer still produces tokens...
		
		// `tokenType != null` should be guaranteed to be false
		// by the preceding check `!builder.eof()`, but we check
		// nullability anyways just to be safe
		while (!builder.eof() && tokenType != null) {
			
			// Advance to the next token
			
			builder.advanceLexer();
			
			// Set the next leaf node marker and mark the previous
			// marker as done right before the newly created marker
			// This is a neat little facility provided by the
			// `PsiBuilder.Marker` interface
			
			PsiBuilder.Marker nextMarker = builder.mark();
			marker.doneBefore(tokenType, nextMarker);
			
			// Store the newly created marker for the next iteration
			
			marker = nextMarker;
			
			// Get the next token
			
			tokenType = builder.getTokenType();
			
		}
		
		// The end of the source file was reached but we still
		// have a set marker that does not correspond to any token,
		// so drop it
		
		marker.drop();
		
		// Mark the root marker as done
		
		rootMarker.done(root);
		
		// Build the tree and return it
		
		return builder.getTreeBuilt();
		
	}
	
}