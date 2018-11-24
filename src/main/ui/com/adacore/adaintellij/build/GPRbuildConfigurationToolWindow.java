package com.adacore.adaintellij.build;

import javax.swing.*;

import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.UIUtils;
import com.adacore.adaintellij.AdaIntelliJUI;

/**
 * GPRbuild configuration tool window.
 */
public class GPRbuildConfigurationToolWindow extends AdaIntelliJUI {
	
	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;
	
	/**
	 * Child UI components.
	 */
	private JPanel  configurationsPanel;
	private JPanel  configurationSettingsPanel;
	private JButton discardButton;
	private JButton applyButton;
	
	/**
	 * External UI components.
	 */
	private GPRbuildConfigurationEditor configurationEditor;
	
	/**
	 * The GPRbuild configuration manager project component of this
	 * view's project.
	 */
	private GPRbuildConfigurationManager gprbuildConfigurationManager;
	
	/**
	 * Constructs a new GPRbuildConfigurationToolWindow given a project.
	 *
	 * @param project The project to which this tool window belongs.
	 */
	GPRbuildConfigurationToolWindow(@NotNull Project project) {
		this(project, null);
	}
	
	/**
	 * Constructs a new GPRbuildConfigurationToolWindow given a project
	 * and an optional parent UI.
	 *
	 * @param project The project to which this tool window belongs.
	 * @param parentUI The UI in which to embed the constructed UI, or null
	 *                 to construct a standalone UI view.
	 */
	GPRbuildConfigurationToolWindow(@NotNull Project project, @Nullable AdaIntelliJUI parentUI) {
		
		super(parentUI);
		
		// Set up the GPRbuild configuration combo-box view
		
		UIUtils.addTitledBorder(configurationsPanel);

		configurationsPanel.add(
			new GPRbuildConfigurationComboBoxView(project, this).getUIRoot());
		
		// Set up the GPRbuild configuration editor view
		
		UIUtils.addTitledBorder(configurationSettingsPanel);
		
		configurationEditor = new GPRbuildConfigurationEditor();
		
		configurationSettingsPanel.add(configurationEditor.getUIRoot());
		
		// Get the project's GPRbuild configuration manager and
		// add a listener to reset configuration editor view
		// on configuration selection change
		
		gprbuildConfigurationManager = GPRbuildConfigurationManager.getInstance(project);
	
		gprbuildConfigurationManager.addRunManagerListener(new RunManagerListener() {
			
			/**
			 * Called when a different configuration is selected.
			 */
			@Override
			public void runConfigurationSelected() { resetConfigurationEditor(); }
			
			/**
			 * Called when a configuration is changed.
			 *
			 * @param settings The changed configuration's settings.
			 */
			@Override
			public void runConfigurationChanged(@NotNull RunnerAndConfigurationSettings settings) {
				
				if (settings.getConfiguration().equals(
					gprbuildConfigurationManager.getSelectedConfiguration()))
				{
					resetConfigurationEditor();
				}
				
			}
			
		});
		
		// Reset the configuration editor for the first time
		
		resetConfigurationEditor();
		
		// Add discard/apply button listeners
		
		discardButton.addActionListener(actionEvent -> resetConfigurationEditor());
		applyButton.addActionListener(actionEvent -> {
			
			GPRbuildConfiguration configuration =
				gprbuildConfigurationManager.getSelectedConfiguration();
			
			if (configuration == null) { return; }
			
			configurationEditor.applyEditorTo(configuration);
			
		});
		
	}
	
	/**
	 * @see com.adacore.adaintellij.AdaIntelliJUI#getUIRoot()
	 */
	@NotNull
	@Override
	public JComponent getUIRoot() { return rootPanel; }
	
	/**
	 * Resets the configuration settings section of this tool window from
	 * the currently selected GPRbuild configuration.
	 */
	private void resetConfigurationEditor() {
		
		GPRbuildConfiguration configuration =
			gprbuildConfigurationManager.getSelectedConfiguration();
		
		if (configuration == null) { return; }
		
		configurationEditor.resetEditorFrom(configuration);
		
	}
	
}
