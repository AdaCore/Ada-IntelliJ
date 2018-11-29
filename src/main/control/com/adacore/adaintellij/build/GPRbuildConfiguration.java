package com.adacore.adaintellij.build;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;

import org.jdom.Element;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.adacore.adaintellij.project.GPRFileManager;

/**
 * Run configuration for running GPRbuild.
 */
public final class GPRbuildConfiguration extends RunConfigurationBase {
	
	/**
	 * The project in which this configuration will be run.
	 */
	private Project project;
	
	/**
	 * The arguments that will be passed to gprbuild.
	 */
	private String gprbuildArguments = "";
	
	/**
	 * The scenario variables that will be passed to gprbuild.
	 */
	private Map<String, String> scenarioVariables = new HashMap<>();
	
	/**
	 * Whether or not the user has already been notified about using
	 * the "-gnatef" flag for gprbuild output file location hyperlinks.
	 */
	private static boolean notifiedAboutFullPathFlag = false;
	
	/**
	 * Constructs a new GPRbuildConfiguration given a project, a factory
	 * and a name.
	 *
	 * @param project The project in which this configuration will be run.
	 * @param factory The factory that generated this configuration.
	 * @param name The name of this configuration.
	 */
	GPRbuildConfiguration(Project project, GPRbuildConfigurationFactory factory, String name) {
		super(project, factory, name);
		this.project = project;
	}
	
