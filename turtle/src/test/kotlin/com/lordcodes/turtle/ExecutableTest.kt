package com.lordcodes.turtle

import com.lordcodes.turtle.ArgsTest.LsFlag
import com.lordcodes.turtle.Git.Action.Add
import com.lordcodes.turtle.Git.Action.Clone
import com.lordcodes.turtle.Git.Action.Init
import com.lordcodes.turtle.util.shouldBe
import com.lordcodes.turtle.util.shouldThrowMessage
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL

internal class ExecutableTest {
    val ls = Executable("ls", URL("https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/"))

    @Test
    fun `failing with command not found is dumb - tell how to install instead`() {
        val notInstalled = Executable(
            name = "sdlpop",
            url = URL("https://github.com/NagyD/SDLPoP")
        )
        val expectedError = "Command ${notInstalled.name} not found. See ${notInstalled.url}"
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
    /** See [Git] **/
    fun `executable + withArg equals Command`() {
        val flags: List<WithArg> = listOf(LsFlag.OnePerLine, LsFlag.BySizeDesc)

        ls + flags.first() shouldBe Command(ls, Args("-1"))
        ls + flags shouldBe Command(ls, Args("-1", "-S"))
    }

    @Test
    fun `define your own executable subclass to create command builder`() {
        Git.init() shouldBe Command(Git, Args("init"))

        val refreshVersions = URL("https://github.com/jmfayard/refreshVersions.git")
        Git.clone(refreshVersions) shouldBe Command(Git, Args("clone", refreshVersions.toString()))

        val files = listOf(File("dir1"), File("dir2"))
        Git.add(files) shouldBe Command(Git, Args("add", "dir1", "dir2"))
    }
}

internal object Git : Executable("git", URL("https://git-scm.com/docs")) {
    enum class Action {
        Init, Clone, Add;
        fun command(): Command = Command(Git, Args(name.lowercase()))
    }

    fun clone(url: URL, destination: File? = null) =
        Clone.command() + Args(url, destination)

    fun init() = Init.command()

    fun add(files: List<File>) = Add.command() + Args(files)
}
