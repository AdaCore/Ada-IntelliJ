package com.adacore.adaintellij.build;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for GPRbuild configurations.
 */
public final class GPRbuildConfigurationFactory extends ConfigurationFactory {

	/**
	 * Constructs a new GPRbuildConfigurationFactory.
	 *
	 * @param type The configuration type for this factory.
	 */
	GPRbuildConfigurationFactory(ConfigurationType type) { super(type); }

	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#createTemplateConfiguration(Project)
	 */
	@NotNull
	@Override
	public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
		return new GPRbuildConfiguration(project, this, "GPRbuild");
	}

	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#getName()
	 */
	@NotNull
	@Override
	public String getName() { return "GPRbuild Configuration Factory"; }

}
