package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.analysis.syntactic.*;

/**
 * Processor of renaming operations over Ada PSI elements.
 */
public class AdaRenamePsiElementProcessor extends RenamePsiElementProcessor {

	/**
	 * Returns whether or not the given element can be renamed.
	 *
	 * @param element The element to test for rename support.
	 * @return Whether or not the given element can be renamed.
	 */
	public static boolean canRenameElement(@NotNull PsiElement element) {
		return AdaPsiElement.getFrom(element) instanceof AdaPsiReference;
	}

	/**
	 * @see AdaRenamePsiElementProcessor#canRenameElement(PsiElement)
	 */
	@Override
	public boolean canProcessElement(@NotNull PsiElement element) {
		return canRenameElement(element);
	}

	/**
	 * Performs the actual process of renaming the given element
	 * to the given name, as well as the given usages of that
	 * element, and notifies the given refactoring listener.
	 *
	 * This method seems to be called with an empty array of usages
	 * when the rename action is called from one of the usages,
	 * instead of the declaration itself, so in that case we manually
	 * perform a reference-search and patch the usage array. Note
	 * that this means that renaming a declared element which simply
	 * happens to have no usages will result in an unnecessary
	 * reference-search.
	 *
	 * @param element The element to rename.
	 * @param newName The new element name.
	 * @param usages The element's usages to rename.
	 * @param listener Refactoring listener to be notified.
	 */
	@Override
	public void renameElement(
		@NotNull  PsiElement                 element,
		@NotNull  String                     newName,
		@NotNull  UsageInfo[]                usages,
		@Nullable RefactoringElementListener listener
	) {

		// Get an Ada PSI element from the given element

		AdaPsiElement adaPsiElement = AdaPsiElement.getFrom(element);

		// Use the Ada PSI element if it is available

		PsiElement patchedElement = adaPsiElement == null ? element : adaPsiElement;

		// Patch the element's usages in case they were not given

		UsageInfo[] patchedUsages = usages.length > 0 ? usages :
			findReferences(patchedElement)
				.stream()
				.map(reference -> createUsageInfo(patchedElement, reference, reference.getElement()))
				.toArray(UsageInfo[]::new);

		// Perform the renaming

		super.renameElement(patchedElement, newName, patchedUsages, listener);

	}

}
