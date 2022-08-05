package com.lordcodes.turtle

import kotlin.test.Test
import kotlin.test.assertEquals

internal class ShellFailedExceptionTest {
    @Test
    fun message() {
        val message = ShellFailedException(RuntimeException()).message

        assertEquals(message, "Running shell command failed")
    }
}
