package com.lordcodes.turtle.internal

import com.lordcodes.turtle.Args
import com.lordcodes.turtle.WithArg
import java.io.File
import java.net.URI
import java.net.URL

internal fun buildArgsFromAnyType(typeUnsafeArgs: List<Any?>): Args {
    var args = typeUnsafeArgs
    while (true) {
        val flattened = args.flattenArguments()
        if (args == flattened) break
        args = flattened
    }

    val argToStringOrError = args
        .filterNotNull()
        .map { arg -> arg.toArgOrError() }

    val (good, bad) = argToStringOrError.partition { it.second == null }

    val goodArguments = good.map { it.first }
    val invalidClasses = bad.map { it.second }.distinct()
    require(invalidClasses.isEmpty()) {
        "Classes couldn't be converted to Args: $invalidClasses"
    }
    return Args(goodArguments)
}

/**
Convert common Java types to a string argument

The result type is a Poor man's Either. Right is error if not null. Left is success.
*/
private fun Any.toArgOrError(): Pair<String, String?> {
    val error = Pair("", this::class.simpleName)
    fun String.left() = Pair(this, null)

    return when {
        this is String || this is Boolean || this is Number || this is Char -> toString().left()
        this is WithArg -> arg.left()
        this is URL || this is URI -> toString().left()
        this is File -> this.path.left()
        else -> error
    }
}

/***
 * Some parameters to Args() may correspond to multiple command-line arguments
 */
private fun List<Any?>.flattenArguments(): List<Any?> =
    flatMap { arg ->
        when (arg) {
            null -> emptyList()
            is Iterable<*> -> arg.toList()
            is Map<*, *> -> arg.entries.flatMap { listOf(it.key, it.value) }
            is Pair<*, *> -> arg.toList()
            is Triple<*, *, *> -> arg.toList()
            else -> listOf(arg)
        }
    }

internal fun quoteCommandArgument(arg: String): String {
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
