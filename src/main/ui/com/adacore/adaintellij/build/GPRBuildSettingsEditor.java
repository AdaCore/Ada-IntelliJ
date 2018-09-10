package com.adacore.adaintellij.build;

import javax.swing.*;
import java.awt.*;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

/**
 * Settings editor UI for the gprbuild run configurations.
 *
 * TODO: Move gprbuild path setting to a global plugin settings page.
 */
public final class GPRBuildSettingsEditor extends SettingsEditor<GPRBuildRunConfiguration> {
	
	/**
	 * UI components.
	 */
	private JPanel panel;
	private TextFieldWithBrowseButton gprbuildPathField;
	private TextFieldWithBrowseButton customGprFilePathField;
	
	/**
	 * Constructs a new GPRBuildSettingsEditor.
	 */
	GPRBuildSettingsEditor() {
		gprbuildPathField      = new TextFieldWithBrowseButton();
		customGprFilePathField = new TextFieldWithBrowseButton();
	}
	
	/**
	 * Restores this editor from the given run configuration.
	 *
	 * @param gprBuildRunConfiguration The configuration from which to
	 *                                 reset this editor.
	 */
	@Override
	protected void resetEditorFrom(@NotNull GPRBuildRunConfiguration gprBuildRunConfiguration) {
		gprbuildPathField.setText(GPRbuildManager.getGprbuildPath());
		customGprFilePathField.setText(gprBuildRunConfiguration.getCustomGprFilePath());
	}
	
	/**
	 * Applies the data in this editor to the given run configuration.
	 *
	 * @param gprBuildRunConfiguration The configuration to which to
	 *                                 apply the data in this editor.
	 */
	@Override
	protected void applyEditorTo(@NotNull GPRBuildRunConfiguration gprBuildRunConfiguration) {
		GPRbuildManager.setGprBuildPath(gprbuildPathField.getText());
		gprBuildRunConfiguration.setCustomGprFilePath(customGprFilePathField.getText());
	}
	
	/**
	 * Sets up the UI of this editor, if it has not been done already.
	 *
	 * @return The main UI component of this editor.
	 */
	@NotNull
	@Override
	protected JComponent createEditor() {
		
		if (panel == null) {
			
			panel = new JPanel(new BorderLayout());
			LabeledComponent<TextFieldWithBrowseButton> gprbuildPathComponent      = new LabeledComponent<>();
			LabeledComponent<TextFieldWithBrowseButton> customGprFilePathComponent = new LabeledComponent<>();
			
			LayoutManager layoutManager = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
			panel.setLayout(layoutManager);
			
			FileChooserDescriptor descriptor = new FileChooserDescriptor(
				true,
				false,
				false,
				false,
				false,
				false
			);
			
			gprbuildPathField.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
			customGprFilePathField.addBrowseFolderListener(new TextBrowseFolderListener(descriptor));
			
			gprbuildPathComponent.setText("Path to gprbuild:");
			gprbuildPathComponent.setComponent(gprbuildPathField);
			
			customGprFilePathComponent.setText("Path to GPR file: (Leave empty to use the project's default project file)");
			customGprFilePathComponent.setComponent(customGprFilePathField);
			
			panel.add(gprbuildPathComponent);
			panel.add(Box.createVerticalStrut(10));
			panel.add(customGprFilePathComponent);
			
		}
		
		return panel;
	}
	
}