	/**
	 * @see com.intellij.execution.configurations.RunConfiguration#getConfigurationEditor()
	 */
	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new GPRbuildConfigurationEditor.SettingsEditorAdapter();
	}
	
	/**
	 * @see com.intellij.execution.configurations.RunConfiguration#checkConfiguration()
	 */
	@Override
	public void checkConfiguration() throws RuntimeConfigurationException {
		super.checkConfiguration();
	}
	
	/**
	 * @see com.intellij.execution.configurations.RunProfile#getState(Executor, ExecutionEnvironment)
	 */
	@NotNull
	@Override
	public RunProfileState getState(
		@NotNull Executor executor,
		@NotNull ExecutionEnvironment executionEnvironment
	) {
		
		return new CommandLineState(executionEnvironment) {
			
			/**
			 * @see com.intellij.execution.configurations.RunProfileState#execute(Executor, ProgramRunner)
			 */
			@NotNull
			@Override
			public ExecutionResult execute(
				@NotNull Executor executor,
				@NotNull ProgramRunner runner
			) throws ExecutionException {
				
				if (!notifiedAboutFullPathFlag && !gprbuildArguments.contains("-gnatef")) {
					
					Notifications.Bus.notify(new AdaIJNotification(
						"Use `-gnatef` build flag for output file location hyperlinks",
						"To be able to resolve file locations in the output of gprbuild, the " +
							"Ada-IntelliJ plugin currently requires the use of the `-gnatef` flag.",
						NotificationType.INFORMATION
					));
					
					notifiedAboutFullPathFlag = true;
					
				}
				
				ProcessHandler processHandler = startProcess();
				ConsoleView    consoleView    =
					TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
				
				consoleView.addMessageFilter(new GPRbuildOutputFilter());
				consoleView.attachToProcess(processHandler);
				
				return new DefaultExecutionResult(consoleView, processHandler);
				
			}
			
			/**
			 * @see com.intellij.execution.configurations.CommandLineState#startProcess()
			 */
			@NotNull
			@Override
			protected ProcessHandler startProcess() throws ExecutionException {
				
				GPRFileManager gprFileManager = GPRFileManager.getInstance(project);
				
				String gprbuildPath = GPRbuildManager.getGprbuildPath();
				String gprFilePath  = gprFileManager.getGprFilePathOrChoose();
				
				if ("".equals(gprbuildPath)) {
					throw new ExecutionException(
						"No gprbuild path specified. Go to `File | Settings... | " +
							"Languages & Frameworks | Ada` to set the gprbuild path."
					);
				} else if ("".equals(gprFilePath)) {
					throw new ExecutionException(
						"No GPR file found in project. Add a" +
							"`.gpr` file to the project's base directory."
					);
				}
				
				// Store scenario variable settings as strings
				
				List<String> commandLineScenarioVariables =
					scenarioVariables.entrySet()
						.stream()
						.map(entry -> "-X" + entry.getKey() + "=" + entry.getValue())
						.collect(Collectors.toList());
				
				// Construct the gprbuild command to run
				
				GeneralCommandLine commandLine =
					new GeneralCommandLine(gprbuildPath, "-P" + gprFilePath, gprbuildArguments);
				
				commandLine.addParameters(commandLineScenarioVariables);
				
				// Start the command process
				
				commandLine.createProcess();
				
				OSProcessHandler processHandler = new OSProcessHandler(commandLine);
				ProcessTerminatedListener.attach(processHandler);
				processHandler.startNotify();
				
				return processHandler;
				
			}
			
		};
		
	}
	
	/**
	 * Serializes the settings in this configuration into the given XML element.
	 * Used to save the state of configurations when the IDE is closed.
	 *
	 * @param element The element to which to write data.
	 */
	@Override
	public void writeExternal(@NotNull Element element) {
		
		Element gprbuildArgumentsElement = new Element("gprbuildArguments");
		Element scenarioVariablesElement = new Element("scenarioVariables");
		
		gprbuildArgumentsElement.addContent(gprbuildArguments);
		
		scenarioVariables.forEach((variable, value) -> {
			
			Element variableElement = new Element("scenarioVariable");
			
			variableElement.setAttribute("variable", variable);
			variableElement.setAttribute("value"   , value);
			
			scenarioVariablesElement.addContent(variableElement);
			
		});
		
		element.addContent(gprbuildArgumentsElement);
		element.addContent(scenarioVariablesElement);
	
	}
	
	/**
	 * Deserializes the data in the given XML element into this configuration.
	 * Used to restore the state of configurations when the IDE is started.
	 *
	 * @param element The element from which to read data.
	 */
	@Override
	public void readExternal(@NotNull Element element) {
		
		Element gprbuildArgumentsElement = element.getChild("gprbuildArguments");
		Element scenarioVariablesElement = element.getChild("scenarioVariables");
		
		if (gprbuildArgumentsElement != null) {
			gprbuildArguments = gprbuildArgumentsElement.getText();
		}
		
		if (scenarioVariablesElement != null) {
			
			scenarioVariablesElement.getContent().forEach(content -> {
				
				Element variableElement = (Element)content;
				
				scenarioVariables.put(
					variableElement.getAttributeValue("variable"),
					variableElement.getAttributeValue("value")
				);
				
			});
			
		}
		
	}
	
	/**
	 * Returns the gprbuild build arguments of this configuration.
	 *
	 * @return The build arguments of this configuration.
	 */
	@Contract(pure = true)
	@NotNull
	String getGprbuildArguments() { return gprbuildArguments; }
	
	/**
	 * Sets the gprbuild build arguments of this configuration.
	 *
	 * @param buildArguments The new build arguments.
	 */
	void setGprbuildArguments(@NotNull String buildArguments) {
		gprbuildArguments = buildArguments;
	}
	
	/**
	 * Returns the scenario variable settings of this configuration.
	 *
	 * @return The scenario variable settings of this configuration.
	 */
	@Contract(pure = true)
	@NotNull
	Map<String, String> getScenarioVariables() {
		return Collections.unmodifiableMap(scenarioVariables);
	}
	
	/**
	 * Sets the scenario variables of this configuration.
	 *
	 * @param scenarioVariables The new scenario variables of this
	 *                          configuration.
	 */
	void setScenarioVariables(@NotNull Map<String, String> scenarioVariables) {
	
		this.scenarioVariables = new HashMap<>();
	
		this.scenarioVariables.putAll(scenarioVariables);
		
	}
	
	/**
	 * Output filter, to be used with the output of gprbuild, for attaching
	 * hyperlinks to certain parts of the output, such as a link to line 23
	 * column 7 of file main.adb for the string "main.adb:23:7".
	 */
	private final class GPRbuildOutputFilter implements Filter {
		
		/**
		 * Pattern matching source file locations.
		 */
		private final Pattern PATTERN = Pattern.compile("^([^:]:?[^:]*):(\\d+):((\\d+):)? " +
				"(((medium )?warning|medium:)?(info|Note|check)?(\\(style|low:|low warning:)?.*)");
		
		/**
		 * @see com.intellij.execution.filters.Filter#applyFilter(String, int)
		 */
		@Nullable
		@Override
		public Filter.Result applyFilter(String line, int entireLength) {
			
			Matcher matcher = PATTERN.matcher(line);
			
			int lineStart = entireLength - line.length();
			
			// Try to find a match
			
			if (!matcher.find()) { return null; }
			
			MatchResult      matchResult       = matcher.toMatchResult();
			String           match             = matchResult.group();
			Iterator<String> matchPartIterator = Arrays.asList(match.split(":")).iterator();
			
			String filePath = matchPartIterator.next();
			
			// Find the referenced file
			
			VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
			
			if (virtualFile == null) { return null; }
			
			// Parse line/column numbers
			
			int lineNumber   = Integer.parseInt(matchPartIterator.next()) - 1;
			int columnNumber = matchPartIterator.hasNext() ?
				Integer.parseInt(matchPartIterator.next()) - 1 : 0;
			
			// Return the result
			
			return new Filter.Result(
				lineStart + matchResult.start(),
				lineStart + matchResult.end(),
				new OpenFileHyperlinkInfo(project, virtualFile, lineNumber, columnNumber)
			);
			
		}
		
	}
	
}
