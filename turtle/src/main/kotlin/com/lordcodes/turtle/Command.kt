package com.lordcodes.turtle

import com.lordcodes.turtle.internal.quoteCommandArgument

/** In memory representation of a CLI command with its [executable] and [Args] **/
data class Command(
    val executable: Executable,
    val args: Args = Args(emptyList()),
) {

    /** Command formatted like for a Bash command-line **/
    override fun toString(): String = "${executable.name} " +
        args.joinToString(separator = " ", transform = ::quoteCommandArgument)

    /** `command + args` is `Command(command.executable, command.args + args)` */
    operator fun plus(other: Args): Command =
        copy(args = args + other)

    /** `command + withArg` is `Command(command.executable, command.args + withArg)` */
    operator fun plus(other: WithArg): Command =
        copy(args = args + other)

    /** `command + withArgs` is `Command(command.executable, command.args + withArgs)` */
    operator fun plus(other: Iterable<WithArg>): Command =
        copy(args = args + other)

    /** `command - args` is shortcut for `Command(command.executable, command.args - args)` */
    operator fun minus(other: Args): Command =
        copy(args = args - other)

    /** `command - args` is shortcut for `Command(command.executable, command.args - args)` */
    operator fun minus(other: WithArg): Command =
        copy(args = Args(args - other.arg))

    /** `command + withArgs` is `Command(command.executable, command.args + withArgs)` */
    operator fun minus(other: Iterable<WithArg>): Command =
        copy(args = args - Args(other))

    /** `Args("-l", "-a") in command` */
    operator fun contains(other: Args) = args.containsAll(other.args)

    /** `"--verbose" in command` */
    operator fun contains(arg: String) = Args(arg) in this

    /** `withArg in command` */
    operator fun contains(arg: WithArg) = Args(arg) in this

    /** `withArgs in command` */
    operator fun contains(other: Iterable<WithArg>) = Args(other) in this

    /**
     * Run a shell command with the specified arguments, receiving the output as a String.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun executeOrThrow(
        shellScript: ShellScript = ShellScript(null),
    ): String = try {
        shellScript.command(executable.name, args)
    } catch (e: ShellFailedException) {
        check(e.couldRunProgram()) { "Command ${executable.name} not found. See ${executable.url}" }
        throw e
    }

    /**
     * Run a shell command with the specified arguments, receiving the output as a String.
     *
     * [onError] is called if there was an issue running the command
     */
    inline fun executeOrElse(
        shellScript: ShellScript = ShellScript(null),
        onError: (Exception) -> String
    ): String {
        return try {
            shellScript.command(executable.name, args)
        } catch (e: ShellFailedException) {
            check(e.couldRunProgram()) { "Command ${executable.name} not found. See ${executable.url}" }
            onError(e)
        } catch (e: ShellRunException) {
            onError(e)
        }
    }
}
