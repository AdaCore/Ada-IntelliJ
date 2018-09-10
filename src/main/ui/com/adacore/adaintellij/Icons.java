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
	public static final Icon ADA_BODY_SOURCE_FILE       = IconLoader.getIcon("/icons/test-icon.png");
	public static final Icon ADA_SPEC_SOURCE_FILE       = IconLoader.getIcon("/icons/test-icon.png");
	public static final Icon ADA_GPR_SOURCE_FILE        = IconLoader.getIcon("/icons/test-icon.png");
	
	/**
	 * Icons for run configuration types.
	 */
	public static final Icon GPRBUILD_RUN_CONFIGURATION = AllIcons.General.Information;
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private Icons() {}
	
}
