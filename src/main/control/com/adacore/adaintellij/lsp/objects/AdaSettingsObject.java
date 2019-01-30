package com.adacore.adaintellij.lsp.objects;

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.gson.annotations.JsonAdapter;

import org.eclipse.lsp4j.jsonrpc.json.adapters.JsonElementTypeAdapter;
import org.eclipse.lsp4j.jsonrpc.validation.NonNull;

/**
 * Class representation of the Ada-specific JSON settings object
 * included in `workspace/didChangeConfiguration` notifications.
 *
 * Settings structure:
 *
 * {
 *     "ada" : {
 *         "projectFile"       : "file:///path/to/projectFile",
 *         "scenarioVariables" : {
 *             "variable1" : "value1",
 *             "variable2" : "value2"
 *         }
 *     }
 * }
 *
 */
public final class AdaSettingsObject {

	/**
	 * Class representation of the "ada" JSON object.
	 */
	public class AdaObject {

		/**
		 * The "projectFile" property.
		 */
		private String projectFile;

		/**
		 * The "scenarioVariables" property.
		 */
		private Map<String, String> scenarioVariables;

		/**
		 * The "projectFile" property getter.
		 */
		@Nullable
		public String getProjectFile() { return projectFile; }

		/**
		 * The "scenarioVariables" property getter.
		 */
		@Nullable
		public Map<String, String> getScenarioVariables() { return scenarioVariables; }

	}

	/**
	 * The "ada" property.
	 */
	@NonNull
	@JsonAdapter(JsonElementTypeAdapter.Factory.class)
	private AdaObject ada;

	/**
	 * Constructs a new Ada settings object.
	 */
	public AdaSettingsObject() { ada = new AdaObject(); }

	/**
	 * Sets the project file of this Ada settings object.
	 *
	 * @param projectFile The project file to set.
	 */
	public void setProjectFile(@NotNull String projectFile) {
		ada.projectFile = projectFile;
	}

	/**
	 * Sets the scenario variables of this Ada settings object.
	 *
	 * @param scenarioVariables The scenario variables to set.
	 */
	public void setScenarioVariables(@NotNull Map<String, String> scenarioVariables) {
		ada.scenarioVariables = scenarioVariables;
	}

	/**
	 * The "ada" property getter.
	 */
	@NotNull
	public AdaObject getAda() { return ada; }

}
