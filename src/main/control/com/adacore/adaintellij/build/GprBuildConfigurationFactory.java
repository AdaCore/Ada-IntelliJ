package com.adacore.adaintellij.build;

import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for gprbuild configurations.
 */
public final class GprBuildConfigurationFactory extends ConfigurationFactory {
	
	/**
	 * Constructs a new GprBuildConfigurationFactory.
	 *
	 * @param type The configuration type for this factory.
	 */
	GprBuildConfigurationFactory(ConfigurationType type) { super(type); }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#createTemplateConfiguration(Project)
	 */
	@Override
	public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
		return new GprBuildRunConfiguration(project, this, "GPR Build");
	}
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationFactory#getName()
	 */
	@Override
	public String getName() {
		return "GPR Build Configuration Factory";
	}
	
}
