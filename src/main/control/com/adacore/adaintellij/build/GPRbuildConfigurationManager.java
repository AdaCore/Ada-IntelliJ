package com.adacore.adaintellij.build;

import java.util.List;

import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.notification.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.adacore.adaintellij.project.AdaProject;

/**
 * Project component handling GPRbuild configuration management.
 */
public final class GPRbuildConfigurationManager implements ProjectComponent {
	
	/**
	 * The project to which this component belongs.
	 */
	private Project project;
	
	/**
	 * The corresponding Ada project component.
	 */
	private AdaProject adaProject;
	
	/**
	 * Constructs a new GPRbuildConfigurationManager.
	 *
	 * @param project The project to attach to the constructed manager.
	 */
	public GPRbuildConfigurationManager(Project project, AdaProject adaProject) {
		this.project    = project;
		this.adaProject = adaProject;
	}
	
	/**
	 * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
	 *
	 * Checks the run manager for GPRbuild run configurations, and if no
	 * configurations are found, creates a default one.
	 */
	@Override
	public void projectOpened() {
		
		if (!adaProject.isAdaProject()) { return; }
		
		RunManagerImpl runManager = RunManagerImpl.getInstanceImpl(project);
		
		// Get the list of GPRbuild run configurations
		
		List<RunConfiguration> configurations =
			runManager.getConfigurationsList(GPRbuildRunConfigurationType.INSTANCE);
		
		// If no configurations were found, create a default GPRbuild run
		// configuration and select it
		
		if (configurations.size() == 0) {
			
			RunnerAndConfigurationSettings settings =
				runManager.createConfiguration("Default GPRbuild Configuration",
					GPRbuildRunConfigurationType.INSTANCE.getConfigurationFactories()[0]);
			
			runManager.addConfiguration(settings);
			runManager.setSelectedConfiguration(settings);
			
			// Notify the user that a default gprbuild configuration was created
			Notifications.Bus.notify(new AdaIJNotification(
				"No gprbuild configurations detected",
				"A default gprbuild configuration was created and" +
					" can be edited in `Run | Edit Configurations...`.",
				NotificationType.INFORMATION
			));
			
		}
		
	}
	
}
