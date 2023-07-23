package com.lordcodes.turtle

import java.io.File

/**
 * Run a block of shell commands within the provided function. Commands are created using [ShellScript].
 *
 * @param [workingDirectory] The location to run commands from. It can be changed between commands within the
 * [script] function. By default, the current working directory will be used.
 * @param [dryRun] Use dry-run mode which prints executed commands instead of launching processes.
 * @param [script] A function that runs a series of shell commands.
 *
 * @return [String] The output of the [script] function. If the function just runs a series of shell commands, it
 * will be the output of running the final command. Using properties within the function, it could return the output
 * of running one of the other commands instead.
 *
 * @throws [ShellFailedException] There was an issue running one of the commands.
 * @throws [ShellRunException] Running one of the commands produced error output.
 */
fun shellRun(
    workingDirectory: File? = null,
    dryRun: Boolean = false,
    script: ShellScript.() -> String,
): String = ShellScript(workingDirectory, dryRun = dryRun).script()

/**
 * Run a shell [command] with the specified [arguments]. Specify the [workingDirectory], or if unspecified the
 * current working directory will be used. Use [dryRun] to print the executed commands instead of actually executing
 * them and launching processes. The output will be provided as a [String].
 *
 * @throws [ShellFailedException] There was an issue running the command.
 * @throws [ShellRunException] Running the command produced error output.
 */
fun shellRun(
    command: String,
    arguments: List<String> = listOf(),
    workingDirectory: File? = null,
    dryRun: Boolean = false,
): String = shellRun(workingDirectory, dryRun = dryRun) { command(command, arguments) }
