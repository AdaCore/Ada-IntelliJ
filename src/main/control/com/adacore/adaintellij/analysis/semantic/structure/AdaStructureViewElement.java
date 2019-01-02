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
import com.adacore.adaintellij.lsp.AdaLSPServer;
import com.adacore.adaintellij.lsp.LSPUtils;
import com.adacore.adaintellij.analysis.semantic.AdaPsiElement;
import com.adacore.adaintellij.Utils;

/**
 * Element in the structure view of an Ada file.
 */
public class AdaStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	
	/**
	 * The PSI element that this structure view element represents.
	 */
	private NavigatablePsiElement element;
	
	/**
	 * Constructs a new AdaStructureViewElement given a PSI
	 * element.
	 *
	 * @param element The PSI element represented by the
	 *                constructed structure view element.
	 */
	AdaStructureViewElement(@NotNull NavigatablePsiElement element) {
		this.element = element;
	}
	
	/**
	 * @see com.intellij.ide.structureView.StructureViewTreeElement#getValue()
	 */
	@Override
	public Object getValue() { return element; }
	
	/**
	 * Returns a string representing this element when sorting.
	 *
	 * @return This element's sorting key.
	 */
	@NotNull
	@Override
	public String getAlphaSortKey() { return element.getText(); }
	
	/**
	 * @see com.intellij.ide.util.treeView.smartTree.TreeElement#getPresentation()
	 */
	@NotNull
	@Override
	public ItemPresentation getPresentation() {
		return new AdaStructureItemPresentation(element);
	}
	
	/**
	 * @see com.intellij.ide.util.treeView.smartTree.TreeElement#getChildren()
	 *
	 * Makes a `textDocument/documentSymbol` request to the ALS to get
	 * the list of symbols in the given document, and returns them.
	 */
	@NotNull
	@Override
	public TreeElement[] getChildren() {
		
		// If this element is not the root element representing
		// the file itself, then return no children
		
		if (!(element instanceof PsiFile)) { return TreeElement.EMPTY_ARRAY; }
		
		final PsiFile     psiFile     = element.getContainingFile();
		final Document    document    = Utils.getPsiFileDocument(psiFile);
		      VirtualFile virtualFile = Utils.getPsiFileVirtualFile(psiFile);
		
		if (document == null || virtualFile == null) { return TreeElement.EMPTY_ARRAY; }
		
		String documentUri = virtualFile.getUrl();
		
		// Make the request and wait for the result
		
		AdaLSPServer lspServer = AdaLSPDriver.getServer(element.getProject());
		
		if (lspServer == null) { return TreeElement.EMPTY_ARRAY; }
		
		List<DocumentSymbol> symbols = lspServer.documentSymbol(documentUri);
		
		// Map the returned symbols to structure view
		// elements and return them as an array
		
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
	
	/**
	 * @see com.intellij.pom.Navigatable#navigate(boolean)
	 */
	@Override
	public void navigate(boolean requestFocus) { element.navigate(requestFocus); }
	
	/**
	 * @see com.intellij.pom.Navigatable#canNavigate()
	 */
	@Override
	public boolean canNavigate() { return element.canNavigate(); }
	
	/**
	 * @see com.intellij.pom.Navigatable#canNavigateToSource()
	 */
	@Override
	public boolean canNavigateToSource() { return element.canNavigateToSource(); }
	
}
