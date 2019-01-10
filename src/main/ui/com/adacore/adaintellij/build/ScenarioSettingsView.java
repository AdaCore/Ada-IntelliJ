package com.adacore.adaintellij.build;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.AdaIntelliJUI;

/**
 * GPRbuild scenario variable settings view.
 */
public final class ScenarioSettingsView extends AdaIntelliJUI {
	
	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;
	
	/**
	 * Child UI component.
	 */
	private JPanel  variablesPanel;
	private JButton addButton;
	
	/**
	 * The number of rows (variables) in the view.
	 */
	private int rowCount = 0;
	
	/**
	 * Constructs a new ScenarioSettingsView.
	 */
	ScenarioSettingsView() { this(null); }
	
	/**
	 * Constructs a new ScenarioSettingsView given an optional parent UI.
	 *
	 * @param parentUI The UI in which to embed the constructed UI, or null
	 *                 to construct a standalone UI view.
	 */
	ScenarioSettingsView(@Nullable AdaIntelliJUI parentUI) {
	
		super(parentUI);
		
		// Add "add" button listener
		
		addButton.addActionListener(actionEvent -> addScenarioVariable());
		
	}
	
	/**
	 * @see com.adacore.adaintellij.AdaIntelliJUI#getUIRoot()
	 */
	@NotNull
	@Override
	public JComponent getUIRoot() { return rootPanel; }
	
	/**
	 * Adds a variable/value empty field pair.
	 */
	private void addScenarioVariable() { addScenarioVariable("", ""); }
	
	/**
	 * Adds a variable/value field pair with the given variable and value.
	 *
	 * @param variable The variable to put in the variable field.
	 * @param value The value to put in the value field.
	 */
	private void addScenarioVariable(@NotNull String variable, @NotNull String value) {
		
		// Create variable/value fields and remove button
		
		JTextField variableField = new JTextField(variable);
		JTextField valueField    = new JTextField(value);
		JButton    removeButton  = new JButton("-");
		
		// Add "remove" button listener
		
		removeButton.addActionListener(removeActionEvent -> {
			
			Component[] components = variablesPanel.getComponents();
			
			assert components.length > 0 && components.length % 3 == 0;
			
			boolean copyNextRow = false;
			
			// For every row (three components)...
			
			for (int i = 0 ; i < components.length ; i += 3) {
				
				JTextField rowVariableField     = (JTextField)components[i];
				JTextField rowValueField        = (JTextField)components[i + 1];
				JButton    rowRemoveButton      = (JButton)components[i + 2];
				
				// If this is the last row, then remove it
				
				if (i == components.length - 3) {
					variablesPanel.remove(rowVariableField);
					variablesPanel.remove(rowValueField);
					variablesPanel.remove(rowRemoveButton);
					break;
				}
				
				// If the current row is the one to remove,
				// then set the row-copying boolean to true
				
				if (!copyNextRow && variableField.equals(rowVariableField)) {
					copyNextRow = true;
				}
				
				// If the row-copying boolean is true, then
				// copy the next row into this one
				
				if (copyNextRow) {
					rowVariableField.setText(((JTextField)components[i + 3]).getText());
					rowValueField.setText(((JTextField)components[i + 4]).getText());
				}
				
			}
			
			// Decrement the row count and update the UI
			
			rowCount--;
			
			updateUI();
			
		});
		
		// Add the row components

		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.weightx = 1.0;
		gridConstraints.gridy   = rowCount;
		gridConstraints.ipadx   = 2;
		gridConstraints.ipady   = 2;
		gridConstraints.fill    = GridBagConstraints.HORIZONTAL;
		gridConstraints.insets  = JBUI.insets(2, 3);
		
		variablesPanel.add(variableField, gridConstraints);
		variablesPanel.add(valueField, gridConstraints);
		
		gridConstraints.weightx = 0.1;
		
		variablesPanel.add(removeButton, gridConstraints);
		
		// Increment the row count and update the UI
		
		rowCount++;
		
		updateUI();
		
	}
	
	/**
	 * Sets the field values given a map of scenario variable settings.
	 *
	 * @param scenarioVariables The scenario variable settings from which
	 *                          to set fields.
	 */
	void setScenarioVariables(@NotNull Map<String, String> scenarioVariables) {
		
		// Remove all children
		
		for (Component child : variablesPanel.getComponents()) {
			variablesPanel.remove(child);
		}
		
		rowCount = 0;
		
		// Add given variable settings
		
		scenarioVariables.forEach(this::addScenarioVariable);
		
		// Update the UI
		
		updateUI();
		
	}
	
	/**
	 * Returns the scenario variable settings in this view's fields as a map.
	 *
	 * @return The entered scenario variable settings.
	 */
	Map<String, String> getScenarioVariables() {
		
		Map<String, String> scenarioVariables = new HashMap<>();
		
		Component[] components = variablesPanel.getComponents();
		
		assert components.length % 3 == 0;
		
		for (int i = 0 ; i < components.length ; i += 3) {
			
			String variable = ((JTextField)components[i]).getText();
			String value    = ((JTextField)components[i + 1]).getText();
			
			if ("".equals(variable)) { continue; }
			
			scenarioVariables.put(variable, value);
			
		}
		
		return scenarioVariables;
		
	}
	
}
