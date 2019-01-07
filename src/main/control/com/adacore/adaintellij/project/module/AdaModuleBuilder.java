package com.adacore.adaintellij.project.module;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.project.template.ProjectTemplateDescriptor;
import com.adacore.adaintellij.project.template.ProjectTemplateManager;
import com.adacore.adaintellij.wizard.AdaProjectWizardUI;

import static com.adacore.adaintellij.project.template.ProjectTemplateDescriptor.TemplateVariableSetting;

/**
 * Ada module builder that sets up new Ada modules.
 * @see com.adacore.adaintellij.project.module.AdaModuleType
 */
public final class AdaModuleBuilder extends ModuleBuilder {
	
	/**
	 * The project template currently selected.
	 */
	private ProjectTemplateDescriptor selectedTemplate = null;
	
	/**
	 * The variable settings for the currently selected
	 * project template.
	 */
	private List<TemplateVariableSetting> variableSettings = new ArrayList<>();
	
	/**
	 * A reference to the name/location settings of the
	 * module being set up.
	 */
	private ModuleNameLocationSettings moduleNameLocationSettings = null;
	
	/**
	 * Project template setup wizard UI.
	 */
	private AdaProjectWizardUI wizardStepUI;
	
	/**
	 * Constructs a new AdaModuleBuilder.
	 */
	public AdaModuleBuilder() {
		
		try {
			
			wizardStepUI = new AdaProjectWizardUI(
				ProjectTemplateManager.getAllProjectTemplates());
			
		} catch (Exception exception) {
			wizardStepUI = null;
		}
		
	}
	
	/**
	 * Sets up the newly created module given the corresponding
	 * modifiable model of roots.
	 *
	 * @param modifiableRootModel The created module's root model.
	 * @throws ConfigurationException If something goes wrong
	 *                                during the setup.
	 */
	@Override
	public void setupRootModel(ModifiableRootModel modifiableRootModel)
		throws ConfigurationException {
		
		// Check that the root model is not null
		
		assert modifiableRootModel != null;
		
		// If the selected template reference is somehow
		// still unset, throw a configuration exception
		
		if (selectedTemplate == null) {
			throw new ConfigurationException("Project setup started " +
				"while no project template has been selected");
		}
		
		// Apply the selected project template
		
		ProjectTemplateManager.applyProjectTemplate(
			selectedTemplate, variableSettings, modifiableRootModel);
		
	}
	
	/**
	 * Returns this module builder's corresponding module type.
	 *
	 * @return This builder's module type.
	 */
	@Override
	public ModuleType getModuleType() {
		return AdaModuleType.getInstance();
	}
	
	/**
	 * Modifies the given module settings step.
	 * In this case, updates the reference to the module name/
	 * location settings from the given settings step in order
	 * to synchronize the different module name fields.
	 *
	 * @param settingsStep The settings step to modify.
	 * @return The modified settings step.
	 */
	@Override
	public ModuleWizardStep modifyStep(SettingsStep settingsStep) {
		
		// Update the reference to the module
		// name/location settings
		
		moduleNameLocationSettings =
			settingsStep.getModuleNameLocationSettings();
		
		// Update the module name
		
		updateModuleName();
		
		// Call the original `modifyStep` method
		
		return super.modifyStep(settingsStep);
		
	}
	
	/**
	 * Creates additional module setup wizard steps given a wizard
	 * context and a module provider.
	 *
	 * @param wizardContext The setup wizard context.
	 * @param modulesProvider The module provider.
	 * @return Additional module setup wizard steps.
	 */
	@NotNull
	@Override
	public ModuleWizardStep[] createWizardSteps(
		@NotNull WizardContext   wizardContext,
		@NotNull ModulesProvider modulesProvider
	) {
		
		// If the setup wizard UI could not be properly
		// initialized, then return an empty array
		
		if (wizardStepUI == null) {
			return ModuleWizardStep.EMPTY_ARRAY;
		}
		
		// Create a module wizard step
		
		ModuleWizardStep wizardStep = new ModuleWizardStep() {
			
			/**
			 * Returns this wizard step's root UI component.
			 *
			 * @return The root UI component of this step.
			 */
			@Override
			public JComponent getComponent() { return wizardStepUI.getUIRoot(); }
			
			/**
			 * @see com.intellij.ide.util.projectWizard.ModuleWizardStep#updateStep()
			 */
			@Override
			public void updateStep() {
				
				if (moduleNameLocationSettings != null) {
					wizardStepUI.setProjectName(moduleNameLocationSettings.getModuleName());
				}
				
			}
			
			/**
			 * @see com.intellij.ide.util.projectWizard.ModuleWizardStep#updateDataModel()
			 */
			@Override
			public void updateDataModel() {
				selectedTemplate = wizardStepUI.getSelectedTemplate();
				variableSettings = wizardStepUI.getTemplateVariables();
			}
			
			/**
			 * @see com.intellij.ide.util.projectWizard.ModuleWizardStep#validate()
			 */
			@Override
			public boolean validate() { return wizardStepUI.isValid(); }
			
			/**
			 * Called when leaving this wizard step.
			 */
			@Override
			public void onStepLeaving() { updateModuleName(); }
			
		};
		
		// Return the wizard step in an array
		
		return new ModuleWizardStep[]{ wizardStep };
		
	}
	
	/**
	 * Updates the name of the module being setup in
	 * the name/location settings.
	 */
	private void updateModuleName() {
		
		if (wizardStepUI != null && moduleNameLocationSettings != null) {
			
			String name = wizardStepUI.getProjectName();
			
			if (name != null) {
				moduleNameLocationSettings.setModuleName(name);
			}
			
		}
		
	}
	
}
