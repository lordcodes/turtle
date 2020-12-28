package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ShellFailedExceptionTest {
    @Test
    fun message() {
        val message = ShellFailedException(RuntimeException()).message

        assertThat(message).isEqualTo("Running shell command failed")
    }
}
