package com.lordcodes.turtle

import com.lordcodes.turtle.internal.buildArgsFromAnyType

/** Convert any number of strings, ints, booleans, lists, pair, File, URI, URL, any supported types
 * to an [Args] of command-line arguments **/
fun Args(vararg args: Any?): Args =
    buildArgsFromAnyType(args.toList())

/** Represent a list of command-line arguments **/
data class Args(val args: List<String>) : List<String> by args {
    /** `Args("src", "dest") + Args("--verbose")` is `Args("src", "dest", "--verbose")` **/
    operator fun plus(other: Args): Args =
        Args(args + other)

    /** `Args("src", "--verbose") - Args("--verbose")` is `Args("src")` **/
    operator fun minus(other: Args): Args =
        Args(args - other.toSet())

    /** args + withArg **/
    operator fun plus(other: WithArg): Args =
        Args(args + other.arg)

    /** args + withArgs **/
    operator fun plus(others: Iterable<WithArg>): Args =
        Args(args + others.map { it.arg })

    /** args - withArg **/
    operator fun minus(other: WithArg): Args =
        Args(args - other.arg)

    /** args - withArgs **/
    operator fun minus(others: Iterable<WithArg>): Args =
        Args(args - others.map { it.arg }.toSet())
}

/** Define your own types that contains a CLI argument **/
interface WithArg {
    val arg: String
}
