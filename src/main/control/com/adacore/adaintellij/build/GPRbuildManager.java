package com.adacore.adaintellij.build;

import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.notifications.AdaIJNotification;

/**
 * Application component handling GPRbuild-related tasks.
 */
public final class GPRbuildManager implements ApplicationComponent {
	
	/**
	 * The extension-less name of the gprbuild executable.
	 */
	private static final String GPRBUILD_NAME = "gprbuild";
	
	/**
	 * The gprbuild path in the system.
	 */
	private static String gprbuildPath;
	
	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@Override
	public String getComponentName() { return "com.adacore.adaintellij.build.GPRbuildManager"; }
	
	/**
	 * @see com.intellij.openapi.components.BaseComponent#initComponent()
	 */
	@Override
	public void initComponent() {
		
		char   fileSeparator = System.getProperty("file.separator").charAt(0);
		String pathSeparator = System.getProperty("path.separator");
		
		String[] paths = System.getenv("PATH").split(pathSeparator);
		
		for (String path : paths) {
			
			VirtualFile directory = LocalFileSystem.getInstance().findFileByPath(path);
			
			// Shouldn't happen in a PATH variable but just to be safe
			if (directory == null || !directory.isDirectory()) { continue; }
			
			for (VirtualFile file : directory.getChildren()) {
				
				if (file.isDirectory()) { continue; }
				
				if (GPRBUILD_NAME.equals(file.getNameWithoutExtension())) {
					
					// Set the gprbuild path
					
					StringBuilder newGprbuildPath = new StringBuilder(path);
					
					if (path.charAt(path.length() - 1) != fileSeparator) {
						newGprbuildPath.append(fileSeparator);
					}
					
					gprbuildPath = newGprbuildPath.append(file.getName()).toString();
					
					// Notify the user that a compiler was found on the path
					Notifications.Bus.notify(new AdaIJNotification(
						"Compiler Found on the PATH",
						"Using the following gprbuild for compilation:\n" + gprbuildPath,
						NotificationType.INFORMATION
					));
					
					return;
					
				}
				
			}
			
		}
		
		// Notify the user that no compiler was found on the path
		Notifications.Bus.notify(new AdaIJNotification(
			"No Compiler Found on the PATH",
			"Please set the gprbuild path in `Run | Edit Configurations`.",
			NotificationType.WARNING
		));
		
	}
	
	/**
	 * Returns the gprbuild path in the system.
	 *
	 * @return The gprbuild path.
	 */
	@Nullable
	public static String getGprbuildPath() { return gprbuildPath; }
	
	/**
	 * Sets the gprbuilc path in the system.
	 *
	 * @param path The new gprbuild path.
	 */
	public static void setGprBuildPath(String path) { gprbuildPath = path; }
	
}
