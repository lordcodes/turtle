package com.lordcodes.turtle.commands

import java.io.File

/**
 * Typesafe file commands
 */
object FileCommands {

    /**
     * Typesafe ls command
     */
    fun ls(
        files: List<File> = listOf(File(".")),
        args: List<Any> = emptyList(),
        commandOptions: LsOptions.() -> List<CommandOption> = { emptyList() }
    ): Command = createCommand(
        executable = "ls",
        argsBeforeOptions = args,
        longArgs = LsOptions.commandOptions(),
        argsAfterOptions = files
    )
}
