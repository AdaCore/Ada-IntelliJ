package com.adacore.adaintellij.project.template;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import com.adacore.adaintellij.Utils;

import static com.adacore.adaintellij.project.template.ProjectTemplateDescriptor.TemplateVariable;
import static com.adacore.adaintellij.project.template.ProjectTemplateDescriptor.TemplateVariableWithOptions;

/**
 * Parser of Ada project template descriptor files.
 */
final class ProjectTemplateFileParser {
	
	/**
	 * Represents the various sections of a project template
	 * descriptor file. Possible values are:
	 *
	 * HEADER      => The section of the template descriptor file
	 *                containing information about the template
	 *                such as the template name and category.
	 *
	 * VARIABLES   => The section of the template descriptor file
	 *                containing template variable names along with
	 *                their default values and descriptions.
	 *
	 * DESCRIPTION => The section of the template descriptor file
	 *                containing a description of the template.
	 */
	private enum ParsingSection { HEADER , VARIABLES , DESCRIPTION }
	
	/**
	 * Reads and parses the template descriptor file in the given
	 * resource project template directory into a project template
	 * descriptor, and returns it.
	 *
	 * @param templateDirectory The resource template directory
	 *                          containing the template files.
	 * @return A project template descriptor representing the
	 *         project template in the given directory.
	 * @throws Exception If the given virtual file is not a
	 *                   directory or if no template descriptor
	 *                   file is found inside.
	 */
	@NotNull
	static ProjectTemplateDescriptor getProjectTemplateDescriptor(
		@NotNull VirtualFile templateDirectory
	) throws Exception {
		
		// Check that the given virtual file represents a directory
		
		if (!templateDirectory.isDirectory()) {
			throw new Exception("Received virtual file must " +
				"be a directory: " + templateDirectory.getName());
		}
		
		// Find project template file in the template directory
		
		VirtualFile templateFile = null;
		
		for (VirtualFile child : templateDirectory.getChildren()) {
			
			if (!child.isDirectory() &&
				ProjectTemplateManager.TEMPLATE_DESCRIPTOR_FILE_EXTENSION.equals(child.getExtension()))
			{
				templateFile = child;
				break;
			}
			
		}
		
		// If no template file is found, throw an exception
		
		if (templateFile == null) {
			throw new Exception("Project template file not " +
				"found in template directory: " + templateDirectory.getName());
		}
		
		// Create a project template descriptor builder
		
		ProjectTemplateDescriptor.Builder descriptorBuilder =
			new ProjectTemplateDescriptor.Builder(templateDirectory);
		
		// Parse the template file and store the parsed data
		// in the passed template descriptor builder
		
		parseProjectTemplateFile(descriptorBuilder, templateFile);
		
		// Build and return the descriptor
		
		return descriptorBuilder.build();
		
	}
	
	/**
	 * Parses the given project template descriptor file into the
	 * given template descriptor builder.
	 *
	 * @param descriptorBuilder The descriptor builder into which
	 *                          to parse the template file.
	 * @param templateFile The template file to parse.
	 * @throws Exception If something goes wrong while reading the
	 *                   template descriptor file.
	 */
	private static void parseProjectTemplateFile(
		@NotNull ProjectTemplateDescriptor.Builder descriptorBuilder,
		@NotNull VirtualFile                       templateFile
	) throws Exception {
		
		// Get the template file lines
		
		String[] fileLines = Utils.getFileLines(templateFile);
		
		if (fileLines == null) {
			throw new Exception("Could not read project template file: " + templateFile.getName());
		}
		
		// Loop over the file lines to complete the builder
		
		ParsingSection parsingSection   = ParsingSection.HEADER;
		boolean        descriptionEmpty = true;
		
		for (String line : fileLines) {
			
			// Trim leading and trailing whitespaces
			
			line = line.trim();
			
			// Ignore empty lines
			
			if ("".equals(line)) { continue; }
			
			// If the line starts with '#', then set the parsing section
			
			if (line.startsWith("#")) {
				
				switch (line.toLowerCase()) {
					case "# header":      parsingSection = ParsingSection.HEADER;      break;
					case "# variables":   parsingSection = ParsingSection.VARIABLES;   break;
					case "# description": parsingSection = ParsingSection.DESCRIPTION; break;
				}
				
				continue;
				
			}
			
			// Handle the line based on the section being parsed
			
			switch (parsingSection) {
				
				case HEADER: {
					
					// Parse the line as a header line
					
					String[] parts = line.split(": ");
					
					if (parts.length != 2) {
						throw new Exception("Invalid project template file: " + templateFile.getName());
					}
					
					String key   = parts[0];
					String value = parts[1];
					
					// Set the appropriate field of the descriptor builder
					
					switch (key.toLowerCase()) {
						case "name":     descriptorBuilder.setTemplateName(value);     break;
						case "category": descriptorBuilder.setTemplateCategory(value); break;
						case "project":  descriptorBuilder.setProjectFileName(value);  break;
					}
					
					// Continue to the next line
					
					continue;
					
				}
				
				case VARIABLES: {
					
					// Parse the line as a template variable line
					
					String[] parts = line.split(": ");
					
					if (parts.length != 3) {
						throw new Exception("Invalid project template file: " + templateFile.getName());
					}
					
					String name         = parts[0];
					String defaultValue = parts[1];
					String description  = parts[2];
					
					// Look for variable options in the description
					
					String[] variableOptions  = null;
					String[] descriptionParts = description.split(":");
					
					if (descriptionParts.length == 2) {
						variableOptions = descriptionParts[1].split(";");
					}
					
					// Add the parsed variable to the descriptor builder
					
					TemplateVariable variable = variableOptions == null || variableOptions.length < 2 ?
						new TemplateVariable(name, defaultValue, description) :
						new TemplateVariableWithOptions(name, defaultValue, descriptionParts[0], variableOptions);
					
					descriptorBuilder.addTemplateVariable(variable);
					
					// Continue to the next line
					
					continue;
					
				}
				
				case DESCRIPTION: {
				
					// Ignore the "[Description]" line
				
					if ("[description]".equals(line.toLowerCase())) { continue; }
					
					// If the description already contains text, then
					// add a space before appending the next line
					
					if (!descriptionEmpty) {
						descriptorBuilder.appendToTemplateDescription(" ");
					}
					
					// Append the next line to the template description
					
					descriptorBuilder.appendToTemplateDescription(line);
					
					descriptionEmpty = false;
					
				}
				
			}
			
		}
		
	}
	
}
