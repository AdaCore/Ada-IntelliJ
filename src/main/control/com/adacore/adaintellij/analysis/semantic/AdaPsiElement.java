package com.adacore.adaintellij.analysis.semantic;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Ada AST node that is not a root node.
 * Due to the way the Ada-IntelliJ plugin constructs ASTs, all tokens
 * produced by the Ada lexer are directly mapped to instances of this
 * class, or its subclass `AdaPsiReference` in some cases (e.g.
 * identifiers). All elements of this class are therefore leaves in
 * their respective trees.
 *
 * For detailed information about the structure of ASTs built by the
 * Ada-IntelliJ Ada parser:
 * @see com.adacore.adaintellij.analysis.semantic.AdaParser
 */
public class AdaPsiElement extends LeafPsiElement {
	
	/**
	 * The underlying tree node.
	 */
	private ASTNode node;
	
	/**
	 * Constructs a new AdaPsiElement given a tree node.
	 *
	 * @param node The tree node to back the constructed
	 *             PSI element.
	 */
	AdaPsiElement(@NotNull ASTNode node) {
		super(node.getElementType(), node.getChars());
		this.node = node;
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#getParent()
	 */
	@Override
	public PsiElement getParent() {
		
		ASTNode parentNode = node.getTreeParent();
		
		return parentNode == null ? null : parentNode.getPsi();
		
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#getNextSibling()
	 */
	@Override
	public PsiElement getNextSibling() {
		
		ASTNode nextNode = node.getTreeNext();
		
		return nextNode == null ? null : nextNode.getPsi();
		
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#getPrevSibling()
	 */
	@Override
	public PsiElement getPrevSibling() {
		
		ASTNode previousNode = node.getTreePrev();
		
		return previousNode == null ? null : previousNode.getPsi();
		
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#getContainingFile()
	 */
	@Override
	public PsiFile getContainingFile() { return (PsiFile)getParent(); }
	
	/**
	 * @see com.intellij.psi.PsiElement#findReferenceAt(int)
	 *
	 * Note: The given offset is relative to the element itself
	 */
	@Override
	public PsiReference findReferenceAt(int offset) {
		return this instanceof AdaPsiReference && offset >= 0 && offset <= getTextLength() ?
			(AdaPsiReference)this : null;
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#isValid()
	 */
	@Override
	public boolean isValid() {
		
		PsiFile containingFile = getContainingFile();
		
		return containingFile != null && containingFile.isValid();
		
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#isWritable()
	 */
	@Override
	public boolean isWritable() { return getContainingFile().isWritable(); }
	
	/**
	 * @see com.intellij.psi.PsiElement#getReference()
	 */
	@Override
	public PsiReference getReference() {
		
		PsiReference[] references = getReferences();
		
		return references.length > 0 ? references[0] : null;
		
	}
	
	/**
	 * @see com.intellij.psi.PsiElement#getReferences()
	 */
	@Override
	@NotNull
	public PsiReference[] getReferences() { return new PsiReference[0]; }
	
	/**
	 * @see com.intellij.psi.PsiElement#replace(PsiElement)
	 */
	@Override
	public PsiElement replace(@NotNull PsiElement newElement) throws IncorrectOperationException {
		throw new IncorrectOperationException("Not yet supported");
	}
	
	/**
	 * @see com.intellij.lang.ASTNode#getStartOffset()
	 */
	@Override
	public int getStartOffset() { return node.getStartOffset(); }
	
	/**
	 * Compares two PSI elements and returns true if they represent the
	 * same element in the same file. Due to the flat nature of the ASTs
	 * built by the Ada parser, this comparison can be accomplished by
	 * simply checking that the two elements are in the same file and that
	 * their offsets within that file are equal.
	 *
	 * @param element1 The first element to compare.
	 * @param element2 The second element to compare.
	 * @return Whether or not the two elements are equal.
	 */
	@Contract("null, _ -> false; _, null -> false")
	public static boolean areEqual(@Nullable PsiElement element1, @Nullable PsiElement element2) {
		
		if (element1 == null || element2 == null) { return false; }
		
		return areInSameFile(element1, element2) &&
			element1.getNode().getStartOffset() == element2.getNode().getStartOffset();
		
	}
	
	/**
	 * Returns whether or not the given PSI elements are in the same file.
	 *
	 * @param element1 The first element to compare.
	 * @param element2 The second element to compare.
	 * @return Whether or not the given elements are in the same file.
	 */
	public static boolean areInSameFile(@NotNull PsiElement element1, @NotNull PsiElement element2) {
		return element1.getContainingFile().getVirtualFile()
			.equals(element2.getContainingFile().getVirtualFile());
	}
	
}
