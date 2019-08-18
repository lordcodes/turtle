package com.lordcodes.turtle

class ShellFailedException(cause: Throwable) : RuntimeException("Running shell command failed", cause)
