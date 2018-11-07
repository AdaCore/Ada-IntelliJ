package com.adacore.adaintellij.project;

import java.util.*;
import java.util.function.Consumer;
import javax.swing.*;

import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.dialogs.ListChooserDialog;
import com.adacore.adaintellij.file.GPRFileType;
import com.adacore.adaintellij.notifications.AdaIJNotification;

/**
 * Project component handling everything related to GPR files.
 */
public final class GPRFileManager implements ProjectComponent {
	
	/**
	 * The project to which this component belongs.
	 */
	private Project project;
	
	/**
	 * The corresponding Ada project component.
	 */
	private AdaProject adaProject;
	
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
	 * The set of registered listeners for the event of setting the default
	 * GPR file path after it was unset.
	 * Every listener is identified by a unique string key that can be used
	 * to register/unregister the listener (see `addGprFileSetListener` and
	 * `removeGprFileSetListener`).
	 * To avoid collisions, all listeners registered from within a class
	 * should use a key prefixed by the fully qualified class name of that
	 * class, for example:
	 * `com.adacore.adaintellij.project.GPRFileManager@myGprFileSetListener`
	 * Moreover, every class should assign different names to its own keys
	 * to avoid collisions.
	 */
	private Map<String, Consumer<String>> gprFileSetListeners = new HashMap<>();
	
	/**
	 * Similar to `gprFileSetListeners` but notifies its listeners every
	 * time the default GPR file path changes.
	 * Listeners can be registered/unregistered with `addGprFileChangeListener`
	 * and `removeGprFileChangeListener`.
	 * The same listener key conventions apply.
	 */
	private Map<String, Consumer<String>> gprFileChangeListeners = new HashMap<>();
	
	/**
	 * Constructs a new GPRFileManager given a project.
	 *
	 * @param project The project to attach to the constructed manager.
	 * @param adaProject The Ada project component to attach to the
	 *                   constructed manager.
	 */
	public GPRFileManager(Project project, AdaProject adaProject) {
		this.project    = project;
		this.adaProject = adaProject;
	}
	
	/**
	 * @see com.intellij.openapi.components.NamedComponent#getComponentName()
	 */
	@Override
	@NotNull
	public String getComponentName() { return "com.adacore.adaintellij.project.GPRFileManager"; }
	
	/**
	 * @see com.intellij.openapi.components.ProjectComponent#projectOpened()
	 */
	@Override
	public void projectOpened() {
		
		if (!adaProject.isAdaProject()) { return; }
		
		// Locate GPR files in the project
		
		locateGprFiles();
		
		switch (gprFilePaths.size()) {
			
			// If no files are found, reset the default GPR
			// file path index warn the user
			
			case 0: {
				
				setDefaultGprFilePathIndex(-1);
				
				Notifications.Bus.notify(new AdaIJNotification(
					"No Project File Found",
					"Add a GPR file to the project structure, or" +
						" specify one in `Run | Edit Configurations...`",
					NotificationType.WARNING
				));
				
				return;
				
			}
			
			// If one GPR file is found, set it as the default
			// GPR file
			
			case 1:
				setDefaultGprFilePathIndex(0);
				break;
			
			// If multiple GPR files are found, ask the user
			// to choose one as default GPR file
			
			default:
				chooseDefaultGprFilePath();
				break;
			
		}
		
		// If multiple GPR files were found and the user has not chosen
		// one as default GPR file, return
		
		String gprFilePath = selectedGprFilePath();
		
		if (gprFilePath == null) { return; }
		
		// Notify the user about the selected default GPR file
		
		Notifications.Bus.notify(new AdaIJNotification(
			"Default Project File",
			"Using the following project file:\n" + selectedGprFilePath(),
			NotificationType.INFORMATION
		));
		
	}
	
