# Turtle 🐢

[![CircleCI](https://circleci.com/gh/lordcodes/turtle.svg?style=svg)](https://circleci.com/gh/lordcodes/turtle)
[![Bintray](https://api.bintray.com/packages/lordcodes/maven/turtle/images/download.svg)](https://bintray.com/lordcodes/maven/turtle/_latestVersion)
[![@lordcodes](https://img.shields.io/badge/contact-@lordcodes-blue.svg?style=flat)](https://twitter.com/lordcodes)
[![Lord Codes Blog](https://img.shields.io/badge/blog-Lord%20Codes-yellow.svg?style=flat)](https://www.lordcodes.com)

Run shell commands from a Kotlin script or application with ease.

Turtle simplifies the process of running external commands and processes from your Kotlin (or Java) code. It comes bundled with a selection of built-in functions, such as opening MacOS applications and dealing with Git. Running shell commands easily is particularly useful from within Kotlin scripts, command line applications and Gradle tasks.

&nbsp;

<p align="center">
    <a href="#features">Features</a> • <a href="#installation">Installation</a> • <a href="#usage">Usage</a>
</p>

## Features

#### ▶︎ Run shell commands with minimal boilerplate

Simply specify the comamnd and its arguments to easily run and retrieve output.

#### ▶︎ Call any of the built-in shell commands

Various commands are provided, such as creating a Git commit and opening files.

#### ▶︎ Use the function syntax to run a series of commands

Specify a sequence of commands to run within a function block.

#### ▶︎ Capture error exit code and output

When a command produces an error, the exit code and error output is thrown as an exception.

## Installation

Turtle is provided as a Gradle/Maven dependency, which will be available via Bintray and Maven Central soon.

#### ▶︎ Gradle Kotlin DSL

```gradle
dependencies {
  implementation("com.lordcodes.turtle:turtle:0.2.0")
}
```

#### ▶︎ Gradle Groovy DSL

```gradle
dependencies {
  implementation 'com.lordcodes.turtle:turtle:0.2.0'
}
```

## Usage

To run a single custom command, just call `shellRun()` and provide the command and arguments.

```kotlin
val output = shellRun("git", listOf("rev-parse", "--abbrev-ref", "HEAD"))
println(output) // Current branch name, e.g. master
```

The working directory can be provided, to run the command in a particular location. `ShellLocation` provides easy access to some useful locations, such as the user's home directory.

```kotlin
val turtleProject = ShellLocation.HOME.resolve("projects/turtle")
val output = shellRun("git", listOf("rev-parse", "--abbrev-ref", "HEAD"), turtleProject)
println(output) // Current branch name, e.g. master
```

To run a series of commands or use the built-in commands, just call `shellRun {}`.

```kotlin
shellRun {
  command("mkdir tortoise")
  
  changeWorkingDirectory("tortoise")
  
  git.commit("Initial commit")
  git.addTag("v1.2", "Release v1.2")
  
  files.openApplication("Spotify")
}
```

The initial working directory can be specified.

```kotlin
val turtleProject = ShellLocation.HOME.resolve("projects/turtle")
shellRun(turtleProject) {
  …
}
```

### Built-in commands

#### ▶︎ Git

```kotlin
shellRun {
  git.init()
  git.status()
  git.commit("Commit message")
  git.commitAllChanges("Commit message")
  git.push("origin", "master")
  git.pull()
  git.checkout("release")
  git.clone("https://github.com/lordcodes/turtle.git")
  git.addTag("v1.1", "Release v1.1")
  git.pushTag("v1.1")
  git.currentBranch()
}
```

#### ▶︎ Files

```kotlin
shellRun {
  files.openFile("script.kts")
  files.openApplication("Mail")
  files.createSymlink("target", "link")
  files.readSymlink("link")
}
```

#### ▶︎ More

Extra commands can easily be added by either calling `command` or by extending `ShellScript`. If you have created a command that you think should be built in, please feel free to [open a PR](https://github.com/lordcodes/turtle/pull/new/master).

## Contributing or Help

If you notice any bugs or have a new feature to suggest, the please feel free to [open a PR](https://github.com/lordcodes/turtle/pull/new/master) or [open an issue](https://github.com/lordcodes/turtle/issues/new). Please make sure to discuss any big changes before putting the effort into creating the PR.

To reach out please contact [@lordcodes on Twitter](https://twitter.com/lordcodes)
