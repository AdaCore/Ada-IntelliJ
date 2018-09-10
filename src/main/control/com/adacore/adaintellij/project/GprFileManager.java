package com.adacore.adaintellij.project;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.dialogs.ListChooserDialog;
import com.adacore.adaintellij.file.GprFileType;
import com.adacore.adaintellij.notifications.AdaIJNotification;

/**
 * Project component handling everything related to GPR files.
 */
public final class GprFileManager implements ProjectComponent {
	
	/**
	 * The project to which this component belongs.
	 */
	private Project project;
	
	/**
	 * A list of paths to GPR files in this project.
	 */
	private List<String> gprFilePaths;
	
	/**
	 * The index, in gprFilePaths, of the GPR file path that is set
	 * as the project's default GPR file, or -1 if no such path exists.
	 */
	private int defaultGprFilePathIndex = -1;
	
	/**
	 * Constructs a new GPR file manager given a project.
	 *
	 * @param project The project to attach to the constructed manager.
	 */
	public GprFileManager(Project project) { this.project = project; }
	
	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@Override
	@NotNull
	public String getComponentName() { return "com.adacore.adaintellij.project.GprFileManager"; }
	
	/**
	 * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
	 */
	@Override
	public void projectOpened() {
		
		locateGprFiles(() -> System.out.println("Done"));
		
	}
	
	/**
	 * Iterates over all files in the project's file hierarchy and stores
	 * the paths of all those that have the .gpr file extension.
	 *
	 * This method also notifies the user when no GPR files are found or
	 * when only one GPR file is found and will be automatically used as
	 * the project's default GPR file.
	 *
	 * In case multiple GPR files are found, this method displays a dialog
	 * allowing the user to choose which GPR file to use as the project's
	 * default GPR file.
	 *
	 * This method takes a Runnable that may be null and whose purpose is
	 * to control how the method behaves in case a default GPR file chooser
	 * dialog needs to be displayed:
	 * - If the Runnable is null, then the dialog must be displayed
	 *   synchronously (using Application#invokeAndWait) which blocks
	 *   the execution of the thread
	 * - If the Runnable is not null, then the dialog must be displayed
	 *   asynchronously (using Application#invokeLater) which does not
	 *   block the execution of the thread
	 *
	 * Ideally, this method should always block the execution, but using
	 * invokeAndWait occasionally causes the IDE to hang indefinitely when
	 * this method is called on project load, which is why this hacky
	 * approach is used for now. See:
	 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000654520-How-to-show-a-blocking-dialog-on-project-load
	 *
	 * @param doneRunnable The runnable to run (if not null) at the end of
	 *                     the execution of this method.
	 */
	private void locateGprFiles(@Nullable Runnable doneRunnable) {
		
		String gprFileExtension = GprFileType.INSTANCE.getDefaultExtension();
		
		gprFilePaths = new ArrayList<>();
		
		VirtualFile baseDirectory = project.getBaseDir();
		
		VfsUtilCore.iterateChildrenRecursively(
			baseDirectory,
			null,
			fileOrDir -> {
				
				if (gprFileExtension.equals(fileOrDir.getExtension())) {
					gprFilePaths.add(fileOrDir.getPath());
				}
				
				return true;
				
			}
		);
		
		switch (gprFilePaths.size()) {
			
			case 0: {
				
				defaultGprFilePathIndex = -1;
				
				Notifications.Bus.notify(new AdaIJNotification(
					"No Project File Found",
					"Add a GPR file to the project structure, or" +
						" specify one in `Run | Edit Configurations...`",
					NotificationType.WARNING
				));
				
				break;
				
			}
			
			case 1: {
				
				defaultGprFilePathIndex = 0;
				
				Notifications.Bus.notify(new AdaIJNotification(
					"Project File Found",
					"Using the following project file:\n" + gprFilePaths.get(0),
					NotificationType.INFORMATION
				));
				
				break;
				
			}
			
			default: {
				
				ListChooserDialog<String> dialog = new ListChooserDialog<>(
					"Multiple GPR files were found in this project.\n" +
						"Please choose one to be used as the default project file:",
					gprFilePaths,
					"You may still set up custom build configurations that use other GPR files.",
					selectedPath -> defaultGprFilePathIndex = gprFilePaths.indexOf(selectedPath),
					() -> defaultGprFilePathIndex = -1,
					ListSelectionModel.SINGLE_SELECTION
				);
				
				if (doneRunnable == null) {
					
					ApplicationManager.getApplication().invokeAndWait(dialog::display);
					
				} else {
					
					ApplicationManager.getApplication().invokeLater(() -> {
						
						dialog.display();
						
						doneRunnable.run();
						
					});
					
				}
				
				return;
				
			}
			
		}
		
		if (doneRunnable != null) { doneRunnable.run(); }
		
	}
	
	/**
	 * Returns the path of the default project GPR file, or null if
	 * no such file exists.
	 *
	 * @return The default project GPR file path, or null.
	 */
	@Nullable
	public String defaultGprFilePath() {
		
		// If no default GPR file exists, try to locate GPR files again
		if (defaultGprFilePathIndex == -1) {
			locateGprFiles(null);
		}
		
		// Return active path, or null if no gpr files found
		return defaultGprFilePathIndex == -1 ? null : gprFilePaths.get(defaultGprFilePathIndex);
		
	}
	
}
