package com.lordcodes.turtle

/**
 * A shell command failed to run due to not being found, it is likely it needs to be installed.
 */
class ShellCommandNotFoundException(
    command: String,
    cause: Throwable
) : RuntimeException("Command $command not found", cause)
