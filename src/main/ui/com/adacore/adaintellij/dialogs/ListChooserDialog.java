package com.adacore.adaintellij.dialogs;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Vector;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

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
public final class ListChooserDialog<T> extends JDialog {
	
	/**
	 * UI components.
	 */
	private JPanel   contentPane;
	private JButton  okButton;
	private JButton  cancelButton;
	private JLabel   mainTextLabel;
	private JList<T> optionList;
	private JLabel   footnoteLabel;
	
	/**
	 * Constructs a new ListChooserDialog given some parameters,
	 * without a footnote.
	 *
	 * @param mainText The main text of the dialog.
	 * @param options The list of items for the user to choose from.
	 * @param okConsumer The consumer to run with the chosen option.
	 * @param cancelRunnable The runnable to run in case the user cancels.
	 * @param listSelectionMode The list selection mode.
	 */
	public ListChooserDialog(
		@NotNull String      mainText,
		@NotNull List<T>     options,
		@NotNull Consumer<T> okConsumer,
		@NotNull Runnable    cancelRunnable,
		         int         listSelectionMode
	) {
		this(mainText,
			options,
			null,
			okConsumer,
			cancelRunnable,
			listSelectionMode
		);
	}
	
	/**
	 * Constructs a new ListChooserDialog given some parameters.
	 *
	 * @param mainText The main text of the dialog.
	 * @param options The list of items for the user to choose from.
	 * @param footnote The footnote text of the dialog.
	 * @param okConsumer The consumer to run with the chosen option.
	 * @param cancelRunnable The runnable to run in case the user cancels.
	 * @param listSelectionMode The list selection mode.
	 */
	public ListChooserDialog(
		@NotNull String      mainText,
		@NotNull List<T>     options,
		         String      footnote,
		@NotNull Consumer<T> okConsumer,
		@NotNull Runnable    cancelRunnable,
		         int         listSelectionMode
	) {
		
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(okButton);
		setLocationRelativeTo(null);
		
		// Set Ok and Cancel buttons listeners
		okButton.addActionListener(event -> consumeAndDispose(okConsumer));
		cancelButton.addActionListener(event -> runAndDispose(cancelRunnable));
		
		// call onCancel() when cross is clicked
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				runAndDispose(cancelRunnable);
			}
			
		});
		
		// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(
			event -> runAndDispose(cancelRunnable),
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);
		
		mainTextLabel.setText(UIUtils.toHtml(mainText));
		
		DefaultListModel<T> model = new DefaultListModel<>();
		options.forEach(model::addElement);
		
		optionList.setModel(model);
		optionList.setVisibleRowCount(10);
		optionList.setSelectionMode(listSelectionMode);
		optionList.setSelectedIndex(0);
		optionList.setBorder(new LineBorder(UIUtils.HARD_BORDER_COLOR));
		optionList.setCellRenderer(new DefaultListCellRenderer() {
			
			@Override
			public Component getListCellRendererComponent(
				JList<?> list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus
			) {
				
				JLabel component = (JLabel)super.getListCellRendererComponent(
					list, value, index, isSelected, cellHasFocus);
				
				component.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				
				return component;
				
			}
			
		});
		optionList.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
			
				if (mouseEvent.getClickCount() >= 2) {
					consumeAndDispose(okConsumer);
				}
			
			}
			
		});
		
		if (footnote == null) {
			footnoteLabel.setVisible(false);
		} else {
			footnoteLabel.setText(UIUtils.toHtml(footnote));
		}
		
	}
	
	/**
	 * Runs the given runnable and disposes of this dialog.
	 *
	 * @param runnable The runnable to run.
	 */
	private void runAndDispose(Runnable runnable) {
		runnable.run();
		dispose();
	}
	
	/**
	 * Runs the given consumer with the selected item and disposes
	 * of this dialog.
	 *
	 * @param consumer The consumer to run.
	 */
	private void consumeAndDispose(Consumer<T> consumer) {
		consumer.accept(optionList.getSelectedValue());
		dispose();
	}
	
	/**
	 * Displays this dialog to the user.
	 */
	public void display() {
		pack();
		setVisible(true);
	}
	
}
