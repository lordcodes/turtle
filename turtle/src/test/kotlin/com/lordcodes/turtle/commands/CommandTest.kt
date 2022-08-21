@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.lordcodes.turtle.commands

import com.lordcodes.turtle.specs.Command
import com.lordcodes.turtle.specs.CommandOption
import com.lordcodes.turtle.specs.command
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.random.Random

class CommandTest {
    val executable = "executable"

    @Test
    fun `the command builder works with Ints, Booleans, File, Pair, Triple, List, HasCommandArguments`() {
        val dir = File("src")
        val command = command(
            executable,
            listOf(
                "-a",
                Pair("b", "c"),
                Triple("d", "e", "f"),
                listOf("g", "h"),
                'i',
                true,
                1,
                1.0,
                null,
                dir,
                URL("http://example.com"),
                URI("http://example.com"),
                CommandOption("--url", URL("http://example.com")),
                CommandOption("--force", true),
                CommandOption("--directory", dir),
                CommandOption("hola", null)
            )
        )
        val expected = Command(
            executable,
            listOf(
                "-a", "b", "c", "d", "e", "f", "g", "h", "i",
                "true", "1", "1.0",
                "src",
                "http://example.com",
                "http://example.com",
                "--url", "http://example.com",
                "--force", "true",
                "--directory", "src",
                "hola",
            )
        )
        assertEquals(expected, command)
    }

    @Test
    fun `Test for invalid arguments`() {
        val e = assertThrows<IllegalArgumentException> {
            command(executable, listOf("invalid", Random(42), ProcessBuilder()))
        }
        assertEquals(
            """
            Command received invalid arguments:
            #1 has type XorWowRandom
            #2 has type ProcessBuilder
            """.trimIndent(),
            e.message
        )
    }

    @Test
    fun `quoting arguments`() {
        val command = Command(
            executable,
            listOf("ab", "a b", "a\"b", "a\'b", "'ab'", "\"ab\"", "", "''", "\"''\"")
        )
        val quotedCommand = """
            $executable ab 'a b' 'a"b' 'a'b' ab ab '' '' ''
        """.trim()
        assertEquals(quotedCommand, command.toString())
    }
}
