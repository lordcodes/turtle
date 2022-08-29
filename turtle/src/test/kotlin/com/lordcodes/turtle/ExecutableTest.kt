package com.lordcodes.turtle

import com.lordcodes.turtle.ArgumentsTest.LsFlag
import org.junit.jupiter.api.Test
import java.net.URL
import kotlin.test.assertEquals

internal class ExecutableTest {
    private val ls = Executable(
        "ls",
        URL("https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/")
    )

    @Test
    fun `plus with Arguments`() {
        val arguments = Arguments("-l", "-a")

        val actual = ls + arguments

        assertEquals(Command(ls, arguments), actual)
    }

    @Test
    fun `plus with multiple WithArgument`() {
        val flags: List<WithArgument> = listOf(LsFlag.OnePerLine, LsFlag.BySizeDesc)

        val actual = ls + flags

        assertEquals(Command(ls, Arguments(listOf("-1", "-S"))), actual)
    }

    @Test
    fun `plus with single WithArgument`() {
        val actual = ls + LsFlag.OnePerLine

        assertEquals(Command(ls, Arguments(listOf("-1"))), actual)
    }
}
