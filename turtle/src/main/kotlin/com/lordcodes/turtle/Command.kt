package com.lordcodes.turtle

/**
 * A shell command, made up of its [executable] (e.g. 'cd') and [arguments].
 *
 * @property [executable] The command executable, e.g. 'cd'.
 * @property [arguments] The arguments to be passed to the executable.
 */
data class Command(
    val executable: Executable,
    val arguments: Arguments = Arguments(emptyList()),
) {
    /**
     * Returns a [Command] copy of this command with the provided [arguments] added.
     *
     * ```
     * command + Arguments("-l", "-a")
     * ```
     *
     * @return [Command] A command copy with the provided arguments added.
     *
     * @param [arguments] The arguments to add to this command.
     */
    operator fun plus(arguments: Arguments): Command = copy(arguments = this.arguments + arguments)

    /**
     * Returns a [Command] copy of this command with the provided [withArguments] added.
     *
     * ```
     * command + withArguments
     * ```
     *
     * @return [Command] A command copy with the provided arguments added.
     *
     * @param [withArguments] The arguments to add to this command.
     */
    operator fun plus(withArguments: Iterable<WithArgument>): Command = copy(arguments = arguments + withArguments)

    /**
     * Returns a [Command] copy of this command with the provided [withArgument] added.
     *
     * ```
     * command + withArgument
     * ```
     *
     * @return [Command] A command copy with the provided argument added.
     *
     * @param [withArgument] The argument to add to this command.
     */
    operator fun plus(withArgument: WithArgument): Command = copy(arguments = arguments + withArgument)

    /**
     * Returns a [Command] copy of this command with the provided [arguments] removed.
     *
     * ```
     * command - Arguments("-l", "-a")
     * ```
     *
     * @return [Command] A command copy with the provided arguments removed.
     *
     * @param [arguments] The arguments to remove from this command.
     */
    operator fun minus(arguments: Arguments): Command = copy(arguments = this.arguments - arguments)

    /**
     * Returns a [Command] copy of this command with the provided [withArguments] removed.
     *
     * ```
     * command - withArguments
     * ```
     *
     * @return [Command] A command copy with the provided arguments removed.
     *
     * @param [withArguments] The arguments to remove from this command.
     */
    operator fun minus(withArguments: Iterable<WithArgument>): Command =
        copy(arguments = arguments - withArguments.toSet())

    /**
     * Returns a [Command] copy of this command with the provided [withArgument] removed.
     *
     * ```
     * command - withArgument
     * ```
     *
     * @return [Command] A command copy with the provided argument removed.
     *
     * @param [withArgument] The argument to remove from this command.
     */
    operator fun minus(withArgument: WithArgument): Command = copy(arguments = arguments - withArgument)

    /**
     * Returns whether this command's arguments contain the provided [arguments].
     *
     * ```
     * Arguments("-l", "-a") in command
     * ```
     *
     * @return [Boolean] Whether the command's arguments contain the provided arguments.
     *
     * @param [arguments] The arguments to check for.
     */
    operator fun contains(arguments: Arguments): Boolean = this.arguments.containsAll(arguments)

    /**
     * Returns whether this command's [arguments] contain the provided [withArguments].
     *
     * ```
     * withArguments in command
     * ```
     *
     * @return [Boolean] Whether the command's arguments contain the provided arguments.
     *
     * @param [withArguments] The arguments to check for.
     */
    operator fun contains(withArguments: Iterable<WithArgument>) = Arguments(withArguments) in this

    /**
     * Returns whether this command's [arguments] contain the provided [argument].
     *
     * ```
     * "--verbose" in command
     * ```
     *
     * @return [Boolean] Whether the command's arguments contain the provided argument.
     *
     * @param [argument] The argument to check for.
     */
    operator fun contains(argument: String) = Arguments(listOf(argument)) in this

    /**
     * Returns whether this command's [arguments] contain the provided [withArgument].
     *
     * ```
     * withArgument in command
     * ```
     *
     * @return [Boolean] Whether the command's arguments contain the provided argument.
     *
     * @param [withArgument] The argument to check for.
     */
    operator fun contains(withArgument: WithArgument) = Arguments(withArgument) in this

    /**
     * Command with its [executable] and [arguments] formatted similarly as a shell command-line, as a [String].
     *
     * @return [String] The command formatted as a string.
     */
    override fun toString(): String =
        "${executable.name} " + arguments.joinToString(separator = " ", transform = ::quoteArgumentIfNecessary)

    private fun quoteArgumentIfNecessary(argument: String): String {
        val unquoted = argument
            .trim('\'')
            .trim('\"')
            .trim('\'')
        val unescaped = unquoted
            .replace("\\\'", "\'")
            .replace("\\\"", "\"")
        return if (unquoted.isBlank() || unquoted.any { it in " '\"" }) {
            "'$unescaped'"
        } else {
            unescaped
        }
    }

    /**
     * Run the command [executable] with [arguments], receiving the output as a [String].
     *
     * @throws [ShellExecutableNotFoundException] The command executable wasn't found.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     *
     * @returns [String] Command output.
     */
    fun executeOrThrow(shellScript: ShellScript = ShellScript()): String = try {
        shellScript.command(executable.name, arguments)
    } catch (ex: ShellCommandNotFoundException) {
        throw ShellExecutableNotFoundException(executable, ex)
    }

    /**
     * Run the command [executable] with [arguments], receiving the output as a [String] and handling errors via the
     * [onError] lambda.
     *
     * @param [shellScript] The [ShellScript] to use for execution.
     * @param [onError] Handle errors that occur when running the command.
     *
     * @returns [String] Command output or output of [onError].
     */
    fun executeOrElse(shellScript: ShellScript = ShellScript(), onError: (Throwable) -> String): String = try {
        shellScript.command(executable.name, arguments)
    } catch (ex: ShellCommandNotFoundException) {
        onError(ShellExecutableNotFoundException(executable, ex.cause))
    } catch (ex: ShellFailedException) {
        onError(ex)
    } catch (ex: ShellRunException) {
        onError(ex)
    }
}
