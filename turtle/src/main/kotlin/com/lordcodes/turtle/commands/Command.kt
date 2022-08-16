@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

import java.io.File
import java.net.URI
import java.net.URL

data class Command(
    val list: List<String>
) {
    val executable = list.firstOrNull()?.takeIf { it.isNotBlank() }
        ?: error("Command has no executable")

    val args: List<String> = list.drop(1)

    override fun toString() = list.joinToString(
        separator = " ",
        transform = { arg -> quoteCommandArgument(arg) }
    )
}

fun createCommand(
    executable: String,
    argsBeforeOptions: List<Any> = emptyList(),
    longArgs: List<LongOption> = emptyList(),
    argsAfterOptions: List<Any> = emptyList(),
): Command {
    val args = listOf(executable) + argsBeforeOptions + longArgs + argsAfterOptions
    return command(*args.toTypedArray())
}

fun command(vararg args: Any?): Command {
    require(args.firstOrNull() is String) { "command() expect at least a first String argument for the executable\nGot: $args" }
    val mappedList = args.map { it.toArgument() }
    val errors = mappedList.mapNotNull { it.exceptionOrNull() }
    require(errors.isEmpty()) { "Command received invalid arguments: ${errors.map { it.message }}" }
    val goodArguments = mappedList.mapNotNull { it.getOrNull() }
    return Command(goodArguments)
}

interface HasCommandArgument {
    val arg: String
}

data class LongOption(val key: String, val value: Any? = null) : HasCommandArgument {
    fun withValue(value: Any): LongOption =
        copy(value = value)

    private val result = value.toArgument()

    override val arg: String = when {
        result.isFailure -> error("Invalid argument $this")
        result == Result.success(null) -> key
        else -> "$key=${result.getOrNull()!!}"
    }
}

internal fun Any?.toArgument(): Result<String?> = when {
    this == null -> Result.success(null)
    this is HasCommandArgument -> Result.success(arg)
    this is String -> Result.success(this)
    this is File -> Result.success(path)
    this is Boolean || this is Int -> Result.success(toString())
    this is URI || this is URL -> Result.success(toString())
    else -> Result.failure(IllegalArgumentException(this::class.simpleName))
}

// TODO: tests
internal fun quoteCommandArgument(arg: String) = when {
    arg.contains("'") -> arg.replace("'", "\\'").let { "'$it'" }
    arg.isBlank() || arg.contains(" ") || arg.contains("\"") -> "'$arg'"
    else -> arg
}
