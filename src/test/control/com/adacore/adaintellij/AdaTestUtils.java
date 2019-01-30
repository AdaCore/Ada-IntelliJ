package com.adacore.adaintellij;

import java.io.*;
import java.net.URI;

/**
 * General utilities for testing the Ada-IntelliJ plugin.
 */
public final class AdaTestUtils {

	/**
	 * Returns the contents of the file at the given URI as a string
	 * of characters.
	 *
	 * @param fileURI The file URI.
	 * @return The contents of the file.
	 * @throws Exception If a problem occurred while reading the file.
	 */
	public static String getFileText(URI fileURI) throws Exception {

		FileReader reader = new FileReader(new File(fileURI));

		StringBuilder textBuilder = new StringBuilder();

		char[] buffer = new char[100];

		while (true) {

			int charactersRead = reader.read(buffer, 0, 100);

			if (charactersRead == -1) { break; }

			textBuilder.append(buffer, 0, charactersRead);

		}

		return textBuilder.toString();

	}

}
