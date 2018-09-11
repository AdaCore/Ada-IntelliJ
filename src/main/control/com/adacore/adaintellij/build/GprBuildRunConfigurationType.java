package com.adacore.adaintellij.build;

import javax.swing.*;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;

import com.adacore.adaintellij.Icons;

/**
 * Run configuration type for GPRbuild run configurations.
 */
public final class GprBuildRunConfigurationType implements ConfigurationType {
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getDisplayName()
	 */
	@Override
	public String getDisplayName() { return "GPRbuild"; }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getConfigurationTypeDescription()
	 */
	@Override
	public String getConfigurationTypeDescription() { return "GPRbuild Run Configuration Type"; }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getIcon()
	 */
	@Override
	public Icon getIcon() { return Icons.GPRBUILD_RUN_CONFIGURATION; }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getId()
	 */
	@Override
	public String getId() { return "GPR_BUILD_CONFIGURATION"; }
	
	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getConfigurationFactories()
	 */
	@Override
	public ConfigurationFactory[] getConfigurationFactories() {
		return new ConfigurationFactory[] { new GPRBuildConfigurationFactory(this) };
	}
	
}
