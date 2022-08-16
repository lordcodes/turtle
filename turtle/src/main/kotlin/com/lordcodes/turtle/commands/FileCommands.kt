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
        longOptions: LsCommandSpec.() -> List<LongOption> = { emptyList() }
    ): Command = CommandSpec.generateCommand(
        executable = "ls",
        argsBeforeOptions = args,
        longArgs = LsCommandSpec.longOptions(),
        argsAfterOptions = files,
    )
}
