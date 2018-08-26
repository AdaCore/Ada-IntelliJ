package com.adacore.adaintellij.file;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * Internal representation of the Ada specification file type.
 */
public class AdaSpecFileType extends AdaFileType {
	
	/**
	 * Unique instance representing the Ada specification file type.
	 */
	public static final AdaFileType INSTANCE = new AdaSpecFileType();
	
	/**
	 * Graphical icon corresponding to the Ada specification file type.
	 */
	public static final Icon ICON = IconLoader.getIcon("/icons/test-icon.png");
	
	/**
	 * Constructs a new instance of the Ada specification file type.
	 */
	private AdaSpecFileType() { super(); }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getName()
	 */
	@NotNull
	@NonNls
	@Override
	public String getName() { return "Ada Specification"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDescription()
	 */
	@NotNull
	@Override
	public String getDescription() { return "Ada Specification Source File"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDefaultExtension()
	 */
	@NotNull
	@NonNls
	@Override
	public String getDefaultExtension() { return "ads"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getIcon()
	 */
	@Nullable
	@Override
	public Icon getIcon() { return ICON; }
	
}
