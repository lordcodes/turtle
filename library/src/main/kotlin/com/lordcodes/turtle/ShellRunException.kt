package com.lordcodes.turtle

class ShellRunException(exitCode: Int, errorText: String) : RuntimeException(
    "Running shell command failed with code $exitCode and message: $errorText"
)
