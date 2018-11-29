package com.adacore.adaintellij.build;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

/**
 * Tool window factory for the GPRbuild configuration tool window.
 */
public final class GPRbuildConfigurationToolWindowFactory implements ToolWindowFactory, DumbAware {
	
	/**
	 * Creates content for a GPRbuild configuration tool window and
	 * attaches it to the given tool window.
	 *
	 * @param project The project to which the given tool window belongs.
	 * @param toolWindow The tool window to set up.
	 */
	@Override
	public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
	
		ContentManager contentManager = toolWindow.getContentManager();
		
		Content content = contentManager.getFactory().createContent(
			new GPRbuildConfigurationToolWindow(project).getUIRoot(), null, false);
		
		contentManager.addContent(content);
		
	}
	
}
