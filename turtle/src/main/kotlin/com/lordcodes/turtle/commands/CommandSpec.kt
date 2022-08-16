@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

abstract class CommandSpec {
    val longOptionsMap: Map<String, LongOption> =
        emptyMap<String, LongOption>().withDefault { key -> LongOption("--$key", null) }

    companion object {
        fun generateCommand(
            executable: String,
            argsBeforeOptions: List<Any> = emptyList(),
            longArgs: List<LongOption> = emptyList(),
            argsAfterOptions: List<Any> = emptyList(),
        ): Command {

            val longWarnings = longArgs.map { it.key }.filter { it !in LsSpec.longOptionsMap.keys }
            if (longWarnings.isNotEmpty()) {
                println("w: $executable() called with unknown long arguments: $longWarnings")
            }

            val args = listOf(executable) + argsBeforeOptions + longArgs + argsAfterOptions
            return command(*args.toTypedArray())
        }
    }
}
