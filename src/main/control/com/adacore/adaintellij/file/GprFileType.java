package com.adacore.adaintellij.file;

import javax.swing.*;

import org.jetbrains.annotations.*;

import com.adacore.adaintellij.Icons;

/**
 * Internal representation of the GNAT project file/GPR file type.
 */
public final class GprFileType extends AdaFileType {
	
	/**
	 * Unique instance representing the GPR file type.
	 */
	public static final AdaFileType INSTANCE = new GprFileType();
	
	/**
	 * Constructs a new instance of the GPR file type.
	 */
	private GprFileType() { super(); }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getName()
	 */
	@NotNull
	@NonNls
	@Override
	public String getName() { return "Ada GPR File"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDescription()
	 */
	@NotNull
	@Override
	public String getDescription() { return "Ada GNAT Project File"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getDefaultExtension()
	 */
	@NotNull
	@NonNls
	@Override
	public String getDefaultExtension() { return "gpr"; }
	
	/**
	 * @see com.intellij.openapi.fileTypes.FileType#getIcon()
	 */
	@Nullable
	@Override
	public Icon getIcon() { return Icons.ADA_GPR_SOURCE_FILE; }
	
}
