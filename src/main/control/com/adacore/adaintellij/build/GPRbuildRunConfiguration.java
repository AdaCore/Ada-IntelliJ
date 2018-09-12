package com.adacore.adaintellij.build;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.*;

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

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.adacore.adaintellij.project.GPRFileManager;
import com.adacore.adaintellij.Utils;

/**
 * Run configuration for running GPRbuild.
 */
public final class GPRbuildRunConfiguration extends RunConfigurationBase {
	
	/**
	 * The project in which this configuration will be run.
	 */
	private Project project;
	
	/**
	 * The arguments that will be passed to gprbuild.
	 */
	private String gprbuildArguments = "";
	
	/**
	 * The path to the GPR file to use in this configuration.
	 * This path may be the empty string, in which case the project's
	 * default GPR file will be used.
	 */
	private String customGprFilePath = "";
	
	/**
	 * Constructs a new GPRbuildRunConfiguration given a project, a factory
	 * and a name.
	 *
	 * @param project The project in which this configuration will be run.
	 * @param factory The factory that generated this configuration.
	 * @param name The name of this configuration.
	 */
	GPRbuildRunConfiguration(Project project, GPRbuildConfigurationFactory factory, String name) {
		super(project, factory, name);
		this.project = project;
	}
	
	/**
	 * @see com.intellij.execution.configurations.RunConfiguration#getConfigurationEditor()
	 */
	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
		return new GPRbuildSettingsEditor();
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
	@Nullable
	@Override
	public RunProfileState getState(
		@NotNull Executor executor,
		@NotNull ExecutionEnvironment executionEnvironment
	) {
		
		return new CommandLineState(executionEnvironment) {
			
			@NotNull
			@Override
			public ExecutionResult execute(
				@NotNull Executor executor,
				@NotNull ProgramRunner runner
			) throws ExecutionException {
				
				ProcessHandler processHandler = startProcess();
				ConsoleView    consoleView    =
					TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
				
				consoleView.addMessageFilter(new GPRbuildOutputFilter());
				consoleView.attachToProcess(processHandler);
				
				return new DefaultExecutionResult(consoleView, processHandler);
				
			}
			
			@NotNull
			@Override
			protected ProcessHandler startProcess() throws ExecutionException {
				
				String gprbuildPath = GPRbuildManager.getGprbuildPath();
				String gprFilePath  = getEffectiveGprFilePath();
				
				if (gprbuildPath == null) {
					throw new ExecutionException(
						"No gprbuild path specified. Go to `Run | Edit" +
							" Configurations...` to set the gprbuild path."
					);
				} else if (gprFilePath == null) {
					throw new ExecutionException(
						"No `.gpr` file found in project. Add a" +
							"`.gpr` file to the project's base directory."
					);
				}
				
				GeneralCommandLine commandLine =
					new GeneralCommandLine(gprbuildPath, "-P" + gprFilePath, gprbuildArguments);
				commandLine.createProcess();
				
				OSProcessHandler processHandler = new OSProcessHandler(commandLine);
				ProcessTerminatedListener.attach(processHandler);
				processHandler.startNotify();
				
				return processHandler;
				
			}
			
		};
		
	}
	
	/**
	 * Returns the path of the custom GPR file used in this configuration.
	 *
	 * @return The path of the custom GPR file.
	 */
	String getCustomGprFilePath() { return customGprFilePath; }
	
	/**
	 * Sets the path of the custom GPR file used in this configuration.
	 *
	 * @param customGprFilePath The new GPR file path.
	 */
	void setCustomGprFilePath(String customGprFilePath) {
		this.customGprFilePath = customGprFilePath;
	}
	
	/**
	 * Returns the path of the effective GPR file used in this configuration,
	 * that is:
	 * - The custom GPR file if its path is not the empty string
	 * - The project's default GPR file path (which may be null) otherwise
	 *
	 * @return The effective GPR file used in this configuration.
	 */
	@Nullable
	private String getEffectiveGprFilePath() {
		
		GPRFileManager gprFileManager = project.getComponent(GPRFileManager.class);
		
		return "".equals(customGprFilePath) ?
			gprFileManager.defaultGprFilePath() : customGprFilePath;
		
	}
	
	/**
	 * TEMPORARY!
	 *
	 * Whether or not the user has already been notified about
	 * gps_cli not being on the PATH.
	 *
	 * TODO: Remove this with gps_cli hack
	 */
	/////////////////////////////////////////////////////////////////////////
	//                                                                     //
	private static boolean notifiedAboutGpsCli = false;
	//                                                                     //
	/////////////////////////////////////////////////////////////////////////
	
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
			
