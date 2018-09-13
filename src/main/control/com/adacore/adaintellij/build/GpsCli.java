package com.adacore.adaintellij.build;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.project.Project;

/**
 * TODO: Remove this in the future
 *
 * Class providing methods that interact with the GPS command-line
 * interface. This is a temporary hack to accomplish tasks that would
 * later be supported natively by the plugin.
 * This requires that GPS be installed and the gps_cli command be
 * on the system's PATH.
 */
public final class GpsCli {
	
	/**
	 * The name of the main command.
	 */
	public static final String COMMAND = "gps_cli";
	
	/**
	 * Returns a list of paths to the source files in the given project.
	 *
	 * @param project The project to get source files from.
	 * @param gprFilePath The path to the GPR file to use with gps_cli.
	 * @return A list of source file paths.
	 * @throws Exception If something goes wrong.
	 */
	static List<String> projectSources(Project project, String gprFilePath) throws Exception {
		
		File script = generateScript(project.getBasePath(),
			"def execute():\n" +
			"\tfor file in GPS.Project.root().sources(True):\n" +
			"\t\tprint file.name()\n");
		
		if (gprFilePath == null) {
			throw new Exception("No GPR file configured.");
		}
		
		Process process = new ProcessBuilder(
			COMMAND,
			"-P" + gprFilePath,
			"--load=python:" + script.getAbsolutePath()
		).start();
		
		List<String> lines = getProcessOutputLines(process);
		
		script.delete();
		
		return lines;
		
	}
	
	/**
	 * Returns a list of paths to the object directories in the given project.
	 *
	 * @param project The project to get object directory paths from.
	 * @param gprFilePath The path to the GPR file to use with gps_cli.
	 * @return A list of object directories paths.
	 * @throws Exception If something goes wrong.
	 */
	static List<String> projectObjectDirectories(Project project, String gprFilePath) throws Exception {

		File script = generateScript(project.getBasePath(),
			"def execute():\n" +
				"\tfor directory_name in GPS.Project.root().object_dirs(False):\n" +
				"\t\tprint directory_name\n");
		
		if (gprFilePath == null) {
			throw new Exception("No GPR file configured.");
		}
		
		Process process = new ProcessBuilder(
			COMMAND,
			"-P" + gprFilePath,
			"--load=python:" + script.getAbsolutePath()
		).start();
		
		List<String> lines = getProcessOutputLines(process);
		
		script.delete();
		
		return lines;
		
	}
	
	/**
	 * Generates and returns a script file at the given path with the given
	 * source code.
	 *
	 * @param scriptParentPath The path to the directory in which to create
	 *                         the script.
	 * @param text The source code to write in the script.
	 * @return The script file.
	 * @throws Exception If something goes wrong.
	 */
	private static File generateScript(String scriptParentPath, String text) throws Exception {

		String scriptPath = scriptParentPath + "/ada-intellij-gps-cli-script.py";

		File script = new File(scriptPath);
		
		if (script.exists()) { script.delete(); }
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(script));
		writer.write(text);
		writer.close();
		
		return script;
		
	}
	
	/**
	 * Reads the output of the given process line by line and returns
	 * it as a list of strings, one for each line.
	 *
	 * @param process The process to read output from.
	 * @return The list of lines output by the process.
	 * @throws Exception If something goes wrong.
	 */
	private static List<String> getProcessOutputLines(Process process) throws Exception {
		
		InputStream    inputStream = process.getInputStream();
		BufferedReader reader      = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		
		List<String> lines = new ArrayList<>();
		String       line  = reader.readLine();
		
		while (line != null) {
			lines.add(line);
			line = reader.readLine();
		}
		
		process.waitFor();
		
		reader.close();
		
		return lines;
		
	}
	
}
