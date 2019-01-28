package com.adacore.adaintellij.wizard;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import com.intellij.ui.components.*;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.*;

import com.adacore.adaintellij.misc.*;
import com.adacore.adaintellij.project.template.ProjectTemplateDescriptor;
import com.adacore.adaintellij.*;

import static com.adacore.adaintellij.project.template.ProjectTemplateDescriptor.*;

/**
 * UI for the Ada project setup wizard.
 */
public final class AdaProjectWizardUI extends AdaIntelliJUI {
	
	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;
	
	/**
	 * Child UI components.
	 */
	private JScrollPane templateListScrollPane;
	private JPanel      templateListContainer;
	private JTextPane   templateDescription;
	private JPanel      templateSettingsContainer;
	
	/**
	 * The sequence of lists of project templates.
	 */
	private JBListSequence<ProjectTemplateDescriptor>
		templateListSequence = new JBListSequence<>();
	
	/**
	 * Constructs a new AdaProjectWizardUI given a list
	 * of project template descriptors.
	 *
	 * @param templateDescriptors The template descriptors.
	 */
	public AdaProjectWizardUI(
		@NotNull List<ProjectTemplateDescriptor> templateDescriptors
	) {
		
		// Initialize the UI
		
		super(null);
		
		UIUtils.addLineBorder(templateListScrollPane);
		UIUtils.adjustScrollSpeed(templateListScrollPane);
		
		templateListContainer.setLayout(new GridBagLayout());
		
		// Group the template descriptors by category
		
		final Map<String, List<ProjectTemplateDescriptor>>
			categoryTemplateDescriptors = new TreeMap<>();
		
		templateDescriptors.forEach(descriptor -> {
			
			categoryTemplateDescriptors.putIfAbsent(
				descriptor.templateCategory, new ArrayList<>());
			
			categoryTemplateDescriptors.get(descriptor.templateCategory).add(descriptor);
			
		});
		
		// Create grid constraints for the template list
		// container components
		
		final GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx   = 0;
		gridConstraints.fill    = GridBagConstraints.HORIZONTAL;
		gridConstraints.weightx = 1.0;
		
		// Set up the template list of each category
		
		categoryTemplateDescriptors.forEach((category, descriptors) -> {
			
			descriptors.sort(Comparator.comparing(descriptor -> descriptor.templateName));
			
			// Create the category label
			
			JLabel label = new JBLabel(category);
			UIUtils.addIconWithGap(label, Icons.PROJECT_TEMPLATE_CATEGORY);
			label.setBorder(JBUI.Borders.empty(2));
			
			// Add the category label
			
			templateListContainer.add(label, gridConstraints);
			
			// Create the template list
			
			DefaultListModel<ProjectTemplateDescriptor> descriptorsModel = new DefaultListModel<>();
			descriptors.forEach(descriptorsModel::addElement);
			
			JBList<ProjectTemplateDescriptor> templateList = new JBList<>(descriptorsModel);
			templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			templateList.setCellRenderer(new IndentedIconedListCellRenderer(1, Icons.PROJECT_TEMPLATE));
			templateListSequence.add(templateList);
			
			// Add the template list
			
			templateListContainer.add(templateList, gridConstraints);
			
		});
		
		// Add an empty panel to fill the rest of the space
		
		gridConstraints.weighty = 1.0;
		
		templateListContainer.add(new JPanel(), gridConstraints);
		
		// Add selection listener to the list sequence to update
		// the settings UI whenever a template is selected
		
		templateListSequence.addSelectionListener(descriptor -> {
			
			if (descriptor != null) {
				
				templateDescription.setText(descriptor.templateDescription);
				
				setUpProjectTemplateSettingsUI(descriptor);
				
			}
			
		});
		
	}
	
	/**
	 * @see com.adacore.adaintellij.AdaIntelliJUI#getUIRoot()
	 */
	@NotNull
	@Override
	public JComponent getUIRoot() { return rootPanel; }
	
	/**
	 * Returns whether or not the wizard state is valid,
	 * which indicates that the wizard is ready to be
	 * applied.
	 *
	 * @return Whether or not the wizard state is valid.
	 */
	public boolean isValid() {
		return templateListSequence.getSelection() != null &&
			!UIUtils.hasEffectivelyEmptyTextComponentDescendant(templateSettingsContainer);
	}
	
	/**
	 * Returns the currently selected project template.
	 *
	 * @return The currently selected project template.
	 */
	public ProjectTemplateDescriptor getSelectedTemplate() {
		return templateListSequence.getSelection();
	}
	
	/**
	 * Traverses the wizard UI to read all template variable
	 * settings from variable components, and returns those
	 * variable settings.
	 *
	 * @return The currently configured template variable
	 *         settings.
	 */
	public List<TemplateVariableSetting> getTemplateVariables() {
		
		List<TemplateVariableSetting> variables = new ArrayList<>();
		
		UIUtils.traverseUIDescendantsOfClass(
			templateSettingsContainer,
			TemplateVariableComponent.class,
			variableTextField -> {
				
				variables.add(new TemplateVariableSetting(
					variableTextField.getVariableName(), variableTextField.getValue()));
				
				return false;
				
			}
		);
		
		return variables;
		
	}
	
