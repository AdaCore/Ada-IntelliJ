package com.adacore.adaintellij.misc;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.*;

/**
 * A sequence of "interconnected" `JBList`s. This class handles
 * list-related operations and events so that all lists added
 * to a sequence behave as if they are a single `JBList`, for
 * example:
 * - Jumping to the last option of the previous list in the
 *   sequence when the "arrow-up" key is pressed while the
 *   first option of a list is selected
 * - Transferring focus to the component next to the last list
 *   in the sequence when the "tab" key is pressed while any of
 *   the lists has focus
 *
 * For simplicity, all lists added to a list sequence must be
 * in single-selection mode.
 *
 * @param <T> The type of the options in the underlying lists.
 */
public final class JBListSequence<T> {

	/**
	 * The `JBList`s in this sequence.
	 */
	private List<JBList<T>> lists = new ArrayList<>();

	/**
	 * A reference to the list currently in focus.
	 */
	private JBList<T> listInFocus = null;

	/**
	 * Listeners to selection changes in this sequence.
	 */
	private List<Consumer<T>> selectionListeners = new ArrayList<>();

	/**
	 * Adds the given `JBList` to this sequence.
	 *
	 * @param list The list to add to this sequence.
	 */
	public void add(@NotNull JBList<T> list) {

		// Check that the list is in single-selection mode

		assert list.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION :
			"Cannot add non-single-selection JBList to JBListSequence";

		// Disable focus traversal keys for this list

		list.setFocusTraversalKeysEnabled(false);

		// Add a focus listener to pass focus to the
		// list that is supposed to have focus

		list.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent focusEvent) {

				if (listInFocus != null) { listInFocus.requestFocusInWindow(); }

			}

		});

		// Add selection change listener to update the list
		// in focus, clear the selection in all other lists
		// and notify selection change listeners

		list.addListSelectionListener(event -> {

			if (!list.getValueIsAdjusting()) { return; }

			listInFocus = list;

			lists.stream()
				.filter(jbList -> jbList != list)
				.forEach(JBList::clearSelection);

			notifySelectionListeners();

		});

		// Add key listener to handle arrow keys and
		// focus key combinations

		list.addKeyListener(new KeyAdapter() {

			/**
			 * Called when a key is pressed.
			 *
			 * @param keyEvent The key event.
			 */
			@Override
			public void keyPressed(KeyEvent keyEvent) {

				if (listInFocus == null) {
					super.keyPressed(keyEvent);
					return;
				}

				int keyCode = keyEvent.getKeyCode();

				if (keyCode == KeyEvent.VK_TAB) {

					if (keyEvent.isShiftDown()) {
						lists.get(0).transferFocusBackward();
					} else {
						lists.get(lists.size() - 1).transferFocus();
					}

					return;

				}

				int selectedIndex    = listInFocus.getSelectedIndex();
				int listInFocusIndex = lists.indexOf(listInFocus);

				boolean pressedUp              = keyCode == KeyEvent.VK_UP;
				boolean pressedDown            = keyCode == KeyEvent.VK_DOWN;
				boolean selectionIsFirstInList = selectedIndex == 0;
				boolean selectionIsLastInList  = selectedIndex == listInFocus.getItemsCount() - 1;
				boolean listInFocusIsFirst     = listInFocusIndex == 0;
				boolean listInFocusIsLast      = listInFocusIndex == lists.size() - 1;

				if ((pressedUp && !(listInFocusIsFirst && selectionIsFirstInList)) ||
					(pressedDown && !(listInFocusIsLast && selectionIsLastInList)))
				{

					boolean switchToPreviousList = pressedUp   && selectionIsFirstInList;
					boolean switchToNextList     = pressedDown && selectionIsLastInList;

					if (switchToPreviousList || switchToNextList) {

						JBList<T> previousListInFocus = listInFocus;

						listInFocus = lists.get(listInFocusIndex + (switchToNextList ? 1 : -1));
						listInFocus.requestFocusInWindow();

						listInFocus.setSelectedIndex(
							switchToNextList ? 0 : listInFocus.getItemsCount() - 1);

						if (previousListInFocus != null) {
							SwingUtilities.invokeLater(previousListInFocus::clearSelection);
						}

					}

					SwingUtilities.invokeLater(() -> notifySelectionListeners());

				}

				super.keyPressed(keyEvent);

			}

		});

		// Add the list to this sequence

		lists.add(list);

	}

	/**
	 * Returns the current selection of the list currently
	 * in focus, or null if no list in this sequence is in
	 * focus.
	 *
	 * @return The current selection or null.
	 */
	@Nullable
	public T getSelection() {
		return listInFocus == null ? null : listInFocus.getSelectedValue();
	}

	/**
	 * Adds a listener to selection changes in this list sequence.
	 *
	 * @param listener The selection change listener to add.
	 */
	public void addSelectionListener(@NotNull Consumer<T> listener) {
		selectionListeners.add(listener);
	}

	/**
	 * Notifies selection change listeners of a selection change.
	 */
	private void notifySelectionListeners() {
		selectionListeners.forEach(listener ->
			listener.accept(getSelection()));
	}

}
