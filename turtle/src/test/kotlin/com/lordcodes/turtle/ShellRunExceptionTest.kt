package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ShellRunExceptionTest {
    @Test
    fun message() {
        val message = ShellRunException(1, "unexpected input").message

        assertThat(message).isEqualTo("Running shell command failed with code 1 and message: unexpected input")
    }
}