			/**
			 * TEMPORARY!
			 *
			 * TODO: Remove this with gps_cli hack
			 */
			/////////////////////////////////////////////////////////////////////////
			//                                                                     //
			if (!Utils.isOnSystemPath(GpsCli.COMMAND, false)) {
				
				if (!notifiedAboutGpsCli) {
					
					// Notify the user that gps_cli needs to be on the path
					Notifications.Bus.notify(new AdaIJNotification(
						"Add gps_cli to PATH for output file location hyperlinks",
						"File location hyperlinks from gprbuild output is an in-dev" +
							" feature and currently requires gps_cli to be on the PATH.",
						NotificationType.INFORMATION
					));
					
					notifiedAboutGpsCli = true;
					
				}
				
				return null;
				
			}
			//                                                                     //
			/////////////////////////////////////////////////////////////////////////
			
			Matcher matcher = PATTERN.matcher(line);
			
			int lineStart = entireLength - line.length();
			
			while (matcher.find()) {
				
				MatchResult      matchResult       = matcher.toMatchResult();
				String           match             = matchResult.group();
				Iterator<String> matchPartIterator = Arrays.asList(match.split(":")).iterator();
				
				StringBuilder    filenameBuilder   = new StringBuilder(matchPartIterator.next());
				
				if (match.charAt(1) == ':') {
					filenameBuilder.append(':');
					filenameBuilder.append(matchPartIterator.next());
				}
				
				String filename = filenameBuilder.toString();
				String filePath = "";
				
				/**
				 * TEMPORARY HACK:
				 * @see com.adacore.adaintellij.build.GpsCli
				 *
				 * TODO: Properly get sources from GPR files instead
				 */
				/////////////////////////////////////////////////////////////////////////
				//                                                                     //
				
				List<String> sources;
				
				String gprFilePath = getEffectiveGprFilePath();
				
				try {
					sources = GpsCli.projectSources(project, gprFilePath);
				} catch (Exception e) { return null; }
				
				String filePathSeparator = System.getProperty("file.separator");
				
				for (String sourcePath : sources) {
					
					String[] pathParts = sourcePath.split(filePathSeparator);
					
					if (pathParts[pathParts.length - 1].equals(filename)) {
						filePath = sourcePath;
						break;
					}
					
				}
				
				if ("".equals(filePath)) {
					
					filePath = null;
					
					List<String> objectDirectories;
					
					try {
						objectDirectories = GpsCli.projectObjectDirectories(project, gprFilePath);
					} catch (Exception e) { return null; }
					
					final AtomicReference<String> objectFilePathReference = new AtomicReference<>();
					
					for (String objectDirectoryPath : objectDirectories) {
					
						VirtualFile objectDirectory =
							LocalFileSystem.getInstance().findFileByPath(objectDirectoryPath);
						
						if (objectDirectory == null) { continue; }
						
						VfsUtilCore.iterateChildrenRecursively(
							objectDirectory,
							null,
							fileOrDir -> {
								
								if (fileOrDir.isDirectory()) { return true; }
								
								if (fileOrDir.getName().equals(filename)) {
									objectFilePathReference.set(fileOrDir.getPath());
									return false;
								}
								
								return true;
								
							}
						);
						
						filePath = objectFilePathReference.get();
						
						if (filePath != null) { break; }
						
					}
					
					if (filePath == null) { return null; }
					
				}
				
				//                                                                     //
				/////////////////////////////////////////////////////////////////////////
				
				VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
				
				if (virtualFile == null) { continue; }
				
				int lineNumber   = Integer.parseInt(matchPartIterator.next()) - 1;
				int columnNumber = matchPartIterator.hasNext() ?
					Integer.parseInt(matchPartIterator.next()) - 1 : 0;
				
				// TODO: Find a way to return results for all matches in a line, see:
				//       https://upsource.jetbrains.com/idea-ce/file/idea-ce-d00d8b4ae3ed33097972b8a4286b336bf4ffcfab/platform/lang-api/src/com/intellij/execution/filters/Filter.java
				return new Filter.Result(
					lineStart + matchResult.start(),
					lineStart + matchResult.end(),
					new OpenFileHyperlinkInfo(project, virtualFile, lineNumber, columnNumber)
				);
				
			}
			
			return null;
			
		}
		
	}
	
}
