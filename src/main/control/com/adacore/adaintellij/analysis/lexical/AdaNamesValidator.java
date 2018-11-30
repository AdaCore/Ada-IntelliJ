package com.adacore.adaintellij.analysis.lexical;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public final class AdaNamesValidator implements NamesValidator {
	
	@Override
	public boolean isKeyword(@NotNull String name, Project project) {
		
		AdaLexer.Token firstToken = AdaLexer.firstToken(name);
		
		return firstToken != null &&
			AdaTokenTypes.KEYWORD_TOKEN_SET.contains(firstToken.TOKEN_TYPE);
		
	}
	
	@Override
	public boolean isIdentifier(@NotNull String name, Project project) {
		
		AdaLexer.Token firstToken = AdaLexer.firstToken(name);
		
		return firstToken != null &&
			AdaTokenTypes.IDENTIFIER_TOKEN_SET.contains(firstToken.TOKEN_TYPE);
		
	}
	
}
