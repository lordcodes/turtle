package com.lordcodes.turtle

class ShellCommandNotFoundException(
    command: String,
    cause: Throwable
) : RuntimeException("Command $command not found", cause)
