package com.lordcodes.turtle

/**
 * A shell command executable failed to run due to not being found, it is likely it needs to be installed.
 */
class ShellExecutableNotFoundException(
    executable: Executable,
    cause: Throwable?,
) : RuntimeException("Command ${executable.name} not found. See ${executable.helpUrl}.", cause)
