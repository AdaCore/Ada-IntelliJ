package com.adacore.adaintellij.settings;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.*;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.project.GPRFileManager;
import com.adacore.adaintellij.UIUtils;
import com.adacore.adaintellij.Utils;

/**
 * Project-specific settings UI for Ada projects.
 */
public final class AdaProjectSettings extends DialogWrapper implements ValidatableConfigurable {

	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;

	/**
	 * Child UI components.
	 */
	private JLabel                    resetLabel;
	private TextFieldWithBrowseButton gprFilePathField;
	private JPanel                    errorPanel;
	private JLabel                    errorMessage;

	/**
	 * Button actions.
	 */
	private Action okAction;
	private Action cancelAction;
	private Action applyAction;

	/**
	 * Last set values.
	 */
	private String lastSetGprFilePath;

	/**
	 * The project that this settings view represents.
	 */
	private Project project;

	/**
	 * The GPR file manager component of this settings view's project.
	 */
	private GPRFileManager gprFileManager;

	/**
	 * Constructs a new AdaProjectSettings given a project.
	 *
	 * @param project The project for which to construct a settings UI.
	 */
	public AdaProjectSettings(@NotNull Project project) {

		super(project, false, IdeModalityType.PROJECT);

		this.project        = project;
		this.gprFileManager = GPRFileManager.getInstance(project);

		// Initialize the dialog

		init();

		// Reset the configuration

		reset();
		updateCanApplyState();

	}

	/**
	 * @see com.intellij.openapi.options.Configurable#getDisplayName()
	 */
	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() { return "Ada Project Settings"; }

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#createComponent()
	 */
	@Nullable
	@Override
	public JComponent createComponent() {

		// Set the title

		setTitle(getDisplayName());

		// Set up the UI

		gprFilePathField.addBrowseFolderListener(
			new TextBrowseFolderListener(UIUtils.SINGLE_FILE_CHOOSER_DESCRIPTOR));

		resetLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		resetLabel.addMouseListener(new MouseAdapter() {

			/**
			 * Called when the mouse is clicked.
			 *
			 * @param mouseEvent The mouse event.
			 */
			@Override
			public void mouseClicked(MouseEvent mouseEvent) { reset(); }

		});

		// Add listeners to fields

		ActionListener actionListener =
			actionEvent -> okAction.actionPerformed(actionEvent);

		DocumentListener documentListener = new DocumentAdapter() {

			/**
			 * Called when text is changed.
			 *
			 * @param documentEvent The document event.
			 */
			@Override
			protected void textChanged(DocumentEvent documentEvent) {
				updateCanApplyState();
			}

		};

		gprFilePathField.getTextField().addActionListener(actionListener);
		gprFilePathField.getTextField().getDocument().addDocumentListener(documentListener);

		// Return the root panel

		return rootPanel;

	}

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#isModified()
	 */
	@Override
	public boolean isModified() {
		return !gprFilePathField.getText().equals(lastSetGprFilePath);
	}

	/**
	 * @see com.adacore.adaintellij.settings.ValidatableConfigurable#validateConfigurable()
	 */
	@Override
	public void validateConfigurable() throws ConfigurationException {

		String gprFilePath = gprFilePathField.getText();

		if (!"".equals(gprFilePath) && !Utils.isInProjectHierarchy(project, gprFilePath)) {
			throw new ConfigurationException(
				"The entered project file path must point " +
					"to a file inside this project.");
		}

	}

	/**
	 * @see com.adacore.adaintellij.settings.ValidatableConfigurable#applyAfterValidation()
	 */
	@Override
	public void applyAfterValidation() {

		String newGprFilePath = gprFilePathField.getText();

		gprFileManager.setGprFilePath(newGprFilePath);
		lastSetGprFilePath = newGprFilePath;

	}

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#reset()
	 */
	@Override
	public void reset() {

		String gprFilePath = gprFileManager.getGprFilePath();

		gprFilePathField.setText(gprFilePath);
		lastSetGprFilePath = gprFilePath;

		hideError();

	}

	/**
	 * @see com.intellij.openapi.ui.DialogWrapper#createCenterPanel()
	 */
	@Nullable
	@Override
	protected JComponent createCenterPanel() { return createComponent(); }

	/**
	 * @see com.intellij.openapi.ui.DialogWrapper#createActions()
	 */
	@NotNull
	@Override
	protected Action[] createActions() {

		// "Ok" button action

		okAction = new DialogWrapperExitAction("Ok", 1) {
			@Override
			protected void doAction(ActionEvent e) {

				try {
					apply();
				} catch (ConfigurationException exception) {
					showError(exception.getMessage());
					return;
				}

				super.doAction(e);

			}
		};

		// "Cancel" button action

		cancelAction = new DialogWrapperExitAction("Cancel", 2);

		// "Apply" button action

		applyAction = new DialogWrapperAction("Apply") {
			@Override
			protected void doAction(ActionEvent e) {

				try {
					apply();
				} catch (ConfigurationException exception) {
					showError(exception.getMessage());
					return;
				}

				hideError();

				updateCanApplyState();

			}
		};

		// Return the actions

		return new Action[] {
			okAction,
			cancelAction,
			applyAction
		};

	}

	/**
	 * @see com.intellij.openapi.ui.DialogWrapper#getPreferredFocusedComponent()
	 */
	@Nullable
	@Override
	public JComponent getPreferredFocusedComponent() {
		return gprFilePathField;
	}

	/**
	 * Updates the UI to enable/disable the apply button and the
	 * reset button when the configurable either becomes modified
	 * or is no longer modified.
	 */
	private void updateCanApplyState() {

		JButton applyButton = getButton(applyAction);

		if (applyButton == null) { return; }

		boolean modified = isModified();

		applyButton.setEnabled(modified);
		resetLabel.setVisible(modified);

	}

	/**
	 * Shows the given error message in the UI.
	 * Used when validation of the entered settings fails.
	 *
	 * @param message The error message to be shown.
	 */
	private void showError(@NotNull String message) {
		errorMessage.setText(message);
		errorPanel.setVisible(true);
	}

	/**
	 * Hides the error message.
	 */
	private void hideError() {
		errorPanel.setVisible(false);
	}

}
