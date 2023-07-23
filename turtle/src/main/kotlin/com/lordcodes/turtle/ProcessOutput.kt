package com.lordcodes.turtle

import java.io.InputStream

/**
 * Output of running a shell command.
 *
 * @property [exitCode] The exit code of the process. By convention, the value {@code 0} indicates normal termination.
 * @property [standardOutput] The standard output produced by running a command or series of commands.
 * @property [standardError] The error output produced by running a command or series of commands.
 */
data class ProcessOutput(
    val exitCode: Int,
    val standardOutput: InputStream,
    val standardError: InputStream,
)
