package com.adacore.adaintellij.dialogs;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.misc.BasicListCellRenderer;
import com.adacore.adaintellij.UIUtils;

/**
 * A simple dialog allowing the user to select an item from a list:
 *
 *         +----------------------------------------+
 *         |                                        |
 *         |               Main text                |
 *         |                                        |
 *         +----------------------------------------+
 *         |                                        |
 *         |                  List                  |
 *         |                                        |
 *         +----------------------------------------+
 *         |                                        |
 *         |          Footnote (optional)           |
 *         |                                        |
 *         +----------------------+--------+--------+
 *         |                      |   Ok   | Cancel |
 *         +----------------------+--------+--------+
 */
public final class ListChooserDialog<T> extends DialogWrapper {

	/**
	 * UI components.
	 */
	private JPanel      contentPane;
	private JTextPane   mainTextPane;
	private JScrollPane optionListScrollPane;
	private JBList<T>   optionList;
	private JTextPane   footnoteTextPane;

	/**
	 * Constructs a new ListChooserDialog given some parameters,
	 * without a footnote.
	 *
	 * @param project The project to which the dialog belongs.
	 * @param title The title of the dialog.
	 * @param mainText The main text of the dialog.
	 * @param options The list of items for the user to choose from.
	 * @param listSelectionMode The list selection mode.
	 */
	public ListChooserDialog(
		@NotNull Project project,
		@NotNull String  title,
		@NotNull String  mainText,
		@NotNull List<T> options,
		         int     listSelectionMode
	) { this(project, title, mainText, options, null, listSelectionMode); }

	/**
	 * Constructs a new ListChooserDialog given some parameters.
	 *
	 * @param project The project to which the dialog belongs.
	 * @param title The title of the dialog.
	 * @param mainText The main text of the dialog.
	 * @param options The list of items for the user to choose from.
	 * @param footnote The footnote text of the dialog.
	 * @param listSelectionMode The list selection mode.
	 */
	public ListChooserDialog(
		@NotNull  Project project,
		@NotNull  String  title,
		@NotNull  String  mainText,
		@NotNull  List<T> options,
		@Nullable String  footnote,
		          int     listSelectionMode
	) {

		super(project, false, IdeModalityType.IDE);

		// Check parameters

		assert options.size() > 0 : "Cannot create list-chooser-dialog from empty option list";

		// Initialize the dialog

		init();

		// Set the title

		setTitle(title);

		// Set the main text

		mainTextPane.setText(mainText);

		// Set up the option list

		UIUtils.addLineBorder(optionListScrollPane);
		UIUtils.adjustScrollSpeed(optionListScrollPane);

		// TODO: Set maximum width on option list to avoid very
		//       wide dialogs when there are gpr file paths that
		//       are too long

		DefaultListModel<T> model = new DefaultListModel<>();
		options.forEach(model::addElement);
		optionList.setModel(model);
		optionList.setSelectionMode(listSelectionMode);

		optionList.setSelectedIndex(0);
		optionList.setVisibleRowCount(Math.min(options.size() + 2, 10));
		optionList.setCellRenderer(new BasicListCellRenderer());

		optionList.addMouseListener(new MouseAdapter() {

			/**
			 * Called when the mouse is clicked.
			 *
			 * @param mouseEvent The mouse event.
			 */
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {

				if (mouseEvent.getClickCount() >= 2) {
					getOKAction().actionPerformed(null);
				}

			}

		});

		// Set the footnote if it is not null

		if (footnote == null) {
			footnoteTextPane.setVisible(false);
		} else {
			footnoteTextPane.setText(footnote);
		}

	}

	/**
	 * @see com.intellij.openapi.ui.DialogWrapper#createCenterPanel()
	 */
	@Nullable
	@Override
	protected JComponent createCenterPanel() { return contentPane; }

	/**
	 * @see com.intellij.openapi.ui.DialogWrapper#getPreferredFocusedComponent()
	 */
	@Override
	public JComponent getPreferredFocusedComponent() { return optionList; }

	/**
	 * Displays this dialog to the user, waits for user action and returns
	 * the list of selected options, or null if no selection was made.
	 *
	 * @return The list of selected options.
	 */
	@Nullable
	public List<T> showAndGetSelections() {

		boolean selectionMade = showAndGet();

		return selectionMade ? optionList.getSelectedValuesList() : null;

	}

	/**
	 * Single selection version of `showAndGetSelections`, returning a
	 * single selection instead of a list, or null if no selection was made.
	 * Throws an assertion error if it is called for a ListChooserDialog
	 * that was created in a multi-selection mode.
	 *
	 * @return The selected option.
	 */
	@Nullable
	public T showAndGetSelection() {

		assert optionList.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION :
			"ListChooserDialog#showAndGetSelection() must not be used in multi-selection mode";

		List<T> selections = showAndGetSelections();

		return selections == null || selections.size() == 0 ? null : selections.get(0);

	}

}
