package com.lordcodes.turtle

import java.io.File
import java.net.URI
import java.net.URL

/**
 * A list of command-line [arguments]. Implements List<String> by delegating to the provided arguments list.
 *
 * @property [arguments] The list of arguments.
 */
data class Arguments(
    val arguments: List<String>,
) : List<String> by arguments {
    /**
     * Returns an [Arguments] copy of these arguments with the provided [arguments] added.
     *
     * ```
     * Arguments("src", "dest") + Arguments("--verbose")
     * ```
     *
     * @return [Arguments] An arguments copy with the provided arguments added.
     *
     * @param [arguments] The arguments to add to these arguments.
     */
    operator fun plus(arguments: Arguments): Arguments = Arguments(this.arguments + arguments)

    /**
     * Returns an [Arguments] copy of these arguments with the provided [arguments] added.
     *
     * ```
     * Arguments("src", "dest") + withArguments
     * ```
     *
     * @return [Arguments] An arguments copy with the provided arguments added.
     *
     * @param [withArguments] The arguments to add to these arguments.
     */
    operator fun plus(withArguments: Iterable<WithArgument>): Arguments =
        Arguments(arguments + withArguments.map { it.argument })

    /**
     * Returns an [Arguments] copy of these arguments with the provided [withArgument] added.
     *
     * ```
     * Arguments("src", "dest") + withArgument
     * ```
     *
     * @return [Arguments] An arguments copy with the provided argument added.
     *
     * @param [withArgument] The argument to add to these arguments.
     */
    operator fun plus(withArgument: WithArgument): Arguments = Arguments(arguments + withArgument.argument)

    /**
     * Returns an [Arguments] copy of these arguments with the provided [arguments] removed.
     *
     * ```
     * Arguments("src", "dest") - Arguments("dest")
     * ```
     *
     * @return [Arguments] An arguments copy with the provided arguments removed.
     *
     * @param [arguments] The arguments to remove from these arguments.
     */
    operator fun minus(arguments: Arguments): Arguments = Arguments(this.arguments - arguments.toSet())

    /**
     * Returns an [Arguments] copy of these arguments with the provided [withArguments] removed.
     *
     * ```
     * Arguments("src", "dest") - withArguments
     * ```
     *
     * @return [Arguments] An arguments copy with the provided arguments removed.
     *
     * @param [withArguments] The arguments to remove from these arguments.
     */
    operator fun minus(withArguments: Iterable<WithArgument>): Arguments =
        Arguments(arguments - withArguments.map { it.argument }.toSet())

    /**
     * Returns an [Arguments] copy of these arguments with the provided [withArgument] removed.
     *
     * ```
     * Arguments("src", "dest") - withArgument
     * ```
     *
     * @return [Arguments] An arguments copy with the provided argument removed.
     *
     * @param [withArgument] The argument to remove from these arguments.
     */
    operator fun minus(withArgument: WithArgument): Arguments = Arguments(arguments - withArgument.argument)
}

/**
 * Create [Arguments] from any number of supported types, converting them to strings.
 *
 * - Boolean
 * - Char
 * - File
 * - Number
 * - String
 * - URI
 * - URL
 * - WithArgument
 *
 * or collections of the supported types.
 *
 * - Iterable
 * - List
 * - Map
 * - Pair
 * - Triple
 */
fun Arguments(vararg arguments: Any?): Arguments {
    val flattenedArguments = arguments.toList().recursivelyFlatten()
    val invalidArgumentClasses = flattenedArguments
        .mapNotNull { argument ->
            if (argument.asArgumentOrNull() == null) {
                argument::class.simpleName
            } else {
                null
            }
        }
        .distinct()
    require(invalidArgumentClasses.isEmpty()) {
        "Classes couldn't be converted to Arguments: $invalidArgumentClasses"
    }
    val validArguments = flattenedArguments.mapNotNull { it.asArgumentOrNull() }
    return Arguments(validArguments)
}

private fun Any.asArgumentOrNull(): String? = when (this) {
    is Boolean -> toString()
    is Char -> toString()
    is File -> path
    is Number -> toString()
    is String -> this
    is URI -> toString()
    is URL -> toString()
    is WithArgument -> argument
    else -> null
}

private fun Iterable<Any?>.recursivelyFlatten(): List<Any> {
    val result = mutableListOf<Any>()
    for (element in this) {
        when (element) {
            null -> {
                continue
            }

            is Iterable<*> -> {
                result.addAll(element.recursivelyFlatten())
            }

            is Map<*, *> -> {
                result.addAll(
                    element.entries
                        .flatMap { listOf(it.key, it.value) }
                        .recursivelyFlatten(),
                )
            }

            is Pair<*, *> -> {
                result.addAll(element.toList().recursivelyFlatten())
            }

            is Triple<*, *, *> -> {
                result.addAll(element.toList().recursivelyFlatten())
            }

            else -> {
                result.add(element)
            }
        }
    }
    return result
}
