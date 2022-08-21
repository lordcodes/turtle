@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

abstract class CommandOptions {
    val optionsMap: Map<String, CommandOption> =
        emptyMap<String, CommandOption>().withDefault { key -> CommandOption("--$key", null) }
}

val NoOptions: CommandOptions.() -> List<CommandOption> = { emptyList() }

data class CommandOption(val key: String, val value: Any? = null) : HasCommandArguments {
    fun withValue(value: Any): CommandOption =
        copy(value = value)

    override val args = listOf(key, value)
}
