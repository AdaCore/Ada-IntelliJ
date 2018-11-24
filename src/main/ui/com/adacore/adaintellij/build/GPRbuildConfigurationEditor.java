package com.adacore.adaintellij.build;

import javax.swing.*;

import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.UIUtils;
import com.adacore.adaintellij.AdaIntelliJUI;

/**
 * Configuration editor UI for GPRbuild configurations.
 */
public class GPRbuildConfigurationEditor extends AdaIntelliJUI {
	
	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;
	
	/**
	 * Child UI components.
	 */
	private TextFieldWithBrowseButton customGprFilePathField;
	private JBTextField               buildArgumentsField;
	private JScrollPane               scenarioScrollPane;
	
	/**
	 * External UI components.
	 */
	private ScenarioSettingsView scenarioSettingsView;
	
	/**
	 * Constructs a new GPRbuildConfigurationEditor.
	 */
	GPRbuildConfigurationEditor() {
		this(null);
	}
	
	/**
	 * Constructs a new GPRbuildConfigurationEditor given an optional
	 * parent UI.
	 *
	 * @param parentUI The UI in which to embed the constructed UI, or null
	 *                 to construct a standalone UI view.
	 */
	GPRbuildConfigurationEditor(@Nullable AdaIntelliJUI parentUI) {
		
		super(parentUI);
		
		// Set up GPR file path file chooser
		
		customGprFilePathField.addBrowseFolderListener(
			new TextBrowseFolderListener(UIUtils.SINGLE_FILE_CHOOSER_DESCRIPTOR));
		
		// Set up the scenario variable settings view
		
		scenarioSettingsView = new ScenarioSettingsView(this);
		
		scenarioScrollPane.getVerticalScrollBar().setUnitIncrement(15);
		scenarioScrollPane.getViewport().add(scenarioSettingsView.getUIRoot());
		
	}
	
	/**
	 * Restores this editor from the given run configuration.
	 *
	 * @param gprBuildConfiguration The configuration from which to
	 *                              reset this editor.
	 */
	void resetEditorFrom(@NotNull GPRbuildConfiguration gprBuildConfiguration) {
		customGprFilePathField.setText(gprBuildConfiguration.getCustomGprFilePath());
		buildArgumentsField.setText(gprBuildConfiguration.getGprbuildArguments());
		scenarioSettingsView.setScenarioVariables(gprBuildConfiguration.getScenarioVariables());
	}
	
	/**
	 * Applies the data in this editor to the given run configuration.
	 *
	 * @param gprBuildConfiguration The configuration to which to
	 *                              apply the data in this editor.
	 */
	void applyEditorTo(@NotNull GPRbuildConfiguration gprBuildConfiguration) {
		gprBuildConfiguration.setCustomGprFilePath(customGprFilePathField.getText());
		gprBuildConfiguration.setGprbuildArguments(buildArgumentsField.getText());
		gprBuildConfiguration.setScenarioVariables(scenarioSettingsView.getScenarioVariables());
	}
	
	/**
	 * @see com.adacore.adaintellij.AdaIntelliJUI#getUIRoot()
	 */
	@NotNull
	@Override
	public JComponent getUIRoot() { return rootPanel; }
	
	/**
	 * Adapter of this editor to the `SettingsEditor` class.
	 */
	public static class SettingsEditorAdapter extends SettingsEditor<GPRbuildConfiguration> {
		
		/**
		 * Internal instance of GPRbuildConfigurationEditor to be adapted.
		 */
		private GPRbuildConfigurationEditor editor;
		
		/**
		 * Restores this editor from the given run configuration.
		 *
		 * @param gprBuildConfiguration The configuration from which to
		 *                              reset this editor.
		 */
		@Override
		protected void resetEditorFrom(@NotNull GPRbuildConfiguration gprBuildConfiguration) {
			editor.resetEditorFrom(gprBuildConfiguration);
		}
		
		/**
		 * Applies the data in this editor to the given run configuration.
		 *
		 * @param gprBuildConfiguration The configuration to which to
		 *                              apply the data in this editor.
		 */
		@Override
		protected void applyEditorTo(@NotNull GPRbuildConfiguration gprBuildConfiguration) {
			editor.applyEditorTo(gprBuildConfiguration);
		}
		
		/**
		 * Sets up the UI of this editor.
		 *
		 * @return The root UI component of this editor.
		 */
		@NotNull
		@Override
		protected JComponent createEditor() {
			
			// Initialize the UI
			
			editor = new GPRbuildConfigurationEditor();
			
			// Return the root component
			
			return editor.getUIRoot();
			
		}
		
	}
	
}
