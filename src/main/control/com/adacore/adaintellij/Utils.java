package com.adacore.adaintellij;

import java.io.File;

import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;

/**
 * Global plugin utilities.
 */
public final class Utils {
	
	/**
	 * Returns whether or not an executable with the given name is on
	 * the system PATH.
	 *
	 * @param executableName The name of the executable to search for.
	 * @param withExtension Whether or not the search should take
	 *                      file extensions into consideration.
	 * @return Whether or not the executable is on the path.
	 */
	public static boolean isOnSystemPath(String executableName, boolean withExtension) {
		return getPathFromSystemPath(executableName, withExtension) != null;
	}
	
	/**
	 * Returns the absolute path to the executable with the given name
	 * by searching the system PATH directories, or null if no such
	 * executable was found.
	 *
	 * @param executableName The name of the executable to search for.
	 * @param withExtension Whether or not the search should take
	 *                      file extensions into consideartion.
	 * @return The absolute path to the executable.
	 */
	@Nullable
	public static String getPathFromSystemPath(@NotNull String executableName, boolean withExtension) {
		
		char   fileSeparator = System.getProperty("file.separator").charAt(0);
		String pathSeparator = System.getProperty("path.separator");
		
		String[] paths = System.getenv("PATH").split(pathSeparator);
		
		// For each entry in the PATH...
		
		for (String path : paths) {
			
			// Get the directory of the entry
			
			VirtualFile directory = LocalFileSystem.getInstance().findFileByPath(path);
			
			// Check that it is indeed a directory
			// Should always be the case in a PATH variable but just to be safe
			
			if (directory == null || !directory.isDirectory()) { continue; }
			
			// For each child of the entry...
			
			for (VirtualFile file : directory.getChildren()) {
				
				// Check that it is an executable file
				
				if (file.isDirectory() || !(new File(file.getPath()).canExecute())) { continue; }
				
				String filename = withExtension ? file.getName() : file.getNameWithoutExtension();
				
				if (executableName.equals(filename)) {
					
					StringBuilder executablePathBuilder = new StringBuilder(path);
					
					// Append the file separator character if it is not already
					// at the end of the PATH entry
					// It should generally not be the case, but just to be safe
					
					if (path.charAt(path.length() - 1) != fileSeparator) {
						executablePathBuilder.append(fileSeparator);
					}
					
					// Return the full path to the executable
					
					return executablePathBuilder.append(file.getName()).toString();
					
				}
				
			}
			
		}
		
		// No matching executable was found in any PATH entry,
		// so return null
		
		return null;
		
	}
	
}
