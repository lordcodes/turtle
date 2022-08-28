package com.lordcodes.turtle

import com.lordcodes.turtle.ArgsTest.LsFlag
import com.lordcodes.turtle.util.shouldBe
import com.lordcodes.turtle.util.shouldThrowMessage
import org.junit.jupiter.api.Test
import java.net.URL

internal class ExecutableTest {
    val ls = Executable("ls", URL("https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/"))

    @Test
    fun `failing with command not found is dumb - tell how to install instead`() {
        val notInstalled = Executable(
            name = "sdlpop",
            howToInstall = URL("https://github.com/NagyD/SDLPoP")
        )
        val expectedError = "Command ${notInstalled.name} not found. See ${notInstalled.howToInstall}"
        shouldThrowMessage(expectedError) {
            Command(notInstalled, Args("--version")).executeOrElse { "ab" }
        }
        shouldThrowMessage(expectedError) {
            Command(notInstalled, Args("--version")).executeOrThrow()
        }
    }

    @Test
    fun `executable + Args equals Command`() {
        val args = Args("-l", "-a")
        ls + args shouldBe Command(ls, args)
    }

    @Test
    fun `executable + withArg equals Command`() {
        val flags: List<WithArg> = listOf(LsFlag.OnePerLine, LsFlag.BySizeDesc)

        ls + flags.first() shouldBe Command(ls, Args("-1"))
        ls + flags shouldBe Command(ls, Args("-1", "-S"))
    }
}
