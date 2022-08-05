package com.lordcodes.turtle

import kotlin.test.Test
import kotlin.test.assertTrue

internal class ShellLocationTest {
    @Test
    fun home() {
        assertTrue(ShellLocation.HOME.absolutePath.isNotEmpty())
    }

    @Test
    fun currentWorking() {
        assertTrue(ShellLocation.CURRENT_WORKING.absolutePath.isNotEmpty())
    }
}
