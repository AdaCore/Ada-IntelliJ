package com.adacore.adaintellij.build;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for GPRbuild configurations.
 */
public final class GPRBuildConfigurationFactory extends ConfigurationFactory {
	
	/**
	 * Constructs a new GPRBuildConfigurationFactory.
	 *
	 * @param type The configuration type for this factory.
	 */
	GPRBuildConfigurationFactory(ConfigurationType type) { super(type); }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#createTemplateConfiguration(Project)
	 */
	@Override
	public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
		return new GPRBuildRunConfiguration(project, this, "GPRbuild");
	}
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#getName()
	 */
	@Override
	public String getName() {
		return "GPRbuild Configuration Factory";
	}
	
}
