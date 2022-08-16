package com.lordcodes.turtle.commands

open class Commands {
    fun buildCommand(
        executable: String,
        argsBeforeOptions: List<Any> = emptyList(),
        longArgs: List<LongOption> = emptyList(),
        argsAfterOptions: List<Any> = emptyList(),
    ): Command {

        val longWarnings = longArgs.map { it.key }.filter { it !in LsOptions.optionsMap.keys }
        if (longWarnings.isNotEmpty()) {
            println("w: $executable() called with unknown long arguments: $longWarnings")
        }

        val args = listOf(executable) + argsBeforeOptions + longArgs + argsAfterOptions
        return command(*args.toTypedArray())
    }

}
