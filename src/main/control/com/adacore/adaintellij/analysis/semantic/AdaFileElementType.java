package com.adacore.adaintellij.analysis.semantic;

import com.intellij.psi.tree.IFileElementType;

import com.adacore.adaintellij.AdaLanguage;

/**
 * Element type representing an Ada file.
 */
public final class AdaFileElementType extends IFileElementType {

	/**
	 * Unique instance representing the Ada file element type.
	 */
	public static final AdaFileElementType INSTANCE = new AdaFileElementType();
	
	/**
	 * Constructs a new instance of the Ada file element type.
	 */
	private AdaFileElementType() {
		super("Ada.FILE", AdaLanguage.INSTANCE);
	}

}
