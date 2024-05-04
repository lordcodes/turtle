@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.lordcodes.turtle

import com.lordcodes.turtle.ArgumentsTest.LsFlag
import java.net.URL
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class CommandTest {
    private val builtinUnixCommands = URL(
        "https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/",
    )
    private val echo = Executable("echo", builtinUnixCommands)
    private val ls = Executable("ls", builtinUnixCommands)

    @Test
    fun `plus with Arguments`() {
        val expected = Command(ls, Arguments(listOf("-1", "-a", "-l")))

        val actual = Command(ls, Arguments("-1")) + Arguments("-a", "-l")

        assertEquals(expected, actual)
    }

    @Test
    fun `plus with multiple WithArgument`() {
        val withArguments: Iterable<WithArgument> = listOf(LsFlag.AllFiles, LsFlag.LongFormat)
        val expected = Command(ls, Arguments(listOf("-1", "-a", "-l")))

        val actual = Command(ls, Arguments("-1")) + withArguments

        assertEquals(expected, actual)
    }

    @Test
    fun `plus with single WithArgument`() {
        val expected = Command(ls, Arguments(listOf("-1", "-a")))

        val actual = Command(ls, Arguments("-1")) + LsFlag.AllFiles

        assertEquals(expected, actual)
    }

    @Test
    fun `minus with Arguments`() {
        val expected = Command(ls, Arguments(listOf("-c")))

        val actual = Command(ls, Arguments("-a", "-b", "-c")) - Arguments("-a", "-b", "-d")

        assertEquals(expected, actual)
    }

    @Test
    fun `minus with multiple WithArgument`() {
        val withArguments: Iterable<WithArgument> = listOf(LsFlag.AllFiles, LsFlag.LongFormat)
        val expected = Command(ls, Arguments(listOf("-1")))

        val actual = Command(ls, Arguments("-1", "-a", "-l")) - withArguments

        assertEquals(expected, actual)
    }

    @Test
    fun `minus with single WithArgument`() {
        val expected = Command(ls, Arguments(listOf("-1", "-l")))

        val actual = Command(ls, Arguments("-1", "-a", "-l")) - LsFlag.AllFiles

        assertEquals(expected, actual)
    }

    @Test
    fun contains() {
        val command = Command(ls, Arguments("-1", "-a", "-l"))
        val withArguments: Iterable<WithArgument> = listOf(LsFlag.AllFiles, LsFlag.LongFormat)

        assertEquals(true, Arguments("-a", "-l") in command)
        assertEquals(true, withArguments in command)
        assertEquals(true, LsFlag.AllFiles in command)
        assertEquals(false, LsFlag.OnlyDirectory in command)
    }

    @Test
    fun `toString without escaping or quotes`() {
        val command = Command(
            ls,
            Arguments("-a", "-l", "--color=when", "src", "build"),
        )

        assertEquals("ls -a -l --color=when src build", command.toString())
    }

    @Test
    fun `toString with escaping and quotes`() {
        val command = Command(
            ls,
            Arguments("ab", "a b", "a\"b", "a\'b", "'ab'", "\"ab\"", "", "''", "\"''\""),
        )
        val expected = """
            ls ab 'a b' 'a"b' 'a'b' ab ab '' '' ''
        """.trim()

        assertEquals(expected, command.toString())
    }

    @Test
    fun `executeOrThrow when success`() {
        val command = Command(echo, Arguments("hello", "world"))

        val actual = command.executeOrThrow()

        assertEquals("hello world", actual)
    }

    @Test
    fun `executeOrThrow when failure`() {
        val command = Command(ls, Arguments("/invalid/path"))
        val expectedError =
            "Running shell command failed with code 1 and message: ls: /invalid/path: No such file or directory"

        val error = assertThrows<ShellRunException> {
            command.executeOrThrow()
        }

        assertEquals(expectedError, error.message)
    }

    @Test
    fun `executeOrThrow when command not found`() {
        val notInstalled = Executable(
            name = "sdlpop",
            helpUrl = URL("https://github.com/NagyD/SDLPoP"),
        )
        val command = Command(notInstalled, Arguments("--version"))
        val expectedError = "Command ${notInstalled.name} not found. See ${notInstalled.helpUrl}."

        val error = assertThrows<ShellExecutableNotFoundException> {
            command.executeOrThrow()
        }

        assertEquals(expectedError, error.message)
    }

    @Test
    fun `executeOrElse when success`() {
        val command = Command(echo, Arguments("hello", "world"))

        val actual = command.executeOrElse { error("should have worked") }

        assertEquals("hello world", actual)
    }

    @Test
    fun `executeOrElse when failure`() {
        val command = Command(ls, Arguments("/invalid/path"))
        val resultIfFailed = "skipping invalid path"

        val actual = command.executeOrElse { resultIfFailed }

        assertEquals(resultIfFailed, actual)
    }

    @Test
    fun `executeOrElse when command not found`() {
        val notInstalled = Executable(
            name = "sdlpop",
            helpUrl = URL("https://github.com/NagyD/SDLPoP"),
        )
        val command = Command(notInstalled, Arguments("--version"))
        val expectedError = "Command ${notInstalled.name} not found. See ${notInstalled.helpUrl}."

        val actual = command.executeOrElse { it.message ?: "Failed" }

        assertEquals(expectedError, actual)
    }
}
