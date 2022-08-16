package com.lordcodes.turtle.commands

abstract class CommandSpec {
    abstract val documentationUrl: String
    abstract val shortOptions: String
    val longOptionsMap: Map<String, LongOption> =
        emptyMap<String, LongOption>().withDefault { key -> LongOption("--$key", null) }

    companion object {
        fun generateCommand(
            executable: String,
            argsBeforeOptions: List<Any> = emptyList(),
            shortOptions: List<Char> = emptyList(),
            longOptions: List<LongOption> = emptyList(),
            argsAfterOptions : List<Any> = emptyList(),
        ): Command {
            val shortArgs = shortOptions.map { ShortOption(it, null) }

            val shortWarnings = shortArgs.map { it.key }.filter { it !in LsCommandSpec.shortOptions }
            val longWarnings = longOptions.map { it.key }.filter { it !in LsCommandSpec.longOptionsMap.keys }
            if (shortWarnings.isNotEmpty()) println("w: $executable() called with unknown short arguments: $shortWarnings")
            if (longWarnings.isNotEmpty()) println("w: $executable() called with unknown long arguments: $longWarnings")

            return Command(
                executable,
                *argsBeforeOptions.toTypedArray(),
                *shortArgs.toTypedArray(),
                *longOptions.toTypedArray(),
                *argsAfterOptions.toTypedArray()
            )
        }
    }
}
