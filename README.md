
# Ada-IntelliJ

Ada-IntelliJ is a plugin for IntelliJ IDEs adding support for the Ada programming language, with many planned features including syntax and error highlighting, code completion, reference resolution, project management, building, and more.

Features currently supported:
* Recognizing `.adb`, `.ads` and `.gpr` files
* Syntax highlighting for `.adb` and `.ads` files
* Basic gprbuild support

The plugin is currently under development. If you encounter problems/bugs, please [submit an issue](https://github.com/AdaCore/Ada-IntelliJ/issues/new) to this repository.

Sections:
* [Gradle](#gradle)
* [Building the Plugin](#building-the-plugin)
* [Running the Plugin](#running-the-plugin)
* [Testing the Plugin](#testing-the-plugin)
* [Change Notes](#change-notes)

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

1. Clone the project and move into the root directory:

```
git clone https://github.com/AdaCore/Ada-IntelliJ.git
cd Ada-IntelliJ
```

Alternatively, you may [download the source code of the master branch](https://github.com/AdaCore/Ada-IntelliJ/archive/master.zip) as a zip archive and extract it:

```
unzip Ada-IntelliJ-master.zip
cd Ada-IntelliJ-master
```

2. Run the Gradle wrapper script with the `build` task. The `build` task involves running JUnit tests (see [Testing the plugin](#testing-the-plugin)) after compiling the Java sources, which is recommended when running the plugin by [installing it from disk](#installing-the-plugin-from-disk) to ensure that it passes the tests first. If however you wish to build the plugin without running the tests, use the `buildPlugin` task instead.

All build-generated files reside in the `build/` directory and can be cleaned up by running the `clean` task.

## Running the Plugin

There are currently two ways to run the plugin in an IntelliJ IDE.

* [Using Gradle (recommended)](#using-gradle-recommended)
* [Installing the Plugin from Disk](#installing-the-plugin-from-disk)

### Using Gradle (recommended)

This is the easier and recommended method as it does not require any additional installation, not even an IntelliJ IDE! The Gradle wrapper takes care of fetching all the dependencies, including an appropriate version of IntelliJ IDEA, and running it in a sandboxed environment with the plugin installed.

Even though the plugin should in general never affect the configuration of the IDE instance in which it is installed, this may still happen due to a bug that might appear during the development phase, which is why this method is recommended as it does not involve installing the plugin to your IDE installation. If however you wish to try the plugin without giving up all your IDE configurations, or you wish to try it on an IntelliJ IDE other than IDEA, then you may proceed with [Installing the Plugin from Disk](#installing-the-plugin-from-disk) at your own risk.

#### Steps

1. Clone the project and move into the root directory, as in step 1 of [Building the Plugin](#building-the-plugin)

2. Run the Gradle wrapper script with task `runIde`

That's it! If all goes well, IntelliJ IDEA should start with the plugin installed.

### Installing the Plugin from Disk

This method requires building the project with Gradle before installing it to an IntelliJ IDE.

#### Steps

1. [Clone/Download and build the plugin](#building-the-plugin), making sure that the build is successful

2. Open the IntelliJ IDE of your choice and go to `File | Settings | Plugins` and click on `Install plugin from disk...`

3. In the file chooser that opens, navigate to the directory of the cloned/extracted Ada-IntelliJ repo, then from there navigate into `build > distributions` and choose the zip archive named `Ada-IntelliJ-<version>.zip` (if the `distributions` directory does not exist than the build in step 1 probably failed)

4. Finally, in the `Plugins` tab of the `Settings` window, click on `Restart <IDE name>`

If all goes well, your IDE should restart with the plugin installed.

## Testing the plugin

The project uses JUnit5 to test part of its implementation. Most non-private methods whose function/behavior is not strongly tied to the IntelliJ platform (e.g. registering file types, saving/restoring run configuration settings, etc.) and that are not system-dependent (e.g. searching the PATH for executables) are tested, each with various scenarios.

Test source files are located in [`src/test/control/`](https://github.com/AdaCore/Ada-IntelliJ/tree/master/src/test/control) and [`src/test/ui`](https://github.com/AdaCore/Ada-IntelliJ/tree/master/src/test/ui), and test resource files are located in [`src/test/resources`](https://github.com/AdaCore/Ada-IntelliJ/tree/master/src/test/resources).

#### Steps

1. Clone the project and move into the root directory, as in step 1 of [Building the Plugin](#building-the-plugin)

2. Run the Gradle wrapper script with task `test`

If no test failures are reported, then all the tests passed.

## Change Notes

###### 0.2-dev

* IDE recognizes GPR (.gpr) files (no syntax highlighting)
* Basic gprbuild support, build configurations with limited customization
* gprbuild output hyperlinks to source code (requires gps_cli to be on PATH)
* Basic GPR file management

###### 0.1-dev

* IDE recognizes Ada body (.adb) and spec (.ads) files
* Basic syntax highlighting for body and spec files

##### 0-dev

