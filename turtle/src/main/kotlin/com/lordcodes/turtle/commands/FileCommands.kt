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
        shortOptions: List<Char> = emptyList(),
        longOptions: LsCommandSpec.() -> List<LongOption> = { emptyList() }
    ): Command = CommandSpec.generateCommand(
        executable = "ls",
        shortOptionChars = shortOptions,
        longArgs = LsCommandSpec.longOptions(),
        argsAfterOptions = files,
    )
}
