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
    val executable = "executable"

    @Test
    fun `Command works with Ints, Booleans, File, etc`() {
        val dir = File(".")
        val command = command(
            executable,
            "--force", true,
            "--directory", dir,
            "--max-length", 1,
            "--url", URL("http://example.com"),
        )
        val expected = Command(
            executable, listOf(
            "--force", "true", "--directory", ".",
            "--max-length", "1", "--url", "http://example.com",
        ))
        assertEquals(expected, command)
    }

    @Test
    fun `command support long options`() {
        val dir = File(".")
        val command = command(
            executable,
            LongOption("--force", true),
            LongOption("--directory", dir),
            "-f",
            "-o=output.txt",
        )
        val expected = Command(
            executable, listOf(
            "--force=true",
            "--directory=.",
            "-f",
            "-o=output.txt",
        ))
        assertEquals(expected, command)
    }

    @Test
    fun `Test for invalid arguments`() {
        val invalidArgument = Random(42)
        val e = assertThrows<IllegalArgumentException> {
            command(executable, invalidArgument)
        }
        assertEquals("Command received invalid arguments: [XorWowRandom]", e.message)
    }

    @Test
    fun `quoting arguments`() {
        val command = Command(executable,
            listOf("ab", "a b", "a\"b", "a\'b", "'ab'", "\"ab\"", "", "''", "\"''\""))
        val quotedCommand = """
            $executable ab 'a b' 'a"b' 'a'b' ab ab '' '' ''
        """.trim()
        assertEquals(quotedCommand, command.toString())
    }
}
