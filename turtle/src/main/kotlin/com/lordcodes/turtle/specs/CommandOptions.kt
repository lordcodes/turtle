@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.specs

/** Extend CommandOptions to give type-safe access to --valid-options **/
abstract class CommandOptions {
    val optionsMap: Map<String, CommandOption> =
        emptyMap<String, CommandOption>().withDefault { key -> CommandOption("--$key", null) }
}

/** Represent a command line option, with or without a value **/
data class CommandOption(val key: String, val value: Any? = null) : HasCommandArguments {
    /** --<key> <value> **/
    fun withValue(value: Any): CommandOption =
        copy(value = value)

    /** --<key>=<value> **/
    fun withEquals(value: Any): CommandOption =
        copy(key = "$key=$value", null)

    override val args = listOf(key, value)
}

/** No options **/
val NoOptions: CommandOptions.() -> List<CommandOption> = { emptyList() }
