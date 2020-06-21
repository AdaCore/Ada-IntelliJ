package com.adacore.adaintellij.misc;

import com.adacore.adaintellij.analysis.lexical.AdaTokenTypes;
import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler;
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdaQuoteHandler extends SimpleTokenSetQuoteHandler implements MultiCharQuoteHandler {

    @Override
    public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
        int start = iterator.getStart();
        try {
            Document doc = editor.getDocument();
            CharSequence chars = doc.getCharsSequence();
            int lineEnd = doc.getLineEndOffset(doc.getLineNumber(offset));

            while (!iterator.atEnd() && iterator.getStart() < lineEnd) {
                IElementType tokenType = iterator.getTokenType();

                if (tokenType == AdaTokenTypes.SPEECH_MARK) {
                    if (isNonClosedLiteral(iterator, chars)) return true;
                }
                iterator.advance();
            }
        }
        finally {
            while(iterator.atEnd() || iterator.getStart() != start) iterator.retreat();
        }

        return false;
    }

    @Nullable
    @Override
    public CharSequence getClosingQuote(@NotNull HighlighterIterator iterator, int offset) {
        return "\"";
    }

    @Override
    public void insertClosingQuote(@NotNull Editor editor, int offset, @NotNull PsiFile file, @NotNull CharSequence closingQuote) {
        editor.getDocument().insertString(offset, closingQuote);
        Project project = file.getProject();
        PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
    }
}
