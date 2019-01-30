package com.adacore.adaintellij.analysis.lexical;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Ada syntax highlighter factory.
 */
public final class AdaSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

	/**
	 * @see com.intellij.openapi.fileTypes.SyntaxHighlighterFactory#getSyntaxHighlighter(Project, VirtualFile)
	 */
	@NotNull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
		return new AdaSyntaxHighlighter();
	}

}
