package com.adacore.adaintellij.settings;

import javax.swing.*;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.build.GPRbuildManager;
import com.adacore.adaintellij.UIUtils;

/**
 * Global IDE settings for Ada development.
 */
public class AdaGlobalSettings implements Configurable {
	
	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;
	
	/**
	 * Child UI components.
	 */
	private TextFieldWithBrowseButton gprbuildPathField;
	
	/**
	 * Last set values.
	 */
	private String lastSetGprbuildPath;
	
	/**
	 * @see com.intellij.openapi.options.Configurable#getDisplayName()
	 */
	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() { return "Ada"; }
	
	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#createComponent()
	 */
	@Nullable
	@Override
	public JComponent createComponent() {
		
		// Set up the UI
		
		gprbuildPathField.addBrowseFolderListener(
			new TextBrowseFolderListener(UIUtils.SINGLE_FILE_CHOOSER_DESCRIPTOR));
		
		// Return the root panel
		
		return rootPanel;
		
	}
	
	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#isModified()
	 */
	@Override
	public boolean isModified() {
		return !gprbuildPathField.getText().equals(lastSetGprbuildPath);
	}
	
	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#apply()
	 */
	@Override
	public void apply() {
		
		String path = gprbuildPathField.getText();
		
		GPRbuildManager.setGprBuildPath(path);
		lastSetGprbuildPath = path;
		
	}
	
	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#reset()
	 */
	@Override
	public void reset() {
		
		String path = GPRbuildManager.getGprbuildPath();
		
		if (path == null) { path = ""; }
		
		gprbuildPathField.setText(path);
		lastSetGprbuildPath = path;
		
	}
	
}
