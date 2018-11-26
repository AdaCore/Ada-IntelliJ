package com.adacore.adaintellij;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

/**
 * UI-specific utilities.
 */
public final class UIUtils {
	
	/**
	 * Light/dark color pair used for component borders.
	 */
	private static final Color BORDER_COLOR = new JBColor(0xbbbbbb, 0x555555);
	
	/**
	 * File chooser descriptor for choosing single files only.
	 */
	public static final FileChooserDescriptor SINGLE_FILE_CHOOSER_DESCRIPTOR =
		new FileChooserDescriptor(true, false, false, false, false, false);
	
	/**
	 * Adds a titeled border to the given component.
	 * The component must have a border that inherits from `TitledBorder`
	 * and a set title (usually in the UI's .form file).
	 *
	 * @param component The titled component to which to add a border.
	 */
	public static void addTitledBorder(@NotNull JComponent component) {
		component.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(BORDER_COLOR),
			((TitledBorder)component.getBorder()).getTitle()
		));
	}
	
}
