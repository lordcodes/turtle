@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.lordcodes.turtle.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class LsCommandTest {

    @Test
    fun `ls without arguments`() {
        val expected = command("ls", ".")
        val actual = Commands.ls()
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with short arguments`() {
        val expected = command("ls", "-a", "-b", "-c", "/etc")
        val actual = Commands.ls(
            files = listOf(File("/etc")),
            args = listOf("-a", "-b", "-c"),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with long arguments`() {
        val expected = command("ls", "--all", "--colour", "--blocks=420", "/etc")
        val actual = Commands.ls(
            files = listOf(File("/etc")),
        ) {
            listOf(all, colour, blocks.withValue("420"))
        }
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with warnings`() {
        Commands.ls() {
            listOf(LongOption("--whatever"), LongOption("--invalid"))
        }
        /*
         * Print to console the following warnings:
         * w: ls() called with unknown long arguments: [--whatever, --invalid]
         */
    }
}
