package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class ShellLocationTest {
    @Test
    fun home() {
        assertThat(ShellLocation.HOME.absolutePath).isNotEmpty()
    }

    @Test
    fun currentWorking() {
        assertThat(ShellLocation.CURRENT_WORKING.absolutePath).isNotEmpty()
    }
}
