
# Ada-IntelliJ 

Ada-IntelliJ is a plugin for IntelliJ-based IDEs adding support for the Ada programming language, with many planned features including syntax and error highlighting, code completion, reference resolution, project management, building, and more.

The plugin is currently under development and **most supported features are still experimental**. If you encounter problems/bugs, please [submit an issue](https://github.com/AdaCore/Ada-IntelliJ/issues/new) to this repository.

#### Supported Features

* Recognizing `.adb`, `.ads` and `.gpr` files
* Limited syntax highlighting for `.adb`, `.ads` and `.gpr` files
* Syntax error highlighting for `.adb` and `.ads` files
* Basic project file and GPRbuild support:
	* Build configurations
	* Build arguments
	* Scenario variables
	* Location hyperlinks in output errors
* Basic references/usages features:
	* Goto definition
	* Usage highlighting
	* Find references
* Global symbol renaming
* Code outline
* Code completion
* Quick line commenting/uncommenting
* Project creation from predefined templates

#### Sections

* [Development](#development)
* [Gradle](#gradle)
* [Building the Plugin](#building-the-plugin)
* [Running the Plugin](#running-the-plugin)
* [Testing the Plugin](#testing-the-plugin)

## Development

The plugin is written entirely in Java 8. The highest usable Java version is upper-bounded by that of the [JBRE](https://confluence.jetbrains.com/display/JRE/JetBrains+Runtime) which at the time of writing is based on OpenJDK 8.

The plugin architecture is centered around the [Microsoft Language Server Protocol](https://microsoft.github.io/language-server-protocol/) (LSP). It is therefore designed in such a way as to perform as little Ada code analysis as possible. Instead, it features an integrated LSP client and relies on an external Ada LSP server in order to achieve most of the smart features it provides. The integrated LSP client is tailored to work with the [Ada Language Server](https://github.com/AdaCore/ada_language_server) (ALS), a specific implementation of an LSP server for Ada with minor extensions to provide support for Ada-specific features such as project files and GPRbuild scenario variables.

The project depends directly on the following:
* The [IntelliJ platform](https://www.jetbrains.org/intellij/sdk/docs/)
* Eclipse [LSP4J](https://github.com/eclipse/lsp4j), a library for implementing LSP clients and servers
* [JUnit5](https://junit.org/junit5/) (for testing only)

The project makes heavy use of JetBrains annotations such as `@Contract(...)`, `@NotNull` and `@Nullable`. IntelliJ IDEA runs live inspections based on these annotations and reports redundancies, potential problems and improvements directly in the source code. It is therefore recommended to use IntelliJ IDEA when working on the project in order to make the most out of these annotations.

You can find a list of change notes [here](https://github.com/AdaCore/Ada-IntelliJ/blob/latest_release/CHANGE-NOTES.md).

Useful resources:
* [IntelliJ platform docs](https://www.jetbrains.org/intellij/sdk/docs/)
* [JetBrains support forums](https://intellij-support.jetbrains.com/hc/en-us/community/topics) (especially the "IntelliJ IDEA Open API and Plugin Development" section)
* [LSP specification](https://microsoft.github.io/language-server-protocol/specification)
* [LSP4J documentation](https://github.com/eclipse/lsp4j/tree/master/documentation)

## Gradle

The project uses [Gradle](https://gradle.org/) and the [Gradle IntelliJ plugin](https://plugins.gradle.org/plugin/org.jetbrains.intellij) for building, testing, packaging and deploying the plugin. In the following sections, the given instructions involve running Gradle tasks from the command-line using the Gradle wrapper script. A Gradle task called `example` can be run as follows:

```
# Linux / macOS
./gradlew example
```
```
@REM Windows
gradlew.bat example
```

Note that all Gradle tasks can also be run from within IntelliJ IDEA's Gradle plugin interface.

## Building the Plugin

#### Steps

1. Clone the project (or [download the latest release](https://github.com/AdaCore/Ada-IntelliJ/archive/latest_release.zip) and extract it) and move into the root directory

2. Run the Gradle wrapper script with the `build` task

The `build` task involves running JUnit tests (see [Testing the plugin](#testing-the-plugin)) after compiling the Java sources, which is recommended when running the plugin by [installing it from disk](#installing-the-plugin-from-disk) to ensure that it passes the tests first. If however you wish to build the plugin without running the tests, use the `buildPlugin` task instead.

All build-generated files reside in the `build/` directory and can be cleaned up by running the `clean` task.

## Running the Plugin

There are currently two ways to run the plugin in an IntelliJ IDE.

* [Using Gradle (recommended)](#using-gradle-recommended)
* [Installing the Plugin from Disk](#installing-the-plugin-from-disk)

Note that in order to enable smart features such as code completion and goto-definition, you need to install the Ada Language Server (follow the instructions [here](https://github.com/AdaCore/ada_language_server#install)).

### Using Gradle (recommended)

This is the easier and recommended method as it does not require any additional installation, not even an IntelliJ-based IDE! The Gradle wrapper takes care of fetching all the dependencies, including an appropriate version of IntelliJ IDEA CE, and running it in a sandboxed environment with the plugin installed.

Even though the plugin should in general never affect the configuration of the IDE instance in which it is installed, this may still happen due to a bug that might appear during the development phase, which is why this method is recommended as it does not involve installing the plugin to your IDE installation. If however you wish to try the plugin without giving up all your IDE configurations, or you wish to try it on an IntelliJ IDE other than IDEA, then you may proceed with [Installing the Plugin from Disk](#installing-the-plugin-from-disk) at your own risk.

#### Steps

1. Clone the project (or [download the latest release](https://github.com/AdaCore/Ada-IntelliJ/archive/latest_release.zip) and extract it) and move into the root directory

Before running the plugin, you may want to [run the tests](#testing-the-plugin) and make sure they all pass.

2. Run the Gradle wrapper script with task `runIde`

That's it! If all goes well, IntelliJ IDEA should start with the plugin installed.

### Installing the Plugin from Disk

This method requires building the project with Gradle before installing it to an IntelliJ IDE.

#### Steps

1. [Clone/Download and build the plugin](#building-the-plugin), making sure that the build is successful

2. Open the IntelliJ-based IDE of your choice and go to `File | Settings | Plugins`

3. Click on `Install plugin from disk...` (to access this option in newer version, you first need to click on the gear icon next to the `Updates` tab)

4. In the file chooser that opens, navigate to the directory of the cloned/extracted Ada-IntelliJ repo, then from there navigate into `build > distributions` and choose the zip archive named `Ada-IntelliJ-<version>.zip` (if the `distributions` directory does not exist then the build in step 1 probably failed)

5. Finally, in the `Plugins` tab of the `Settings` window, click on `Restart <IDE name>`

If all goes well, your IDE should restart with the plugin installed.

## Testing the Plugin

The project uses JUnit5 for testing.

Currently, only the lexer and lexer regex classes are tested. We have plans to set up more comprehensive tests, which is tricky given the limitations of testing within the IntelliJ platform.

Test source files are located in [`src/test/control/`](https://github.com/AdaCore/Ada-IntelliJ/tree/master/src/test/control) and test resource files are located in [`src/test/resources/`](https://github.com/AdaCore/Ada-IntelliJ/tree/master/src/test/resources).

#### Steps

1. Clone the project (or [download the latest release](https://github.com/AdaCore/Ada-IntelliJ/archive/latest_release.zip) and extract it) and move into the root directory

2. Run the Gradle wrapper script with task `test`

If no test failures are reported, then all the tests passed.

A comprehensive test report including success rates and execution durations is automatically generated by Gradle in HTML form and can be found in `build/reports/tests/test/`.
