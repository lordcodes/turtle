package com.lordcodes.turtle.commands

import java.io.File

object FileCommands {

    fun ls(
        files: List<File> = listOf(File(".")),
        shortOptions: List<Char> = emptyList(),
        longOptions: LsCommandSpec.() -> List<LongOption> = { emptyList() }
    ): Command = CommandSpec.generateCommand(
        executable = "ls",
        shortOptions = shortOptions,
        longOptions = LsCommandSpec.longOptions(),
        argsAfterOptions = files,
    )
}

