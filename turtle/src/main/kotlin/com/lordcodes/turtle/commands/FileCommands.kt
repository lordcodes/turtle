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
        longOptions: LsOptions.() -> List<LongOption> = { emptyList() }
    ): Command = CommandOptions.generateCommand(
        executable = "ls",
        argsBeforeOptions = args,
        longArgs = LsOptions.longOptions(),
        argsAfterOptions = files,
    )
}
