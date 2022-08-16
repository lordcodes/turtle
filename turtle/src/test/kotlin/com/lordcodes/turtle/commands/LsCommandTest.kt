@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class LsCommandTest {

    @Test
    fun `ls without arguments`() {
        val expected = Command(listOf("ls", "."))
        val actual = FileCommands.ls()
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with short arguments`() {
        val expected = Command(listOf("ls", "-a", "-b", "-c", "/etc"))
        val actual = FileCommands.ls(
            files = listOf(File("/etc")),
            shortOptions = listOf('a', 'b', 'c'),
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with long arguments`() {
        val expected = Command(listOf("ls", "--all", "--colour", "--blocks=420", "/etc"))
        val actual = FileCommands.ls(
            files = listOf(File("/etc")),
        ) {
            listOf(all, colour, blocks.withValue("420"))
        }
        assertEquals(expected, actual)
    }

    @Test
    fun `ls with warnings`() {
        FileCommands.ls(
            shortOptions = listOf('Y', '$'),
        ) {
            listOf(LongOption("--whatever"), LongOption("--invalid"))
        }
        /*
         * Print to console the following warnings:
         * w: ls() called with unknown short arguments: [Y, $]
         * w: ls() called with unknown long arguments: [--whatever, --invalid]
         */
    }
}
