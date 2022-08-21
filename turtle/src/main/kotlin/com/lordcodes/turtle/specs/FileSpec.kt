package com.lordcodes.turtle.specs

import java.io.File

/**
 * Typesafe file commands
 */
object FileSpec {

    /**
     * Typesafe ls command
     */
    fun ls(
        files: List<File> = listOf(File(".")),
        args: List<Any> = emptyList(),
        commandOptions: LsOptions.() -> List<CommandOption> = { emptyList() }
    ): Command = command("ls", args + LsOptions.commandOptions() + files)
}
