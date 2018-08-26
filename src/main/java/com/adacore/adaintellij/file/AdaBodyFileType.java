package com.adacore.adaintellij.file;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * Internal representation of the Ada body file type.
 */
public class AdaBodyFileType extends AdaFileType {
	
	/**
	 * Unique instance representing the Ada body file type.
	 */
	public static final AdaFileType INSTANCE = new AdaBodyFileType();
	
	/**
	 * Graphical icon corresponding to the Ada body file type.
	 */
	public static final Icon ICON = IconLoader.getIcon("/icons/test-icon.png");
	
	/**
	 * Constructs a new instance of the Ada body file type.
	 */
	private AdaBodyFileType() { super(); }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getName()
	 */
	@NotNull
	@NonNls
	@Override
	public String getName() { return "Ada Body"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDescription()
	 */
	@NotNull
	@Override
	public String getDescription() { return "Ada Body Source File"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDefaultExtension()
	 */
	@NotNull
	@NonNls
	@Override
	public String getDefaultExtension() { return "adb"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getIcon()
	 */
	@Nullable
	@Override
	public Icon getIcon() { return ICON; }

}
