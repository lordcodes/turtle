package com.lordcodes.turtle

import java.net.URL

/** First argument of [Command] **/
open class Executable(
    /** Executable name **/
    val name: String,
    /** nudge you to link to a documentation website or an installation url */
    val url: URL
) {
    /** `ls + Args("-l", "-a)` is `Command(ls, Args("-l", "-a)` **/
    operator fun plus(args: Args): Command = Command(this, args)

    /** `ls + withArg` is `Command(ls, Args(withArg)` **/
    operator fun plus(withArg: WithArg): Command = Command(this, Args(withArg))

    /** `ls + withArgs` is `Command(ls, Args(withArgs)` **/
    operator fun plus(withArgs: Iterable<WithArg>): Command = Command(this, Args(withArgs))

    override fun toString() =
        "Executable($name) // see $url"

    override fun equals(other: Any?) =
        name == (other as? Executable)?.name

    override fun hashCode() = name.hashCode()
}
