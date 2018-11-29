package com.adacore.adaintellij;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.*;

/**
 * Global plugin utilities.
 */
public final class Utils {
	
	public static final String FILE_PATH_SEPARATOR        = System.getProperty("file.separator");
	public static final String ENVIRONMENT_PATH_SEPARATOR = System.getProperty("path.separator");
	
	/**
	 * Class-wide logger for the Utils class.
	 */
	private static Logger LOGGER = Logger.getInstance(Utils.class);
	
	/**
	 * File/document manager for retrieving `VirtualFile` and `Document` instances.
	 */
	private static FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
	
	/**
	 * Performs a checked conversion from a URL string to a URL object:
	 * - If the string represents a valid URL, the corresponding URL object
	 *   is returned
	 * - Otherwise if the conversion fails due to an invalid URL string,
	 *   then the failure is logged and null is returned
	 *
	 * @param urlString The URL string to convert to an object.
	 * @return The converted URL object or null if the conversion failed.
	 */
	@Nullable
	public static URL urlStringToUrl(@NotNull String urlString) {
	
		URL url = null;
	
		try {
			url = new URL(urlString);
		} catch (MalformedURLException exception) {
			LOGGER.error("An invalid URL string was used to construct a URL object", exception);
		}
		
		return url;
		
	}
	
	/**
	 * Returns whether or not the given path points to a file or directory
	 * that is in the file hierarchy of the given project.
	 *
	 * @param project The base project.
	 * @param path The absolute path to test.
	 * @return Whether or not the path target is in the project hierarchy.
	 */
	public static boolean isInProjectHierarchy(@NotNull Project project, @NotNull String path) {
		return getPathRelativeToProjectBase(project, path) != null;
	}
	
	/**
	 * Returns the path representing the target of the given path, but
	 * relative to the base directory of the given project.
	 * Returns null if the target of the given path is not a descendant
	 * of the project base directory.
	 *
	 * @param project The base project.
	 * @param path The absolute path for which to get the relative path.
	 * @return The corresponding relative path or null.
	 */
	@Nullable
	public static String getPathRelativeToProjectBase(@NotNull Project project, @NotNull String path) {
		
		String projectBasePath = project.getBasePath();
		
		if (projectBasePath == null || !path.startsWith(projectBasePath)) {
			return null;
		} else if (path.equals(projectBasePath)) {
			return "";
		}
		
		String relativePath = path.substring(projectBasePath.length());
		
		if (!relativePath.startsWith(FILE_PATH_SEPARATOR)) { return null; }
		
		return relativePath.substring(1);
		
	}
	
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
		
		String[] paths = System.getenv("PATH").split(ENVIRONMENT_PATH_SEPARATOR);
		
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
					
					if (!FILE_PATH_SEPARATOR.equals(String.valueOf(path.charAt(path.length() - 1)))) {
						executablePathBuilder.append(FILE_PATH_SEPARATOR);
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
	
	/**
	 * Returns the document corresponding to the given virtual file.
	 *
	 * @param file The virtual file for which to get the document.
	 * @return The file's corresponding document.
	 */
	@Nullable
	public static Document getVirtualFileDocument(@NotNull VirtualFile file) {
		return fileDocumentManager.getDocument(file);
	}
	
	/**
	 * Returns the virtual file corresponding to the given document.
	 *
	 * @param document The document for which to get the virtual file.
	 * @return The document's corresponding virtual file.
	 */
	@Nullable
	public static VirtualFile getDocumentVirtualFile(@NotNull Document document) {
		return fileDocumentManager.getFile(document);
	}
	
	/**
	 * Returns the document corresponding to the given PSI file.
	 *
	 * @param file The PSI file for which to get the document.
	 * @return The file's corresponding document.
	 */
	@Nullable
	public static Document getPsiFileDocument(@NotNull PsiFile file) {
		return file.getViewProvider().getDocument();
	}
	
	/**
	 * Returns the PSI file corresponding to the given virtual file.
	 *
	 * @param project The project to which the returned PSI file belongs.
	 * @param file The virtual file for which to get the PSI file.
	 * @return The file's corresponding PSI file.
	 */
	@Nullable
	public static PsiFile getVirtualFilePsiFile(@NotNull Project project, @NotNull VirtualFile file) {
		return PsiManager.getInstance(project).findFile(file);
	}
	
}
