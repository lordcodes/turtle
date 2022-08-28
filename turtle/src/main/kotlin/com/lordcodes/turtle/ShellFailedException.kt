package com.lordcodes.turtle

/**
 * A shell command failed to run. A [ShellFailedException] will be thrown in situations when the command couldn't
 * be started or it failed to run in some way. The issue may relate to the environment or with an invalid input.
 */
class ShellFailedException(cause: Throwable) : RuntimeException("Running shell command failed", cause) {

    /** wether the command failed because the program was not installed **/
    fun couldRunProgram(): Boolean =
        cause?.message?.contains("Cannot run program") != true
}
