# Changelog

## Un-released changes (master)

## v0.10.0

* Stream output as a Sequence [PR #239](https://github.com/lordcodes/turtle/pull/239).
* Now using [kotlinter](https://github.com/jeremymailen/kotlinter-gradle) for formatting and linting Kotlin.
* Gradle 8.7.
* JDK 17 and Kotlin JVM Target 17.
* Dependency updates.

## v0.9.0

* Multi-platform open command. [PR #115](https://github.com/lordcodes/turtle/pull/119).
* Handle different platforms for File Commands. [PR #213](https://github.com/lordcodes/turtle/pull/213).
* Kotlin 1.9.20, latest Gradle, targeting Kotlin language version 11 and many dependency updates.

## v0.8.0

* Add which command. [PR #117](https://github.com/lordcodes/turtle/pull/117)
* Add dry run command. [PR #118](https://github.com/lordcodes/turtle/pull/118)
* Command and Arguments abstraction. [PR #127](https://github.com/lordcodes/turtle/pull/127)
* Many dependency updates.

## v0.7.0

* Make ShellScript constructor accessible, so that consumers can create their own instances, such as creating a wrapper class. [PR #99](https://github.com/lordcodes/turtle/pull/99)
* Many dependency updates.

## v0.6.0

* Specify a process callback for a single command or a default one to use for all commands. This can be used to access the underlying process to monitor it or customise behaviour such as implementing a timeout. [PR #66](https://github.com/lordcodes/turtle/pull/66).
* Access command output using the `InputStreams` rather than waiting for it to complete fully and accessing as a `String`. [PR #67](https://github.com/lordcodes/turtle/pull/67).
* Many dependency updates.

## v0.5.0

Fixed JDK version for consumers.

## v0.4.0

:rotating_light: IMPORTANT: Only usable by certain JDK versions, please use v0.5.0 or later.

Fixed the broken POM file from v0.3.0.

## v0.3.0

:rotating_light: IMPORTANT: Broken release, please use v0.5.0 or later.

* Maven Central publishing.
* Updated dependencies to latest.

## v0.2.0

Allow users to add extra Git commands to `GitCommands` via extension functions.

## v0.1.0

Initial release of turtle
