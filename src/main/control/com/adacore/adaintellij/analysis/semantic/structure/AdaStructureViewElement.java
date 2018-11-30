package com.adacore.adaintellij.analysis.semantic.structure;

import java.util.List;
import java.util.Objects;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import org.eclipse.lsp4j.DocumentSymbol;

import com.adacore.adaintellij.lsp.AdaLSPDriver;
import com.adacore.adaintellij.lsp.LSPUtils;
import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;
import com.adacore.adaintellij.Utils;

public class AdaStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	
	private NavigatablePsiElement element;
	
	AdaStructureViewElement(@NotNull NavigatablePsiElement element) {
		this.element = element;
	}
	
	@Override
	public Object getValue() { return element; }
	
	@NotNull
	@Override
	public String getAlphaSortKey() { return element.getText(); }
	
	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		return new AdaStructureItemPresentation(element);
	}
	
	@NotNull
	@Override
	public TreeElement[] getChildren() {
		
		if (!(element instanceof PsiFile)) { return TreeElement.EMPTY_ARRAY; }
		
		final PsiFile     psiFile     = element.getContainingFile();
		final Document    document    = Utils.getPsiFileDocument(psiFile);
		      VirtualFile virtualFile = Utils.getPsiFileVirtualFile(psiFile);
		
		if (document == null || virtualFile == null) { return TreeElement.EMPTY_ARRAY; }
		
		String documentUri = virtualFile.getUrl();
		
		List<DocumentSymbol> symbols =
			AdaLSPDriver.getServer(element.getProject()).documentSymbol(documentUri);
		
		return symbols.stream()
			.map(symbol -> {
			
				PsiElement element = psiFile.findElementAt(
					LSPUtils.positionToOffset(document, symbol.getSelectionRange().getStart()));
				
				if (element == null) { return null; }
				
				AdaPsiElement adaPsiElement = AdaPsiElement.getFrom(element);
				
				if (adaPsiElement == null) { return null; }
				
				return new AdaStructureViewElement(adaPsiElement);
				
			})
			.filter(Objects::nonNull)
			.toArray(TreeElement[]::new);
		
	}
	
	@Override
	public void navigate(boolean requestFocus) { element.navigate(requestFocus); }
	
	@Override
	public boolean canNavigate() { return element.canNavigate(); }
	
	@Override
	public boolean canNavigateToSource() { return element.canNavigateToSource(); }
	
}
