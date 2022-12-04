<p align="center">
    <img src="art/logo.png" width="500" max-width="90%" alt="Turtle" />
</p>

<p align="center">
  <a href="https://kotlinlang.org/">
      <img src="https://img.shields.io/badge/100%25-kotlin-935dff.svg" alt="Pure Kotlin" />
  </a>
  <a href="https://github.com/lordcodes/turtle/releases/latest">
      <img src="https://img.shields.io/github/release/lordcodes/turtle.svg?style=flat" alt="Latest release" />
  </a>
  <a href="https://github.com/lordcodes/turtle/workflows/Gradle">
    <img src="https://github.com/lordcodes/turtle/workflows/Gradle/badge.svg" alt="Gradle build status" />
  </a>
  <a href="https://twitter.com/lordcodes">
    <img src="https://img.shields.io/badge/twitter-@lordcodes-00acee.svg?style=flat" alt="Twitter: @lordcodes" />
  </a>
</p>

---

Run shell commands from a Kotlin script or application with ease.

Turtle simplifies the process of running external commands and processes from your Kotlin (or Java) code. It comes bundled with a selection of built-in functions, such as opening MacOS applications and dealing with Git. Running shell commands easily is particularly useful from within Kotlin scripts, command line applications and Gradle tasks.

&nbsp;

<p align="center">
    <a href="#features">Features</a> • <a href="#install">Install</a> • <a href="#usage">Usage</a> • <a href="#contributing-or-help">Contributing</a>
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

## Install

Turtle is provided as a Gradle/Maven dependency.

* v0.5.0 onwards are available via Maven Central.
* v0.3.0 and v0.4.0 had issues, so please use v0.5.0 or later.
* Earlier releases were available via Bintray/JCenter.

#### ▶︎ Gradle Kotlin DSL

```gradle
dependencies {
  implementation("com.lordcodes.turtle:turtle:0.8.0")
}
```

#### ▶︎ Gradle Groovy DSL

```gradle
dependencies {
  implementation 'com.lordcodes.turtle:turtle:0.8.0'
}
```

## Usage

Note: Turtle is developed with MacOS in mind and so some commands or features may not exist or work as expected on Linux or Windows. There is an open issue to consider multiplatform support more carefully.

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

If you notice any bugs or have a new feature to suggest, please check out the [contributing guide](https://github.com/lordcodes/turtle/blob/master/CONTRIBUTING.md). If you want to make changes, please make sure to discuss anything big before putting in the effort of creating the PR.

To reach out, please contact [@lordcodes on Twitter](https://twitter.com/lordcodes).
