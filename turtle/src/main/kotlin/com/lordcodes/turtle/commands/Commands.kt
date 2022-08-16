package com.lordcodes.turtle.commands

import java.io.File

/**
 * Typesafe file commands
 */
object Commands {

    /**
     * Typesafe ls command
     */
    fun ls(
        files: List<File> = listOf(File(".")),
        args: List<Any> = emptyList(),
        longOptions: LsSpec.() -> List<LongOption> = { emptyList() }
    ): Command = CommandSpec.generateCommand(
        executable = "ls",
        argsBeforeOptions = args,
        longArgs = LsSpec.longOptions(),
        argsAfterOptions = files,
    )
}
