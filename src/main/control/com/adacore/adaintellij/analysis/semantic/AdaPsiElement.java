package com.adacore.adaintellij.analysis.semantic;

import javax.swing.*;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.Icons;
import com.adacore.adaintellij.Utils;

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
public class AdaPsiElement extends LeafPsiElement implements NavigatablePsiElement {

	/**
	 * Represents the various types of elements that an `AdaPsiElement`
	 * can represent.
	 *
	 * @see com.adacore.adaintellij.analysis.semantic.AdaPsiStructureManager
	 *
	 * Possible values are:
	 *
	 * PACKAGE_SPEC_IDENTIFIER => The identifier representing a package spec.
	 * PACKAGE_BODY_IDENTIFIER => The identifier representing a package body.
	 *
	 * TYPE_IDENTIFIER         => The identifier in a type declaration.
	 * CONSTANT_IDENTIFIER     => The identifier in a constant declaration.
	 * VARIABLE_IDENTIFIER     => The identifier in a variable declaration.
	 *
	 * PROCEDURE_IDENTIFIER    => The identifier representing a procedure.
	 * FUNCTION_IDENTIFIER     => The identifier representing a function.
	 *
	 * OTHER                   => Any other type of Ada element.
	 */
	public enum AdaElementType {

		PACKAGE_SPEC_IDENTIFIER, PACKAGE_BODY_IDENTIFIER,

		TYPE_IDENTIFIER, CONSTANT_IDENTIFIER, VARIABLE_IDENTIFIER,

		PROCEDURE_IDENTIFIER, FUNCTION_IDENTIFIER,

		OTHER

	}

	/**
	 * The underlying tree node.
	 */
	private ASTNode node;

	/**
	 * The type of this Ada element. Set to `OTHER` by default.
	 */
	private AdaElementType adaElementType = AdaElementType.OTHER;

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
	 * Returns an icon representing this `AdaPsiElement` given
	 * some flags packed in an integer. This implementation
	 * ignores the given flags and always returns the same icon
	 * given the type of this Ada element. It is therefore
	 * preferred to simply use `getIcon()`.
	 *
	 * @param flags The flags to use to determine the icon.
	 * @return An icon representing this element.
	 */
	@Nullable
	@Override
	public Icon getIcon(int flags) {

		switch (adaElementType) {

			case PACKAGE_SPEC_IDENTIFIER: return Icons.ADA_SPEC_SOURCE_FILE;
			case PACKAGE_BODY_IDENTIFIER: return Icons.ADA_BODY_SOURCE_FILE;

			case TYPE_IDENTIFIER:         return Icons.ADA_TYPE;
			case CONSTANT_IDENTIFIER:     return Icons.ADA_CONSTANT;
			case VARIABLE_IDENTIFIER:     return Icons.ADA_VARIABLE;

			case PROCEDURE_IDENTIFIER:    return Icons.ADA_PROCEDURE;
			case FUNCTION_IDENTIFIER:     return Icons.ADA_FUNCTION;

			case OTHER:
			default:                      return null;

		}

	}

	/**
	 * Returns the icon representing this `AdaPsiElement`.
	 *
	 * @return The icon representing this element.
	 */
	@Nullable
	public Icon getIcon() { return getIcon(ICON_FLAG_VISIBILITY); }

	/**
	 * Sets the type of this Ada element to the given element type.
	 *
	 * @param elementType The element type to set.
	 */
	void setAdaElementType(@NotNull AdaElementType elementType) {
		adaElementType = elementType;
	}

	/**
	 * Returns the Ada element type of this element.
	 *
	 * @return The Ada element type of this element.
	 */
	@NotNull
	public AdaElementType getAdaElementType() { return adaElementType; }

	/**
	 * Returns the `AdaPsiElement` corresponding to the given element,
	 * or null if the latter has no such corresponding element.
	 * For more information:
	 * @see com.adacore.adaintellij.analysis.semantic.AdaParser
	 *
	 * @param element The element for which to get the corresponding
	 *                Ada PSI element.
	 * @return The corresponding `AdaPsiElement` or null.
	 */
	@Contract(pure = true)
	@Nullable
	public static AdaPsiElement getFrom(@NotNull PsiElement element) {

		// If the given PSI element is an Ada PSI element,
		// then cast it and return it

		if (element instanceof AdaPsiElement) {
			return (AdaPsiElement)element;
		}

		// Else it is probably a leaf PSI element whose parent
		// is an Ada PSI element (see explanation in `AdaParser`),
		// so check if its parent is an Ada PSI element and if it
		// is, cast it and return it

		PsiElement parent = element.getParent();

		if (parent instanceof AdaPsiElement) {
			return (AdaPsiElement)parent;
		}

		// Otherwise, the element has no corresponding Ada PSI
		// element, so return null

		return null;

	}

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
	 * Returns true only if both files containing the two given elements
	 * are found and are equivalent.
	 *
	 * @param element1 The first element to compare.
	 * @param element2 The second element to compare.
	 * @return Whether or not the given elements are in the same file.
	 */
	public static boolean areInSameFile(@NotNull PsiElement element1, @NotNull PsiElement element2) {

		PsiFile file1 = element1.getContainingFile();
		PsiFile file2 = element2.getContainingFile();

		return file1 != null && file2 != null &&
			Utils.psiFilesRepresentSameFile(file1, file2);

	}

	/**
	 * Returns a string representation of this PSI element.
	 *
	 * @return A string representation of this PSI element.
	 */
	@Override
	public String toString() {
		return "AdaPsiElement(" + getElementType().toString() + ")";
	}

}
