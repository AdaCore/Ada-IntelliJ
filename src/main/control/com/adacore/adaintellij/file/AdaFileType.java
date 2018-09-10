package com.adacore.adaintellij.file;

import com.intellij.openapi.fileTypes.LanguageFileType;

import com.adacore.adaintellij.AdaLanguage;

/**
 * Any file type specific to the Ada language.
 */
public abstract class AdaFileType extends LanguageFileType {
	
	/**
	 * Constructs a new Ada-specific file type.
	 */
	AdaFileType() { super(AdaLanguage.INSTANCE); }
	
}
