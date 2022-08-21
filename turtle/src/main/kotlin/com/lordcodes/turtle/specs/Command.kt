@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.lordcodes.turtle.specs

import java.io.File
import java.net.URI
import java.net.URL

/** In memory representation of a CLI command with its [executable] and [arguments] **/
data class Command(
    val executable: String,
    val arguments: List<String> = emptyList(),
) {
    override fun toString(): String {
        val quotedArguments = arguments.joinToString(" ") { arg -> quoteCommandArgument(arg) }
        return "$executable $quotedArguments"
    }
}

/** Any class you own that can be converted to command-line arguments **/
interface HasCommandArguments {
    val args: List<Any?>
}

abstract class HasSingleCommandArgument(
    arg: String
) : HasCommandArguments {
    override val args = listOf(arg)
}

/** Type-unsafe Command builder that enables building type-safe functions on top of it **/
fun command(executable: String, typeUnsafeArgs: List<Any?>): Command {
    val args: List<Any?> = typeUnsafeArgs.toList().flattenArguments().flattenArguments().flattenArguments()

    val goodArguments = mutableListOf<String>()
    val invalidArguments = mutableListOf<String>()
    for ((index, arg) in args.withIndex()) {
        when {
            arg == null -> {} // ignore
            arg is String || arg is Boolean || arg is Number || arg is Char ->
                goodArguments += arg.toString()
            arg is URL || arg is URI ->
                goodArguments += arg.toString()

            arg is File -> goodArguments += arg.path
            else -> invalidArguments += "#$index has type ${arg::class.simpleName}"
        }
    }
    require(invalidArguments.isEmpty()) {
        "Command received invalid arguments:\n${invalidArguments.joinToString("\n")}"
    }
    return Command(executable, goodArguments)
}

private fun List<Any?>.flattenArguments(): List<Any?> =
    flatMap { arg ->
        when (arg) {
            null -> emptyList()
            is List<*> -> arg
            is Pair<*, *> -> arg.toList()
            is Triple<*, *, *> -> arg.toList()
            is HasCommandArguments -> arg.args
            else -> listOf(arg)
        }
    }

private fun quoteCommandArgument(arg: String): String {
    val doublequote = "\""
    val simplequote = "\'"
    val slash = "\\"
    val unquoted = arg
        .removePrefix(simplequote).removeSuffix(simplequote)
        .removePrefix(doublequote).removeSuffix(doublequote)
        .removePrefix(simplequote).removeSuffix(simplequote)

    val unescaped = unquoted
        .replace("$slash$simplequote", simplequote)
        .replace("$slash$doublequote", doublequote)
    val needsQuote = unquoted.any { it in " '\"" } || unquoted.isBlank()

    return when {
        needsQuote -> "'$unescaped'"
        else -> unquoted
    }
}