	/**
	 * Returns the path to the project's default GPR file, or null if
	 * no such file exists.
	 * This is the public GPR file path getter which does slightly more
	 * work than its private counterpart `selectedGprFilePath`: if no
	 * default GPR file path is set, or if it is but the corresponding file
	 * no longer exists, it will search the project structure for GPR files
	 * before finally returning the newly found unique GPR file or null if
	 * no GPR files are found.
	 * This method also allows to ask the user to choose a default GPR file
	 * if multiple files were found during the search, and will also return
	 * null if the user dismisses the dialog without choosing a file.
	 *
	 * @param chooseIfUnsetAndMultiple Whether or not to ask the user to
	 *                                 choose a default GPR file in case no
	 *                                 such file was previously set and
	 *                                 multiple GPR files are found.
	 * @return The default GPR file path, or null.
	 */
	@Nullable
	public String defaultGprFilePath(boolean chooseIfUnsetAndMultiple) {
		
		if (!adaProject.isAdaProject()) { return null; }
		
		String defaultGprFilePath = selectedGprFilePath();
		
		boolean noDefaultGprFile = false;
		
		if (defaultGprFilePath == null) {
			noDefaultGprFile = true;
		} else {
			
			// Check that the file exists
			
			VirtualFile defaultGprFile =
				LocalFileSystem.getInstance().findFileByPath(defaultGprFilePath);
			
			if (defaultGprFile == null || !defaultGprFile.isValid()) {
				noDefaultGprFile = true;
			}
			
		}
		
		// If no default GPR file exists, try to locate GPR files again
		
		if (noDefaultGprFile) {
			
			locateGprFiles();
			
			// If multiple GPR files were found and `chooseIfUnsetAndMultiple`
			// is true, ask the user to choose a default GPR file
			
			if (gprFilePaths.size() > 1 && chooseIfUnsetAndMultiple) {
				chooseDefaultGprFilePath();
			}
			
			// Return active path, or null if no gpr files found
			
			return selectedGprFilePath();
			
		}
		
		// Otherwise return the default GPR file path
		
		else {
			return defaultGprFilePath;
		}
		
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
	 */
	private void locateGprFiles() {
		
		// Refresh the project
		
		project.getBaseDir().refresh(false, true);
		
		// Reset the list of GPR file paths
		
		gprFilePaths = new ArrayList<>();
		
		// Iterate over the project files and add the paths of files
		// with the GPR file extension to the list of GPR file paths
		
		String gprFileExtension = GPRFileType.INSTANCE.getDefaultExtension();
		
		VfsUtilCore.iterateChildrenRecursively(
			project.getBaseDir(),
			null,
			fileOrDir -> {
				
				// Check that entry is an existing file
				// with the GPR file extension
				
				if (
					fileOrDir.isValid() &&
					!fileOrDir.isDirectory() &&
					gprFileExtension.equals(fileOrDir.getExtension())
				) {
					gprFilePaths.add(fileOrDir.getPath());
				}
				
				return true;
				
			}
		);
		
	}
	
	/**
	 * Returns the path to the currently selected project default GPR file,
	 * or null if no such file exists.
	 * This is the purely internal GPR file path getter that simply gets the
	 * path from `gprFilePaths`, if the current GPR file path index is set.
	 *
	 * @return The currently selected default GPR file path, or null.
	 */
	@Nullable
	private String selectedGprFilePath() {
		return defaultGprFilePathIndex == -1 ? null : gprFilePaths.get(defaultGprFilePathIndex);
	}
	
	/**
	 * Displays a dialog asking the user to choose a default GPR file from
	 * the list of GPR files found in the project structure.
	 */
	private void chooseDefaultGprFilePath() {
		
		// Show the dialog and get the user selection
		
		ListChooserDialog<String> dialog = new ListChooserDialog<>(
			project,
			"Choose Project File",
			"Multiple GPR files were found in this project. " +
				"Please choose one to be used as the default project file:",
			gprFilePaths,
			"You can always a set the default project file later, but this is required " +
				"for semantic features to work, such as reference highlighting and " +
				"code completion.\n\n" +
				"You may still set up custom build configurations that use other GPR files.",
			ListSelectionModel.SINGLE_SELECTION
		);
		
		String selectedPath = dialog.showAndGetSelection();
		
		// If the user made a selection, set the GPR file path index
		
		if (selectedPath != null) {
			setDefaultGprFilePathIndex(gprFilePaths.indexOf(selectedPath));
		}
		
	}
	
	/**
	 * Sets the index of the default GPR file path in the list of
	 * GPR files in the project structure.
	 * If the new index is different from the previous index, all
	 * registered listeners for the event are notified.
	 *
	 * @param newIndex The new default GPR file path index.
	 */
	private void setDefaultGprFilePathIndex(int newIndex) {
		
		if (newIndex == defaultGprFilePathIndex) { return; }
		
		assert newIndex >= -1 && newIndex < gprFilePaths.size();
		
		boolean unset = defaultGprFilePathIndex == -1;
		
		defaultGprFilePathIndex = newIndex;
		
		final String defaultGprFilePath = defaultGprFilePath(false);
		
		if (unset) {
			gprFileSetListeners.forEach((key, listener) -> listener.accept(defaultGprFilePath));
		}
		
		gprFileChangeListeners.forEach((key, listener) -> listener.accept(defaultGprFilePath));
		
	}
	
	/**
	 * Registers the given listener with the given key to GPR file set events.
	 * @see GPRFileManager#gprFileSetListeners
	 *
	 * @param key The key of the listener to register.
	 * @param listener The listener to register.
	 */
	public void addGprFileSetListener(String key, Consumer<String> listener) {
		gprFileSetListeners.put(key, listener);
	}
	
	/**
	 * Unregisters the listener with the given key from GPR file set events.
	 * @see GPRFileManager#gprFileSetListeners
	 *
	 * @param key The key of the listener to unregister.
	 */
	public void removeGprFileSetListener(String key) {
		gprFileSetListeners.remove(key);
	}
	
	/**
	 * Registers the given listener with the given key to GPR file change events.
	 * @see GPRFileManager#gprFileChangeListeners
	 *
	 * @param key The key of the listener to register.
	 * @param listener The listener to register.
	 */
	public void addGprFileChangeListener(String key, Consumer<String> listener) {
		gprFileChangeListeners.put(key, listener);
	}
	
	/**
	 * Unregisters the listener with the given key from GPR file change events.
	 * @see GPRFileManager#gprFileChangeListeners
	 *
	 * @param key The key of the listener to unregister.
	 */
	public void removeGprFileChangeListener(String key) {
		gprFileChangeListeners.remove(key);
	}
	
}
