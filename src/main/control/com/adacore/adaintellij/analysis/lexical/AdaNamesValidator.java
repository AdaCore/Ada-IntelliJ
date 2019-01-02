package com.adacore.adaintellij.analysis.lexical;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Name validator for Ada, used to perform basic syntactic
 * checks on given names.
 */
public final class AdaNamesValidator implements NamesValidator {
	
	/**
	 * @see com.intellij.lang.refactoring.NamesValidator#isKeyword(String, Project)
	 */
	@Override
	public boolean isKeyword(@NotNull String name, Project project) {
		
		AdaLexer.Token firstToken = AdaLexer.firstToken(name);
		
		return firstToken != null &&
			AdaTokenTypes.KEYWORD_TOKEN_SET.contains(firstToken.TOKEN_TYPE);
		
	}
	
	/**
	 * @see com.intellij.lang.refactoring.NamesValidator#isIdentifier(String, Project)
	 */
	@Override
	public boolean isIdentifier(@NotNull String name, Project project) {
		
		AdaLexer.Token firstToken = AdaLexer.firstToken(name);
		
		return firstToken != null &&
			AdaTokenTypes.IDENTIFIER_TOKEN_SET.contains(firstToken.TOKEN_TYPE);
		
	}
	
}
