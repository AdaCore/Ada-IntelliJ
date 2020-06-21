package com.adacore.adaintellij.misc;

import com.adacore.adaintellij.analysis.lexical.AdaTokenTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdaBraceMatcher implements PairedBraceMatcher {

    @NotNull
    @Override
    public BracePair[] getPairs() {

        BracePair[] pairs = {
            new BracePair(
                AdaTokenTypes.LEFT_PARENTHESIS,
                AdaTokenTypes.RIGHT_PARENTHESIS,
                true
            )
        };

        return pairs;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return 0;
    }
}
