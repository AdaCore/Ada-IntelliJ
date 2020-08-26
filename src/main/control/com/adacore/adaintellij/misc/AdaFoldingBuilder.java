package com.adacore.adaintellij.misc;

import com.adacore.adaintellij.lsp.AdaLSPDriver;
import com.adacore.adaintellij.lsp.AdaLSPServer;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ui.update.MergingUpdateQueue;
import org.eclipse.lsp4j.FoldingRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdaFoldingBuilder extends FoldingBuilderEx implements DumbAware {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {

        Project project = root.getProject();
        PsiFile psiFile = root.getContainingFile();

        List<FoldingRange> foldingRanges = AdaLSPDriver.getServer(project)
            .foldingRange(
                psiFile.getVirtualFile().getUrl()
            );

        List<FoldingDescriptor> descriptors = this.buildFoldingDescriptorsFromFoldingRanges(
            foldingRanges,
            document,
            root
        );

        return descriptors.toArray(
            new FoldingDescriptor[descriptors.size()]
        );
    }

    private List<FoldingDescriptor> buildFoldingDescriptorsFromFoldingRanges(
        List<FoldingRange> foldingRanges,
        Document document,
        PsiElement root
    ){
        List<FoldingDescriptor> descriptors = new ArrayList<>();

        for(FoldingRange foldingRange :  foldingRanges) {
            int foldStartOffset;
            int foldEndOffset;

            if (foldingRange.getStartLine() == foldingRange.getEndLine()) {
                continue;
            }

            try {
                foldStartOffset = document.getLineStartOffset(foldingRange.getStartLine());
                foldEndOffset = document.getLineEndOffset(foldingRange.getEndLine());
            } catch (IndexOutOfBoundsException e) {
                continue;
            }

            if (foldStartOffset < foldEndOffset) {

                Set<Object> dependencies = new HashSet<>();

                descriptors.add(new FoldingDescriptor(
                        root.getNode(),
                        new TextRange(
                                foldStartOffset,
                                foldEndOffset
                        ),
                        null,
                        document.getText(new TextRange(
                                foldStartOffset,
                                document.getLineEndOffset(foldingRange.getStartLine())
                        )) + " ...",
                        foldingRange.getKind().equals("imports"),
                        dependencies
                ));
            }
        }

        return descriptors;
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return null;
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}
