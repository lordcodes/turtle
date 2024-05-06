package com.lordcodes.turtle

/**
 * A shell command completed with an error exit code and error output.
 *
 * @property [exitCode] The exit code of the process. By convention, the value {@code 0} indicates normal termination.
 * @property [errorText] The error output produced by running a command or series of commands.
 */
data class ShellRunException(
    val exitCode: Int,
    val errorText: String? = null,
) : RuntimeException(
    if (errorText == null) {
        "Running shell command failed with code $exitCode"
    } else {
        "Running shell command failed with code $exitCode and message: $errorText"
    },
)
