package com.adacore.adaintellij;

import javax.swing.*;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

/**
 * Icons used across the Ada-IntelliJ plugin.
 */
public final class Icons {
	
	/**
	 * Icons for Ada file types.
	 */
	public static final Icon ADA_BODY_SOURCE_FILE       = IconLoader.getIcon("/icons/adb-icon.png");
	public static final Icon ADA_SPEC_SOURCE_FILE       = IconLoader.getIcon("/icons/ads-icon.png");
	public static final Icon ADA_GPR_SOURCE_FILE        = IconLoader.getIcon("/icons/gpr-icon.png");
	
	/**
	 * Icons for run configuration types.
	 */
	public static final Icon GPRBUILD_RUN_CONFIGURATION = AllIcons.Actions.Compile;
	public static final Icon MAIN_RUN_CONFIGURATION     = AllIcons.Actions.Execute;
	
	/**
	 * Icons for project-related UI.
	 */
	public static final Icon PROJECT_TEMPLATE_CATEGORY  = AllIcons.Nodes.Folder;
	public static final Icon PROJECT_TEMPLATE           = AllIcons.General.ProjectStructure;
	public static final Icon ADA_MODULE                 = AllIcons.Nodes.Module;
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private Icons() {}
	
}
