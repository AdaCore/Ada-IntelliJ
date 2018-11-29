package com.adacore.adaintellij.build;

import java.util.List;
import java.util.stream.Collectors;

import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.notification.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * Project run manager used to manage GPRbuild run configurations.
	 */
	private RunManagerImpl runManager;
	
	/**
	 * Constructs a new GPRbuildConfigurationManager.
	 *
	 * @param project The project to attach to the constructed manager.
	 * @param adaProject The Ada project component to attach to the
	 *                   constructed manager.
	 */
	public GPRbuildConfigurationManager(Project project, AdaProject adaProject) {
		this.project    = project;
		this.adaProject = adaProject;
		this.runManager = RunManagerImpl.getInstanceImpl(project);
	}
	
	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@NotNull
	@Override
	public String getComponentName() {
		return "com.adacore.adaintellij.build.GPRbuildConfigurationManager";
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
		
		// Get the list of GPRbuild run configurations
		
		List<RunConfiguration> configurations =
			runManager.getConfigurationsList(GPRbuildConfigurationType.INSTANCE);
		
		// If no configurations were found, create a default GPRbuild run
		// configuration and select it
		
		if (configurations.size() == 0) {
			
			RunnerAndConfigurationSettings settings =
				runManager.createConfiguration("Default GPRbuild Configuration",
					GPRbuildConfigurationType.INSTANCE.getConfigurationFactories()[0]);
			
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
	
	/**
	 * Returns the GPRbuildConfigurationManager project component of the given project.
	 *
	 * @param project The project for which to get the component.
	 * @return The project component.
	 */
	@NotNull
	public static GPRbuildConfigurationManager getInstance(@NotNull Project project) {
		return project.getComponent(GPRbuildConfigurationManager.class);
	}
	
	/**
	 * Adds the given listener to the run manager.
	 *
	 * @param listener The listener to add.
	 */
	public void addRunManagerListener(@NotNull RunManagerListener listener) {
		runManager.addRunManagerListener(listener);
	}
	
	/**
	 * Returns the list of all registered GPRbuild configurations.
	 *
	 * @return The list of GPRbuild configurations.
	 */
	@NotNull
	public List<GPRbuildConfiguration> getAllConfigurations() {
		return runManager.getConfigurationsList(GPRbuildConfigurationType.INSTANCE)
			.stream()
			.map(runConfiguration -> (GPRbuildConfiguration)runConfiguration)
			.collect(Collectors.toList());
	}
	
	/**
	 * Returns the currently selected GPRbuild configuration, or null
	 * if no configuration is selected or if the selected configuration
	 * is not a GPRbuild configuration.
	 *
	 * @return The currently selected GPRbuild configuration, or null if
	 *         no such configuration is selected.
	 */
	@Nullable
	public GPRbuildConfiguration getSelectedConfiguration() {
	
		RunnerAndConfigurationSettings settings =
			runManager.getSelectedConfiguration();
	
		if (settings == null) { return null; }
		
		RunConfiguration configuration = settings.getConfiguration();
		
		return configuration instanceof GPRbuildConfiguration ?
			(GPRbuildConfiguration)configuration : null;
		
	}
	
	/**
	 * Sets the selected configuration to the given GPRbuild configuration.
	 *
	 * @param configuration The GPRbuild configuration to select.
	 */
	public void setSelectedConfiguration(@NotNull GPRbuildConfiguration configuration) {
	
		runManager.getAllSettings()
			.stream()
			.filter(settings -> settings.getConfiguration().equals(configuration))
			.findFirst()
			.ifPresent(runManager::setSelectedConfiguration);
	
	}
	
}
