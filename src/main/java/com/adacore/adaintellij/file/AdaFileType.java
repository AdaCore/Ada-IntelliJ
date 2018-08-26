package com.adacore.adaintellij.file;

import com.adacore.adaintellij.AdaLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;

/**
 * Any file type specific to the Ada language.
 *
 * TODO: Add subclass for the GPR file type
 */
public abstract class AdaFileType extends LanguageFileType {
	
	/**
	 * Constructs a new Ada-specific file type.
	 */
	AdaFileType() { super(AdaLanguage.INSTANCE); }
	
}
