@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")

package com.lordcodes.turtle

import com.lordcodes.turtle.ArgsTest.LsFlag
import com.lordcodes.turtle.util.shouldBe
import com.lordcodes.turtle.util.shouldThrowMessage
import org.junit.jupiter.api.Test
import java.net.URL

internal class CommandTest {
    val builtinUnixCommands = URL("https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/")
    val echo = Executable("echo", builtinUnixCommands)
    val ls = Executable("ls", builtinUnixCommands)
    val lsCommand = Command(ls, Args("-a", "-l", "--color=when", "src", "build"))

    @Test
    fun `execute a command - success case`() {
        val command = Command(echo, Args("hello", "world"))
        command.executeOrThrow() shouldBe "hello world"
        command.executeOrElse { error("should have worked") } shouldBe "hello world"
    }

    @Test
    fun `execute a command - failure case`() {
        val invalidCommand = Command(ls, Args("/invalid/path"))
        val expectedError =
            "Running shell command failed with code 1 and message: ls: /invalid/path: No such file or directory"

        shouldThrowMessage(expectedError) {
            invalidCommand.executeOrThrow()
        }

        val resultIfFailed = "skipping invalid path"
        invalidCommand.executeOrElse { resultIfFailed } shouldBe resultIfFailed

        invalidCommand.executeOrElse {
            println("Invalid path, got expection $it, quick return instead of crashing")
            return
        }
    }

    @Test
    fun `Command#toString - simple arguments, no escaping`() {
        lsCommand.toString() shouldBe "ls -a -l --color=when src build"
    }

    @Test
    fun `Command#toString with dumb bash escaping`() {
        val command = Command(
            ls,
            Args("ab", "a b", "a\"b", "a\'b", "'ab'", "\"ab\"", "", "''", "\"''\"")
        )
        val quotedCommand = """
            ls ab 'a b' 'a"b' 'a'b' ab ab '' '' ''
        """.trim()
        command.toString() shouldBe quotedCommand
    }

    @Test
    fun `Command operators`() {

        val withArgs: Iterable<WithArg> = // -a -l
            listOf(LsFlag.AllFiles, LsFlag.LongFormat)
        val withArg: WithArg = withArgs.first() // -a
        val withArg2 = withArgs.last() // -l

        val lsOneArg = Command(ls, Args("-1"))
        val lsThreeArgs = Command(ls, Args("-1", "-a", "-l"))
        lsOneArg + withArgs shouldBe lsThreeArgs
        lsOneArg + Args("-a", "-l") shouldBe lsThreeArgs
        lsOneArg + withArg + Args("-l") shouldBe lsThreeArgs

        lsThreeArgs - Args("-a", "-l") shouldBe lsOneArg
        lsThreeArgs - withArg - withArg2 shouldBe lsOneArg
        lsThreeArgs - withArgs shouldBe lsOneArg

        ("-1" in lsThreeArgs) shouldBe true
        (withArg in lsThreeArgs) shouldBe true
        (withArgs in lsThreeArgs) shouldBe true

        ("-a" !in lsOneArg) shouldBe true
    }
}
