package com.adacore.adaintellij;

import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

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
	 * Various UI-related constants.
	 */
	private static final int LABEL_ICON_TEXT_GAP   = 5;
	private static final int ADJUSTED_SCROLL_SPEED = 12;

	/**
	 * File chooser descriptor for choosing single files only.
	 */
	public static final FileChooserDescriptor SINGLE_FILE_CHOOSER_DESCRIPTOR =
		new FileChooserDescriptor(true, false, false, false, false, false);

	/**
	 * Adds a simple line border to the given component.
	 *
	 * @param component The component to which to add a border.
	 */
	public static void addLineBorder(@NotNull JComponent component) {
		component.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
	}

	/**
	 * Adds a titled border to the given component.
	 * The component must have a border that inherits from `TitledBorder`
	 * and a set title (usually in the UI's .form file).
	 *
	 * @param component The titled component to which to add a border.
	 */
	public static void addTitledBorder(@NotNull JComponent component) {
		addTitledBorder(component, ((TitledBorder)component.getBorder()).getTitle());
	}

	/**
	 * Adds a titled border with the given title to the given component.
	 *
	 * @param component The component to which to add a border.
	 * @param title The title to give to the border.
	 */
	public static void addTitledBorder(@NotNull JComponent component, @NotNull String title) {
		component.setBorder(new TitledBorder(
			BorderFactory.createLineBorder(BORDER_COLOR), title));
	}

	/**
	 * Adjusts the scroll speed of the given scroll-pane, both vertically
	 * and horizontally.
	 *
	 * @param scrollPane The scroll-pane to adjust.
	 */
	public static void adjustScrollSpeed(@NotNull JScrollPane scrollPane) {
		scrollPane.getVerticalScrollBar().setUnitIncrement(ADJUSTED_SCROLL_SPEED);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(ADJUSTED_SCROLL_SPEED);
	}

	/**
	 * Adds the given icon, along with an icon-text gap, to the given label.
	 *
	 * @param label The label to which to add the icon.
	 * @param icon The icon to add.
	 */
	public static void addIconWithGap(@NotNull JLabel label, @NotNull Icon icon) {
		label.setIcon(icon);
		label.setIconTextGap(LABEL_ICON_TEXT_GAP);
	}

	/**
	 * Recursively traverses the UI descendants of the given container,
	 * calling the given handler for each component of the given class or
	 * interface.
	 * @see UIUtils#traverse(Container, Class, Function)
	 *
	 * The given handler must return true to stop the traversal or false
	 * to continue.
	 *
	 * @param container The root container to traverse.
	 * @param classToHandle The component class/interface to handle.
	 * @param handler The component handler.
	 * @param <T> The type represented by the component class to handle.
	 */
	public static <T> void traverseUIDescendantsOfClass(
		@NotNull Container            container,
		@NotNull Class<T>             classToHandle,
		@NotNull Function<T, Boolean> handler
	) { traverse(container, classToHandle, handler); }

	/**
	 * Returns whether or not the given container has at least one UI
	 * descendant that is a `JTextComponent` and whose text content is
	 * effectively empty (empty or whitespace only).
	 *
	 * @param container The container to test.
	 * @return Whether or not the given component has an effectively
	 *         empty text component descendant.
	 */
	public static boolean hasEffectivelyEmptyTextComponentDescendant(@NotNull Container container) {

		AtomicBoolean result = new AtomicBoolean(false);

		traverse(container, JTextComponent.class, textComponent -> {

			boolean empty = textComponent.getText().trim().length() == 0;

			if (empty) { result.set(true); }

			return empty;

		});

		return result.get();

	}

	/**
	 * Internal UI container traversal method. Performs recursive,
	 * depth-first traversal of the given container. Returns a boolean
	 * to indicate whether or not traversal should be stopped.
	 *
	 * @param container The root container to traverse.
	 * @param classToHandle The component class/interface to handle.
	 * @param handler The component handler.
	 * @param <T> The type represented by the component class to handle.
	 * @return true to stop the traversal, false otherwise.
	 */
	private static <T> boolean traverse(
		@NotNull Container            container,
		@NotNull Class<T>             classToHandle,
		@NotNull Function<T, Boolean> handler
	) {

		for (Component child : container.getComponents()) {

			// Make sure the child is not null

			if (child == null) { continue; }

			boolean stop = false;

			// If the child is an instance of the class to
			// handle, then call the handler on that child

			if (classToHandle.isInstance(child)) {
				stop = handler.apply(classToHandle.cast(child));
			}

			if (stop) { return true; }

			// If the child is itself a container, then
			// traverse it recursively

			if (child instanceof Container) {
				stop = traverse((Container)child,classToHandle, handler);
			}

			if (stop) { return true; }

		}

		return false;

	}

}
