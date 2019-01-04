package com.adacore.adaintellij;

import java.net.URL;

import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.*;

/**
 * Ada-IntelliJ plugin-wide resource manager.
 * This class makes use of IntelliJ's virtual file system API,
 * and provides facilities for checked access to resource files
 * and directories, as well as reading text resource files.
 */
public final class ResourceManager {
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private ResourceManager() {}
	
	/**
	 * Returns the resource file or directory at the given path
	 * relative to a base resource directory, or null if no such
	 * file or directory exists.
	 *
	 * @param path The path to the resource entry to get.
	 * @return The resource entry at the given path.
	 */
	@Nullable
	private static VirtualFile getResourceFileOrDirectory(@NotNull String path) {
		
		URL resourceUrl = ResourceManager.class.getResource(path);
		
		return resourceUrl == null ? null : VfsUtil.findFileByURL(resourceUrl);
		
	}
	
	/**
	 * Returns the resource file at the given path relative to a
	 * base resource directory, or null if the entry at the given
	 * path is either non-existent or a directory.
	 *
	 * @param path The path to the resource to get.
	 * @return The resource at the given path.
	 */
	@Nullable
	public static VirtualFile getResource(@NotNull String path) {
		
		VirtualFile resource = getResourceFileOrDirectory(path);
		
		return resource == null || resource.isDirectory() ? null : resource;
		
	}
	
	/**
	 * Returns the resource directory at the given path relative
	 * to a base resource directory, or null if the entry at the
	 * given path is either non-existent or a file.
	 *
	 * @param path The path to the resource directory to get.
	 * @return The resource directory at the given path.
	 */
	@Nullable
	public static VirtualFile getResourceDirectory(@NotNull String path) {
		
		VirtualFile resource = getResourceFileOrDirectory(
			path.endsWith("/") ? path : path + '/');
		
		return resource == null || !resource.isDirectory() ? null : resource;
		
	}
	
	/**
	 * Returns the text content of the resource at the given
	 * path, relative to a base resource directory, as a string,
	 * or null if the entry at the given path is either
	 * non-existent or a directory.
	 * @see com.adacore.adaintellij.Utils#getFileText(VirtualFile)
	 *
	 * @param path The path to the resource to read.
	 * @return The resource content as a string.
	 */
	@Nullable
	public static String getResourceText(@NotNull String path) {
	
		VirtualFile resource = getResource(path);
	
		return resource == null ? null : Utils.getFileText(resource);
		
	}
	
	/**
	 * Returns the text content of the resource at the given
	 * path, relative to a base resource directory, as an
	 * array of strings, one for each line of text, or null if
	 * the entry at the given path is either non-existent or a
	 * directory.
	 * @see com.adacore.adaintellij.Utils#getFileLines(VirtualFile)
	 *
	 * @param path The path to the resource to read.
	 * @return The resource lines as an array of strings.
	 */
	@Nullable
	public static String[] getResourceLines(@NotNull String path) {
		
		VirtualFile resource = getResource(path);
		
		return resource == null ? null : Utils.getFileLines(resource);
		
	}
	
}
