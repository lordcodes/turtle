package com.lordcodes.turtle

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ShellRunExceptionTest {
    @Test
    fun message() {
        val message = ShellRunException(1, "unexpected input").message

        assertEquals(message, "Running shell command failed with code 1 and message: unexpected input")
    }
}
