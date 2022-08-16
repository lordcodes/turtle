@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertIterableEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.net.URL
import kotlin.random.Random

class CommandTest {

    @Test
    fun `Command works with Ints, Booleans, File, etc`() {
        val dir = File(".")
        val command = Command(
            "command",
            "--force", true,
            "--directory", dir,
            "--max-length", 1,
            "--url", URL("http://example.com"),
        )
        val expected: List<String> = listOf(
            "command",
            "--force", "true", "--directory", ".",
            "--max-length", "1", "--url", "http://example.com",
        )
        assertIterableEquals(expected, command.list)
    }

    @Test
    fun `command support short and long options`() {
        val dir = File(".")
        val command = Command(
            "command",
            LongOption("--force", true),
            LongOption("--directory", dir),
            ShortOption('f'),
            ShortOption('o', "output.txt"),
        )
        val expected: List<String> = listOf(
            "command",
            "--force=true",
            "--directory=.",
            "-f",
            "-o=output.txt",
        )
        assertIterableEquals(expected, command.list)
    }

    @Test
    fun `Test for invalid arguments`() {
        val invalidArgument = Random(42)
        val e = assertThrows<IllegalArgumentException> {
            Command("command", invalidArgument)
        }
        assertEquals("Command received invalid arguments: [$invalidArgument]", e.message)
    }
}
