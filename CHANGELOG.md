# Changelog

## Un-released changes (master)

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
