package com.adacore.adaintellij.analysis.syntactic;

import com.intellij.lang.*;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.*;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.lexical.*;

/**
 * Definition of the parser implementation for Ada.
 *
 * For the actual implementation of the Ada parser:
 * @see AdaParser
 */
public final class AdaParserDefinition implements ParserDefinition {

	/**
	 * @see com.intellij.lang.ParserDefinition#createLexer(Project)
	 */
	@NotNull
	@Override
	public Lexer createLexer(Project project) { return new AdaLexer(); }

	/**
	 * @see com.intellij.lang.ParserDefinition#createParser(Project)
	 */
	@Override
	public PsiParser createParser(Project project) { return new AdaParser(); }

	/**
	 * @see com.intellij.lang.ParserDefinition#getFileNodeType()
	 */
	@Override
	public IFileElementType getFileNodeType() {
		return AdaFileElementType.INSTANCE;
	}

	/**
	 * @see com.intellij.lang.ParserDefinition#createFile(FileViewProvider)
	 */
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new AdaPsiFile(viewProvider);
	}

	/**
	 * @see com.intellij.lang.ParserDefinition#createElement(ASTNode)
	 */
	@NotNull
	@Override
	public PsiElement createElement(ASTNode node) {
		return AdaTokenTypes.IDENTIFIER_TOKEN_SET.contains(node.getElementType()) ?
			new AdaPsiReference(node) : new AdaPsiElement(node);
	}

	/**
	 * @see com.intellij.lang.ParserDefinition#getWhitespaceTokens()
	 */
	@NotNull
	@Override
	public TokenSet getWhitespaceTokens() { return AdaTokenTypes.WHITESPACE_TOKEN_SET; }

	/**
	 * @see com.intellij.lang.ParserDefinition#getCommentTokens()
	 */
	@NotNull
	@Override
	public TokenSet getCommentTokens() { return AdaTokenTypes.COMMENT_TOKEN_SET; }

	/**
	 * @see com.intellij.lang.ParserDefinition#getStringLiteralElements()
	 */
	@NotNull
	@Override
	public TokenSet getStringLiteralElements() { return AdaTokenTypes.STRING_LITERAL_TOKEN_SET; }

	/**
	 * @see com.intellij.lang.ParserDefinition#spaceExistenceTypeBetweenTokens(ASTNode, ASTNode)
	 */
	@Override
	public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

}
