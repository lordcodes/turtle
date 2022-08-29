package com.lordcodes.turtle

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.random.Random
import kotlin.test.assertEquals

internal class ArgumentsTest {
    @Test
    fun `plus with another Arguments`() {
        val before = Arguments(listOf("-a", "-l"))

        val actual = before +
            Arguments(listOf("--color=when")) +
            Arguments(File("src"), File("build"))

        assertEquals(Arguments(listOf("-a", "-l", "--color=when", "src", "build")), actual)
    }

    @Test
    fun `plus with multiple WithArgument`() {
        val before = Arguments("-a")
        val withArguments: Iterable<WithArgument> = listOf(LsFlag.OnlyDirectory, LsFlag.OnePerLine)

        val actual = before + withArguments

        assertEquals(Arguments(listOf("-a", "-d", "-1")), actual)
    }

    @Test
    fun `plus with single WithArgument`() {
        val before = Arguments("-a")

        val actual = before + LsFlag.OnlyDirectory

        assertEquals(Arguments(listOf("-a", "-d")), actual)
    }

    @Test
    fun `minus with another Arguments`() {
        val before = Arguments(listOf("-a", "-l", "src", "build"))

        val actual = before -
            Arguments(listOf("--color=when")) -
            Arguments(File("src"), File("build"))

        assertEquals(Arguments(listOf("-a", "-l")), actual)
    }

    @Test
    fun `minus with multiple WithArgument`() {
        val before = Arguments(listOf("-a", "-l", "src", "build"))
        val withArguments: Iterable<WithArgument> = listOf(LsFlag.AllFiles, LsFlag.LongFormat)

        val actual = before - withArguments

        assertEquals(Arguments(listOf("src", "build")), actual)
    }

    @Test
    fun `minus with single WithArgument`() {
        val before = Arguments(listOf("-a", "-l", "src", "build"))

        val actual = before - LsFlag.LongFormat

        assertEquals(Arguments(listOf("-a", "src", "build")), actual)
    }

    @Test
    fun `Arguments with vararg Strings`() {
        assertEquals(Arguments(listOf("a", "b")), Arguments("a", "b"))
    }

    @Test
    fun `Arguments with vararg supported types`() {
        val rawUrl = "http://example.com"
        val expected = Arguments(listOf("0", "1", "10.0", "true", "false", "c", rawUrl, rawUrl, "src"))

        val actual = Arguments(0, 1, 10.0, true, false, 'c', URL(rawUrl), URI(rawUrl), File("src"))

        assertEquals(expected, actual)
    }

    @Test
    fun `Arguments with vararg flatten`() {
        val expected = Arguments(
            listOf(
                "a",
                "b",
                "c",
                "d",
                "d",
                "e",
                "g",
                "h",
                "i",
                "j",
                "k",
                "l",
                "m",
                "n",
                "--color",
                "blue",
                "--max",
                "42"
            )
        )

        val actual = Arguments(
            "a",
            listOf("b", "c", "d"),
            setOf("d", "e", "e"),
            Pair("g", "h"),
            Triple("i", "j", File("k")),
            listOf(listOf("l", "m"), setOf("n")),
            mapOf(
                "--color" to "blue",
                "--max" to 42
            )
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `Arguments with vararg containing invalid arguments`() {
        val exception = assertThrows<IllegalArgumentException> {
            Arguments("invalid", Random(42), ProcessBuilder())
        }

        assertEquals(
            "Classes couldn't be converted to Arguments: [XorWowRandom, ProcessBuilder]",
            exception.message
        )
    }

    @Test
    fun `Arguments with vararg using WithArgument`() {
        val actual = Arguments(LsFlag.LongFormat, LsFlag.AllFiles, LsFlag.BySizeDesc)

        assertEquals(Arguments(listOf("-l", "-a", "-S")), actual)
    }

    enum class LsFlag(override val argument: String) : WithArgument {
        OnePerLine("-1"),
        AllFiles("-a"),
        LongFormat("-l"),
        BySizeDesc("-S"),
        OnlyDirectory("-d"),
    }
}
