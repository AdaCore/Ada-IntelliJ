package com.adacore.adaintellij.project.template;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable data class representation of an Ada
 * project template.
 */
public final class ProjectTemplateDescriptor {
	
	/**
	 * The resource directory containing the project
	 * template files.
	 */
	public final VirtualFile templateDirectory;
	
	/**
	 * The project template data.
	 */
	public final String             templateName;
	public final String             templateCategory;
	public final String             templateDescription;
	public final String             projectFileName;
	public final TemplateVariable[] templateVariables;
	
	/**
	 * Constructs a new ProjectTemplateDescriptor given a
	 * resource directory and template data.
	 *
	 * @param templateDirectory The template resource directory.
	 * @param templateName The template name.
	 * @param templateCategory The template category.
	 * @param templateDescription The template description.
	 * @param projectFileName The name of the project file in
	 *                        the project template.
	 * @param templateVariables The list of variables that can
	 *                          be configured for the template.
	 */
	public ProjectTemplateDescriptor(
		@NotNull VirtualFile        templateDirectory,
		@NotNull String             templateName,
		@NotNull String             templateCategory,
		@NotNull String             templateDescription,
		@NotNull String             projectFileName,
		@NotNull TemplateVariable[] templateVariables
	) {
		this.templateDirectory   = templateDirectory;
		this.templateName        = templateName;
		this.templateCategory    = templateCategory;
		this.templateDescription = templateDescription;
		this.projectFileName     = projectFileName;
		this.templateVariables   = templateVariables;
	}
	
	/**
	 * Returns a string representation of this project template
	 * descriptor.
	 *
	 * @return A string representation of this project template
	 *         descriptor.
	 */
	@Override
	public String toString() { return templateName; }
	
	/**
	 * Immutable data class representation of a project template
	 * variable.
	 */
	public static class TemplateVariable {
		
		/**
		 * The template variable data.
		 */
		public final String name;
		public final String defaultValue;
		public final String description;
		
		/**
		 * Constructs a new TemplateVariable given some data.
		 *
		 * @param name The name of the variable.
		 * @param defaultValue The default value of the variable.
		 * @param description The description of the variable.
		 */
		public TemplateVariable(
			@NotNull String name,
			@NotNull String defaultValue,
			@NotNull String description
		) {
			this.name         = name;
			this.defaultValue = defaultValue;
			this.description  = description;
		}
		
	}
	
	/**
	 * Immutable data class representation of a project template
	 * variable whose value can only be one of a finite set of
	 * options.
	 */
	public static final class TemplateVariableWithOptions extends TemplateVariable {
		
		/**
		 * The possible values for the variable.
		 */
		public final String[] options;
		
		/**
		 * Constructs a new TemplateVariableWithOptions given
		 * some data.
		 *
		 * @param name The name of the variable.
		 * @param defaultValue The default value of the variable.
		 * @param description The description of the variable.
		 * @param options The possible values for the variable.
		 */
		public TemplateVariableWithOptions(
			@NotNull String   name,
			@NotNull String   defaultValue,
			@NotNull String   description,
			@NotNull String[] options
		) {
			super(name, defaultValue, description);
			this.options = options;
		}
		
	}
	
	/**
	 * Immutable data class representation of a project template
	 * variable setting.
	 */
	public static final class TemplateVariableSetting {
		
		/**
		 * The variable setting data.
		 */
		public final String name;
		public final String value;
		
		/**
		 * Constructs a new TemplateVariableSetting given some
		 * data.
		 *
		 * @param name The name of the template variable.
		 * @param value The value assigned to the variable.
		 */
		public TemplateVariableSetting(
			@NotNull String name,
			@NotNull String value
		) {
			this.name  = name;
			this.value = value;
		}
		
	}
	
	/**
	 * Project template descriptor builder.
	 */
	public static final class Builder {
		
		/**
		 * Data for the project template descriptor being built.
		 */
		private VirtualFile            templateDirectory;
		private String                 templateName        = "";
		private String                 templateCategory    = "";
		private StringBuilder          templateDescription = new StringBuilder();
		private String                 projectFileName     = "";
		private List<TemplateVariable> templateVariables   = new ArrayList<>();
		
		/**
		 * Constructs a new Builder given a resource directory for
		 * the project template descriptor being built.
		 *
		 * @param templateDirectory The template resource directory.
		 */
		public Builder(@NotNull VirtualFile templateDirectory) {
			this.templateDirectory = templateDirectory;
		}
		
		/**
		 * Sets the project template name to the given name.
		 *
		 * @param templateName The template name to set.
		 */
		public void setTemplateName(@NotNull String templateName) {
			this.templateName = templateName;
		}
		
		/**
		 * Sets the project template category to the given category.
		 *
		 * @param templateCategory The template category to set.
		 */
		public void setTemplateCategory(@NotNull String templateCategory) {
			this.templateCategory = templateCategory;
		}
		
		/**
		 * Appends the given text to the project template description.
		 *
		 * @param text The text to append.
		 */
		public void appendToTemplateDescription(@NotNull String text) {
			templateDescription.append(text);
		}
		
		/**
		 * Sets the project file name to the given name.
		 *
		 * @param projectFileName The project file name to set.
		 */
		public void setProjectFileName(@NotNull String projectFileName) {
			this.projectFileName = projectFileName;
		}
		
		/**
		 * Adds the given template variable to the list of variables
		 * of the template descriptor being built.
		 *
		 * @param variable The variable to add.
		 */
		public void addTemplateVariable(@NotNull TemplateVariable variable) {
			this.templateVariables.add(variable);
		}
		
		/**
		 * Builds and returns the project template descriptor.
		 *
		 * @return The built project template descriptor.
		 */
		@NotNull
		public ProjectTemplateDescriptor build() {
			return new ProjectTemplateDescriptor(
				templateDirectory,
				templateName,
				templateCategory,
				templateDescription.toString(),
				projectFileName,
				templateVariables.toArray(new TemplateVariable[0])
			);
		}
		
	}
	
}
