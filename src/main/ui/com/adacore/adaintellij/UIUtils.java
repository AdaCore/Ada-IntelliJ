package com.adacore.adaintellij;

import java.awt.*;

/**
 * A collection of UI utilities.
 */
public final class UIUtils {
	
	/**
	 * Colors to be used for borders in IntelliJ dialogs.
	 */
	public static Color HARD_BORDER_COLOR = new Color(48, 48, 48);
	public static Color SOFT_BORDER_COLOR = new Color(80, 80, 80);
	
	/**
	 * Private default constructor to prevent instantiation.
	 */
	private UIUtils() {}
	
	/**
	 * Transforms text in a string to HTML by performing simple
	 * replacing and wrapping in HTML tags.
	 *
	 * Contrary to IntelliJ notifications, in dialogs and other UI
	 * classes, simple text formatting (such as line breaks) requires
	 * text to be in HTML format.
	 * This method allows text to be written in the same format as
	 * with IntelliJ notifications. Dialog and other UI classes
	 * therefore need to systematically transform all text they receive
	 * by calling this method.
	 *
	 * @param text The text to transform to HTML.
	 * @return An HTML version of the string.
	 */
	public static String toHtml(String text) {
		
		String html = text
			.replaceAll("\n", "<br/>");
		
		return "<html>" + html + "</html>";
		
	}
	
}
