package com.lordcodes.turtle

import java.net.URL

/** First argument of [Command] **/
data class Executable(
    /** Executable name **/
    val name: String,
    /** Forces you to explain how to install your dependencies instead of letting it as an exercise to the user */
    val howToInstall: URL
) {
    /** `ls + Args("-l", "-a)` is `Command(ls, Args("-l", "-a)` **/
    operator fun plus(args: Args): Command = Command(this, args)

    /** `ls + withArg` is `Command(ls, Args(withArg)` **/
    operator fun plus(withArg: WithArg): Command = Command(this, Args(withArg))

    /** `ls + withArgs` is `Command(ls, Args(withArgs)` **/
    operator fun plus(withArgs: Iterable<WithArg>): Command = Command(this, Args(withArgs))
}
