package com.lordcodes.turtle

import java.net.URL

/**
 * Command executable, e.g. 'cd', with its [name] and a [helpUrl] to provide the user help if the executable isn't
 * found on the system.
 *
 * @property [name] The command executable name, e.g. 'cd'.
 * @property [helpUrl] A url that gives help for the executable if it isn't found on the system.
 */
data class Executable(
    val name: String,
    val helpUrl: URL? = null,
) {
    /**
     * Creates a [Command] using this executable with the provided [arguments].
     *
     * ```
     * ls + Arguments("-l", "-a")
     * ```
     *
     * @return [Command] The created command.
     *
     * @param [arguments] The arguments to pass to this executable.
     */
    operator fun plus(arguments: Arguments): Command = Command(this, arguments)

    /**
     * Creates a [Command] using this executable with the provided [withArguments].
     *
     * ```
     * ls + withArgs
     * ```
     *
     * @return [Command] The created command.
     *
     * @param [withArguments] The arguments to pass to this executable.
     */
    operator fun plus(withArguments: Iterable<WithArgument>): Command =
        Command(this, Arguments(withArguments))

    /**
     * Creates a [Command] using this executable with the provided [withArgument].
     *
     * ```
     * ls + withArg
     * ```
     *
     * @return [Command] The created command.
     *
     * @param [withArgument] The argument to pass to this executable.
     */
    operator fun plus(withArgument: WithArgument): Command = Command(this, Arguments(withArgument))
}
