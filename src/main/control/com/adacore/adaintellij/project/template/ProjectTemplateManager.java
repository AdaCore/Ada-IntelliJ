package com.adacore.adaintellij.project.template;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.ResourceManager;
import com.adacore.adaintellij.Utils;

import static com.adacore.adaintellij.project.template.ProjectTemplateDescriptor.TemplateVariableSetting;

/**
 * Ada project template manager.
 */
public final class ProjectTemplateManager {

	/**
	 * Project template descriptor file extension.
	 */
	static final String TEMPLATE_DESCRIPTOR_FILE_EXTENSION = "gpt";

	/**
	 * Class-wide logger for the ProjectTemplateManager class.
	 */
	private static Logger LOGGER = Logger.getInstance(Utils.class);

	/**
	 * Resource directory containing all project templates.
	 */
	private static final VirtualFile TEMPLATES_ROOT_DIRECTORY =
		ResourceManager.getResourceDirectory("/project-templates");

	/**
	 * Template variable text markers.
	 */
	private static final String TEMPLATE_VARIABLE_PREFIX = "@_";
	private static final String TEMPLATE_VARIABLE_SUFFIX = "_@";

	/**
	 * Returns template descriptors for all available project
	 * templates.
	 *
	 * @return All project template descriptors.
	 * @throws Exception If the root resource template directory
	 *                   is not found.
	 */
	@NotNull
	public static List<ProjectTemplateDescriptor> getAllProjectTemplates() throws Exception {

		// Check the root resource template directory

		if (TEMPLATES_ROOT_DIRECTORY == null) {

			Exception exception = new Exception("Could not find project templates directory");

			LOGGER.error(exception);
			throw exception;

		}

		// Map each child directory to its corresponding descriptor,
		// then collect and return the results

		return Stream.of(TEMPLATES_ROOT_DIRECTORY.getChildren())
			.map(templateDirectory -> {

				if (!templateDirectory.isDirectory()) { return null; }

				try {

					return ProjectTemplateFileParser.getProjectTemplateDescriptor(templateDirectory);

				} catch (Exception exception) {

					LOGGER.error(exception);
					return null;

				}

			})
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

	}

	/**
	 * Apply the given project template descriptor, with the given
	 * template variable settings, to the module represented by the
	 * given root model.
	 *
	 * @param templateDescriptor The descriptor of the template to
	 *                           apply.
	 * @param variableSettings The variable settings to apply.
	 * @param modifiableRootModel The root model of the module to
	 *                            which to apply the template.
	 * @throws ConfigurationException If something goes wrong while
	 *                                applying the template.
	 */
	public static void applyProjectTemplate(
		@NotNull ProjectTemplateDescriptor     templateDescriptor,
		@NotNull List<TemplateVariableSetting> variableSettings,
		@NotNull ModifiableRootModel           modifiableRootModel
	) throws ConfigurationException {

		// Add the project base directory as a content root

		VirtualFile projectBaseDir = modifiableRootModel.getProject().getBaseDir();

		modifiableRootModel.addContentEntry(projectBaseDir);

		// Copy all files in the project template directory
		// except the template descriptor file

		for (VirtualFile child : templateDescriptor.templateDirectory.getChildren()) {

			if (TEMPLATE_DESCRIPTOR_FILE_EXTENSION.equals(child.getExtension())) {
				continue;
			}

			try {
				VfsUtil.copy(null, child, projectBaseDir);
			} catch (IOException exception) {
				throw new ConfigurationException(exception.getMessage());
			}

		}

		final AtomicBoolean failed       = new AtomicBoolean(false);
		final StringBuilder errorMessage = new StringBuilder();

		// Recursively iterate over the copied files

		VfsUtil.iterateChildrenRecursively(projectBaseDir, null, fileOrDir -> {

			String fileOrDirName = fileOrDir.getName();

			// For each variable setting, check if the variable name
			// appears in the filename, and if it does then substitute
			// the variable name with its assigned value

			for (TemplateVariableSetting variableSetting : variableSettings) {

				Matcher nameMatcher =
					getPatternFromVariable(variableSetting.name).matcher(fileOrDirName);

				if (nameMatcher.find()) {

					try {
						fileOrDir.rename(null, nameMatcher.replaceAll(variableSetting.value));
					} catch (IOException exception) {
						failed.set(true);
						errorMessage.append(exception.getMessage());
						return false;
					}

				}

			}

			// If the entry is a directory then move on
			// to the next entry

			if (fileOrDir.isDirectory()) { return true; }

			// Get the file's corresponding document

			final Document document = Utils.getVirtualFileDocument(fileOrDir);

			// If we were unable to get the file document (e.g.
			// the file is in binary format) then move on to the
			// next entry

			if (document == null) { return true; }

			// For each variable setting, check if the variable name
			// appears in the document, and if it does then substitute
			// all occurrences of the variable name with its assigned
			// value

			CommandProcessor commandProcessor = CommandProcessor.getInstance();

			final CharSequence text = document.getCharsSequence();

			for (final TemplateVariableSetting variableSetting : variableSettings) {

				Pattern variablePattern = getPatternFromVariable(variableSetting.name);

				while (true) {

					final Matcher contentMatcher = variablePattern.matcher(text);

					if (!contentMatcher.find()) { break; }

					commandProcessor.executeCommand(
						modifiableRootModel.getProject(),
						() -> document.replaceString(
							contentMatcher.start(),
							contentMatcher.end(),
							variableSetting.value
						),
						null,
						null,
						UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION,
						false
					);

				}

			}

			return true;

		});

		if (failed.get()) {
			throw new ConfigurationException(errorMessage.toString());
		}

	}

	/**
	 * Returns a project template variable regex for the
	 * given variable name.
	 *
	 * @param name The name of the template variable.
	 * @return The corresponding template variable regex.
	 */
	private static Pattern getPatternFromVariable(@NotNull String name) {
		return Pattern.compile(
			TEMPLATE_VARIABLE_PREFIX + name + TEMPLATE_VARIABLE_SUFFIX,
			Pattern.CASE_INSENSITIVE
		);
	}

}