	/**
	 * Returns the currently set project name.
	 * The project name is simply assumed to be the value of the
	 * field whose variable name case-insensitively matches the
	 * string "project_name".
	 *
	 * @return The currently set project name.
	 */
	@Nullable
	public String getProjectName() {
		
		final StringBuilder nameBuilder = new StringBuilder();
		
		UIUtils.traverseUIDescendantsOfClass(
			templateSettingsContainer,
			TemplateVariableTextField.class,
			variableTextField -> {
				
				if ("project_name".equals(
					variableTextField.variableName.toLowerCase()))
				{
					nameBuilder.append(variableTextField.getText());
					return true;
				}
				
				return false;
				
			}
		);
		
		String name = nameBuilder.toString().trim();
		
		return name.length() == 0 ? null : name;
		
	}
	
	/**
	 * Sets the project name to the given name.
	 * For information about where the project name is stored:
	 * @see AdaProjectWizardUI#getProjectName()
	 *
	 * @param name The name to set.
	 */
	public void setProjectName(@NotNull String name) {
		
		UIUtils.traverseUIDescendantsOfClass(
			templateSettingsContainer,
			TemplateVariableTextField.class,
			variableTextField -> {
			
				if ("project_name".equals(
					variableTextField.variableName.toLowerCase()))
				{
					variableTextField.setText(name);
					return true;
				}
				
				return false;
			
			}
		);
	
	}
	
	/**
	 * Sets up the configurable section of the UI with fields/
	 * combo-boxes from the template variables in the given
	 * template descriptor.
	 *
	 * @param descriptor The template descriptor with template
	 *                   variables from which to set up the UI.
	 */
	private void setUpProjectTemplateSettingsUI(
		@NotNull ProjectTemplateDescriptor descriptor
	) {
		
		templateSettingsContainer.removeAll();
		
		// If the descriptor's template has no variables,
		// then add a short message for the user
		
		if (descriptor.templateVariables.length == 0) {
		
			templateSettingsContainer.setLayout(new GridLayout(1, 1));
			
			JBLabel label = new JBLabel("No settings to configure.");
			
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			templateSettingsContainer.add(label);
		
		}
		
		// Otherwise, for each variable, add a text field or a
		// combo-box, depending on whether or not the variable
		// is one with a finite set of options
		
		else {
			
			templateSettingsContainer.setLayout(
				new BoxLayout(templateSettingsContainer, BoxLayout.Y_AXIS));
			
			for (TemplateVariable variable : descriptor.templateVariables) {
				
				JPanel panel = new JPanel(new GridLayout(2, 1));
				
				UIUtils.addTitledBorder(panel, variable.name.replaceAll("_", " "));
				
				JComponent variableComponent = variable instanceof TemplateVariableWithOptions ?
					new TemplateVariableComboBox(
						variable.name,
						((TemplateVariableWithOptions)variable).options,
						variable.defaultValue
					) :
					new TemplateVariableTextField(variable.name, variable.defaultValue);
				
				panel.add(variableComponent);
				panel.add(new JBLabel(variable.description).withBorder(JBUI.Borders.empty(2, 3)));
				
				templateSettingsContainer.add(panel);
				templateSettingsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
				
			}
			
		}
		
		// Update the UI of the settings container
		
		templateSettingsContainer.updateUI();
		
	}
	
	/**
	 * A UI component representing a template variable.
	 */
	private interface TemplateVariableComponent {
		
		/**
		 * Returns the name of the template variable that this
		 * component represents.
		 *
		 * @return The name of this component's variable.
		 */
		String getVariableName();
		
		/**
		 * Returns the currently set value for the template
		 * variable that this component represents.
		 *
		 * @return The value set for this component's variable.
		 */
		String getValue();
		
	}
	
	/**
	 * A template variable text field.
	 */
	private static final class TemplateVariableTextField
		extends JBTextField implements TemplateVariableComponent
	{
		
		/**
		 * The name of the template variable that this
		 * text field represents.
		 */
		final String variableName;
		
		/**
		 * Constructs a new TemplateVariableTextField given a
		 * variable name and an initial value.
		 *
		 * @param variableName The name of the template
		 *                     variable represented by the
		 *                     constructed text field.
		 * @param initialValue The initial value to which to
		 *                     set the constructed text field.
		 */
		TemplateVariableTextField(@NotNull String variableName, @NotNull String initialValue) {
			super(initialValue);
			this.variableName = variableName;
		}
		
		/**
		 * @see TemplateVariableComponent#getVariableName()
		 */
		@Override
		public String getVariableName() { return variableName; }
		
		/**
		 * @see TemplateVariableComponent#getValue()
		 */
		@Override
		public String getValue() { return getText(); }
		
	}
	
	/**
	 * A template variable combo-box.
	 */
	private static final class TemplateVariableComboBox
		extends JComboBox<String> implements TemplateVariableComponent
	{
		
		/**
		 * The name of the template variable that this
		 * combo-box represents.
		 */
		final String variableName;
		
		/**
		 * Constructs a new TemplateVariableComboBox given a
		 * variable name, an array of possible values and an
		 * initial value.
		 *
		 * @param variableName The name of the template
		 *                     variable represented by the
		 *                     constructed combo-box.
		 * @param possibleValues The possible values for the
		 *                       variable.
		 * @param initialValue The initial value to which to
		 *                     set the constructed combo-box.
		 */
		TemplateVariableComboBox(
			@NotNull String   variableName,
			@NotNull String[] possibleValues,
			@NotNull String   initialValue
		) {
			
			super(possibleValues);
			setSelectedItem(initialValue);
			
			this.variableName = variableName;
			
		}
		
		/**
		 * @see TemplateVariableComponent#getVariableName()
		 */
		@Override
		public String getVariableName() { return variableName; }
		
		/**
		 * @see TemplateVariableComponent#getValue()
		 */
		@Override
		public String getValue() { return (String)getSelectedItem(); }
		
	}
	
}
