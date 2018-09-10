package com.adacore.adaintellij.file;

import javax.swing.*;

import org.jetbrains.annotations.*;

import com.adacore.adaintellij.icons.Icons;

/**
 * Internal representation of the Ada specification file type.
 */
public final class AdaSpecFileType extends AdaFileType {
	
	/**
	 * Unique instance representing the Ada specification file type.
	 */
	public static final AdaFileType INSTANCE = new AdaSpecFileType();
	
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
	public Icon getIcon() { return Icons.ADA_SPEC_SOURCE_FILE; }
	
}
