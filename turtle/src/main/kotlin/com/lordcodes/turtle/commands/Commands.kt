package com.lordcodes.turtle.commands

open class Commands {
    fun buildCommand(
        executable: String,
        argsBeforeOptions: List<Any> = emptyList(),
        longArgs: List<LongOption> = emptyList(),
        argsAfterOptions: List<Any> = emptyList(),
    ): Command {
        val args = listOf(executable) + argsBeforeOptions + longArgs + argsAfterOptions
        return command(*args.toTypedArray())
    }

}
