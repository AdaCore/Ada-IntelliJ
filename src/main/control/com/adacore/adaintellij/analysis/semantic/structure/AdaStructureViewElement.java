package com.adacore.adaintellij.analysis.semantic.structure;

import java.util.stream.Stream;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.analysis.semantic.*;

import static com.adacore.adaintellij.analysis.semantic.AdaPsiElement.AdaElementType;

/**
 * Element in the structure view of an Ada file.
 */
public final class AdaStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
	
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
		
		if (!(element instanceof AdaPsiFile)) { return TreeElement.EMPTY_ARRAY; }
		
		// Filter this element's children by Ada element type,
		// map them to `AdaStructureViewElement` and return them
		
		return Stream.of(element.getChildren())
			.filter(element -> {
				
				if (element == null) { return false; }
				
				AdaPsiElement adaPsiElement = AdaPsiElement.getFrom(element);
				
				if (!(adaPsiElement instanceof AdaPsiReference)) { return false; }
				
				AdaElementType elementType = adaPsiElement.getAdaElementType();
				
				return
					elementType == AdaElementType.PACKAGE_SPEC_IDENTIFIER ||
					elementType == AdaElementType.PACKAGE_BODY_IDENTIFIER ||
					elementType == AdaElementType.TYPE_IDENTIFIER         ||
					elementType == AdaElementType.CONSTANT_IDENTIFIER     ||
					elementType == AdaElementType.VARIABLE_IDENTIFIER     ||
					elementType == AdaElementType.PROCEDURE_IDENTIFIER    ||
					elementType == AdaElementType.FUNCTION_IDENTIFIER;
				
			})
			.map(element -> new AdaStructureViewElement((AdaPsiReference)element))
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
