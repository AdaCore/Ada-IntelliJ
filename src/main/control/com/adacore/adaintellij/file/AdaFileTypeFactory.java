package com.adacore.adaintellij.file;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Ada file type factory.
 */
public final class AdaFileTypeFactory extends FileTypeFactory {

	/**
	 * Registers the different Ada-specific file types.
	 *
	 * @param fileTypeConsumer The consumer used to register the file types.
	 */
	@Override
	public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {

		// Ada source file types
		fileTypeConsumer.consume(AdaSpecFileType.INSTANCE);
		fileTypeConsumer.consume(AdaBodyFileType.INSTANCE);

		// GPR file type
		fileTypeConsumer.consume(GPRFileType.INSTANCE);

	}

}
