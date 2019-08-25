package com.lordcodes.turtle

import java.io.File

/**
 * Run a block of shell commands within the provided function. Commands are created using [ShellScript].
 *
 * @param [workingDirectory] The location to run commands from. It can be changed between commands within the
 * [script] function. By default, the current working directory will be used.
 * @param [script] A function that runs a series of shell commands.
 *
 * @return [String] The output of the [script] function. If the function just runs a series of shell commands, it
 * will be the output of running the final command. Using properties within the function, it could return the output
 * of running one of the other commands instead.
 *
 * @throws [ShellFailedException] There was an issue running one of the commands.
 * @throws [ShellRunException] Running one of the commands produced error output.
 */
fun shellRun(workingDirectory: File? = null, script: ShellScript.() -> String) =
    ShellScript(workingDirectory).script()

/**
 * Run a shell command with the specified arguments.
 *
 * @param [command] A command to run.
 * @param [arguments] The arguments to pass to the command.
 * @param [workingDirectory] The location to run the command from. By default, the current working directory will
 * be used.
 *
 * @return [String] The output of running the command.
 *
 * @throws [ShellFailedException] There was an issue running the command.
 * @throws [ShellRunException] Running the command produced error output.
 */
fun shellRun(command: String, arguments: List<String> = listOf(), workingDirectory: File? = null) =
    shellRun(workingDirectory) { command(command, arguments) }
