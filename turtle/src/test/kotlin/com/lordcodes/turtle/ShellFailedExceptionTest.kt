package com.lordcodes.turtle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ShellFailedExceptionTest {
    @Test
    fun message() {
        val message = ShellFailedException(RuntimeException()).message

        assertThat(message).isEqualTo("Running shell command failed")
    }
}
