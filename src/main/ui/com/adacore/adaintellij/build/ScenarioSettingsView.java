package com.adacore.adaintellij.build;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.adacore.adaintellij.AdaIntelliJUI;

/**
 * GPRbuild scenario variable settings view.
 */
public class ScenarioSettingsView extends AdaIntelliJUI {
	
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
	ScenarioSettingsView() {
		this(null);
	}
	
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
			
			variablesPanel.remove(variableField);
			variablesPanel.remove(valueField);
			variablesPanel.remove(removeButton);
			
			rowCount--;
			
			updateUI();
			
		});
		
		// Add the row components
		
		rowCount++;
		
		GridBagConstraints variableFieldConstraints = new GridBagConstraints();
		
		variableFieldConstraints.weightx = 1.0;
		variableFieldConstraints.gridx   = 0;
		variableFieldConstraints.gridy   = rowCount;
		variableFieldConstraints.ipadx   = 2;
		variableFieldConstraints.ipady   = 2;
		variableFieldConstraints.fill    = GridBagConstraints.HORIZONTAL;
		variableFieldConstraints.insets  = JBUI.insets(2, 3);
		
		variablesPanel.add(variableField, variableFieldConstraints);
		
		GridBagConstraints valueFieldConstraints = new GridBagConstraints();
		
		valueFieldConstraints.weightx = 1.0;
		valueFieldConstraints.gridy   = rowCount;
		valueFieldConstraints.ipadx   = 2;
		valueFieldConstraints.ipady   = 2;
		valueFieldConstraints.fill    = GridBagConstraints.HORIZONTAL;
		valueFieldConstraints.insets  = JBUI.insets(2, 3);
		
		variablesPanel.add(valueField, valueFieldConstraints);
		
		GridBagConstraints removeButtonConstraints = new GridBagConstraints();
		
		removeButtonConstraints.weightx = 0.1;
		removeButtonConstraints.gridy   = rowCount;
		removeButtonConstraints.ipadx   = 2;
		removeButtonConstraints.ipady   = 2;
		removeButtonConstraints.fill    = GridBagConstraints.HORIZONTAL;
		removeButtonConstraints.insets  = JBUI.insets(2, 3);
		
		variablesPanel.add(removeButton, removeButtonConstraints);
		
		// Update the UI
		
		updateUI();
		
	}
	
	/**
	 * Sets the field values given a map of scenario variable settings.
	 *
	 * @param scenarioVariables The scenario variable settings from which
	 *                          to set fields.
	 */
	void setScenarioVariables(@NotNull Map<String, String> scenarioVariables) {
		
		for (Component child : variablesPanel.getComponents()) {
			variablesPanel.remove(child);
		}
		
		rowCount = 0;
		
		scenarioVariables.forEach(this::addScenarioVariable);
		
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
