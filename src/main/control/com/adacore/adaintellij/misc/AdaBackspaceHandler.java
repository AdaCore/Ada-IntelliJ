package com.adacore.adaintellij.misc;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class AdaBackspaceHandler extends BackspaceHandlerDelegate {

    @Override
    public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
        if (c == '"') {
            int currentCaretOffset = editor.getCaretModel().getCurrentCaret().getOffset();
            String nextLetter = editor.getDocument().getText(new TextRange(currentCaretOffset, currentCaretOffset + 1));
            if (nextLetter.equals("\"")) {
                editor.getDocument().deleteString(currentCaretOffset, currentCaretOffset + 1);
            }

        }
    }

    @Override
    public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
        return false;
    }
}
