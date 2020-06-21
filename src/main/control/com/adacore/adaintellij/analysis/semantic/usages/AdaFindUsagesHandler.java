package com.adacore.adaintellij.analysis.semantic.usages;

import java.util.*;
import java.util.stream.*;

import com.intellij.find.findUsages.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

import org.eclipse.lsp4j.Location;

import com.adacore.adaintellij.lsp.*;

import static com.adacore.adaintellij.Utils.*;
import static com.adacore.adaintellij.lsp.LSPUtils.*;

/**
 * Find-usages handler for Ada, powered by the
 * Ada Language Server (ALS).
 */
public final class AdaFindUsagesHandler extends FindUsagesHandler {

	/**
	 * The project in which this handler is used.
	 */
	private Project project;

	/**
	 * Constructs a new AdaFindUsagesHandler given a PSI element.
	 *
	 * @param element The element to use to contruct the handler.
	 */
	AdaFindUsagesHandler(@NotNull PsiElement element) {
		super(element);
		project = element.getProject();
	}

	/**
	 * Finds usages of the given element and passes them to the given
	 * processor. This method is used by the IDE when the standard
	 * find-usages action is called.
	 *
	 * @param element The element for which to find usages.
	 * @param processor The processor of found usages.
	 * @param options The find-usages options.
	 * @return The boolean value true.
	 */
	@Override
	public boolean processElementUsages(
		@NotNull PsiElement           element,
		@NotNull Processor<? super UsageInfo> processor,
		@NotNull FindUsagesOptions    options
	) {

		ApplicationManager.getApplication().runReadAction(() ->
			findReferences(element, false).forEach(reference ->
				processor.process(new UsageInfo(reference.getElement()))));

		return true;

	}

	/**
	 * Finds references of the given target element in the given search
	 * scope, specifically for highlighting those references in the open
	 * document editor, and returns them in a collection.
	 *
	 * @param target The target to which to find references.
	 * @param searchScope The scope in which to find references.
	 * @return The set of references to the given target element.
	 */
	@NotNull
	@Override
	public Collection<PsiReference> findReferencesToHighlight(
		@NotNull PsiElement  target,
		@NotNull SearchScope searchScope
	) {
		return findReferences(target, true)
			.filter(reference -> searchScope.contains(
				reference.getElement().getContainingFile().getVirtualFile()))
			.collect(Collectors.toSet());
	}

	/**
	 * General purpose method that finds references to the given target
	 * element.
	 * Makes a `textDocument/references` request to the ALS to get the
	 * list of references to the given element, and returns them as a
	 * stream.
	 *
	 * @param target The target to which to find references.
	 * @param includeDefinition Whether or not to include the target
	 *                          element itself with the returned
	 *                          references.
	 */
	@NotNull
	private Stream<PsiReference> findReferences(
		@NotNull PsiElement target,
		         boolean    includeDefinition
	) {

		PsiFile  file        = target.getContainingFile();
		Document document    = getPsiFileDocument(file);
		String   documentUri = file.getVirtualFile().getUrl();

		if (document == null) { return Stream.empty(); }

		// Make the request and wait for the result

		AdaLSPServer lspServer = AdaLSPDriver.getServer(project);

		if (lspServer == null) { return Stream.empty(); }

		List<Location> referenceLocations = lspServer.references(
			documentUri, offsetToPosition(document, target.getTextOffset()), includeDefinition);

		// Map the returned locations to PSI references and
		// return them as a stream

		return referenceLocations
			.stream()
			.map(location -> {

				VirtualFile locationFile = findFileByUrlString(location.getUri());

				if (locationFile == null) { return null; }

				Document locationDocument = getVirtualFileDocument(locationFile);
				PsiFile  locationPsiFile  = getVirtualFilePsiFile(project, locationFile);

				if (locationDocument == null || locationPsiFile == null) { return null; }

				return locationPsiFile.findReferenceAt(
					positionToOffset(locationDocument, location.getRange().getStart()));

			})
			.filter(Objects::nonNull);

	}

}
