package com.lordcodes.turtle

class ShellExecutableNotFoundException(
    executable: Executable,
    cause: Throwable?
) : RuntimeException("Command ${executable.name} not found. See ${executable.helpUrl}.", cause)
