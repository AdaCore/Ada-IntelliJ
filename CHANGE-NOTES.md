
# Change Notes

###### 0.6-dev

* Updated "until-build" IDEA version to support future versions of IDEs
* Fixed bug related to finding files in windows ([#57](https://github.com/AdaCore/Ada-IntelliJ/pull/57))
* Fixed null-related bug in Ada PSI element comparison ([#59](https://github.com/AdaCore/Ada-IntelliJ/pull/59))
* Fixed bugs in GPRbuild tool window and GPRbuild configurations ([#64](https://github.com/AdaCore/Ada-IntelliJ/pull/64))
* Fixed lexer bug generating keyword tokens after apostrophe tokens ([#65](https://github.com/AdaCore/Ada-IntelliJ/pull/65))

###### 0.5-dev

* Fixed minor bugs in project wizard and project templates ([#54](https://github.com/AdaCore/Ada-IntelliJ/pull/54))
* Fixed GPR file selection dialog sometimes causing IDE to freeze ([#54](https://github.com/AdaCore/Ada-IntelliJ/pull/54))

###### 0.4-dev

* Registration of separate GPR file language
* Separate lexer and token-based highlighter for `.gpr` files
* GPRbuild scenario variables
* GPRbuild configuration serialization/deserialization on IDE shutdown/startup
* GPRbuild configuration tool window
* Removed `gps_cli` requirement for GPRbuild output hyperlinks in favor of using `-gnatef` flag
* Separate global Ada settings and project settings
* Quick line commenting/uncommenting
* Global symbol renaming
* Project wizard with project templates
* Support for LSP v3.13.0
* LSP request timeouts
* Ada program structure view / code outline (using ALS)
* Ada code annotation / syntax error highlighting (using ALS)
* Fixed plugin sending LSP `textDocument/references` requests for PSI viewer mock files ([#19](https://github.com/AdaCore/Ada-IntelliJ/pull/19))
* Fixed reference highlighting bug ([#19](https://github.com/AdaCore/Ada-IntelliJ/pull/19))
* Fixed lexer end-of-file bug ([#33](https://github.com/AdaCore/Ada-IntelliJ/pull/33))
* Performance improvements ([#42](https://github.com/AdaCore/Ada-IntelliJ/pull/42), [#43](https://github.com/AdaCore/Ada-IntelliJ/pull/43))

###### 0.3-dev

* Automatic creation of default GPRbuild configurations
* Automatic GPR-file-based detection of Ada projects
* Integrated LSP client (supporting LSP v3.10.0) for ALS
* PSI element family implementations for Ada
* Ada parser creating flat PSI trees from Ada source files
* Resolving references (using ALS)
* Finding references / usage highlighting (using ALS)
* Code completion (using ALS)
* Fixed apostrophe-related lexer bugs ([#12](https://github.com/AdaCore/Ada-IntelliJ/pull/12))
* Fixed GPR file selection dialog sometimes causing IDE to freeze ([#13](https://github.com/AdaCore/Ada-IntelliJ/pull/13))

###### 0.2-dev

* Registration of the `.gpr` file type
* File icons for `.adb`, `.ads` and `.gpr` files
* Basic GPRbuild configurations with limited customization
* GPRbuild output hyperlinks to source code (requires `gps_cli` to be on `PATH`)
* Basic GPR file manager
* Fixed non-case-insensitive lexical analysis ([#2](https://github.com/AdaCore/Ada-IntelliJ/issues/2), [#5](https://github.com/AdaCore/Ada-IntelliJ/pull/5))

###### 0.1-dev

* Registration of Ada language along with `.adb` and `.ads` file types
* Ada lexer and token-based highlighter for `.adb` and `.ads` files
