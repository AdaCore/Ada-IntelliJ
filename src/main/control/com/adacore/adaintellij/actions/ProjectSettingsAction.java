package com.adacore.adaintellij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import com.adacore.adaintellij.settings.AdaProjectSettings;

/**
 * IntelliJ action to open Ada project settings.
 */
public final class ProjectSettingsAction extends AnAction {

	/**
	 * @see com.intellij.openapi.actionSystem.AnAction#actionPerformed(AnActionEvent)
	 */
	@Override
	public void actionPerformed(AnActionEvent event) {

		Project project = event.getProject();

		if (project == null) { return; }

		AdaProjectSettings settings = new AdaProjectSettings(project);

		settings.showAndGet();

	}

}
