package com.lordcodes.turtle

import com.lordcodes.turtle.util.shouldBe
import com.lordcodes.turtle.util.shouldThrowMessage
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URI
import java.net.URL
import kotlin.random.Random

internal class ArgsTest {
    val builtinUnixCommands = URL("https://www.cyberciti.biz/faq/linux-unix-bash-shell-list-all-builtin-commands/")
    val ls = Executable("ls", builtinUnixCommands)
    val lsCommand = Command(ls, Args("-a", "-l", "--color=when", "src", "build"))
    val url = "http://example.com"

    @Test
    fun `Args with vararg`() {
        Args("a", "b") shouldBe Args(listOf("a", "b"))
    }

    @Test
    fun `Args plus Args and Command plus Args`() {
        val folders = listOf(File("src"), File("build"))
        val allWithDetails = Args("-a", "-l")
        val longOptions = Args("--color=when")

        val allArgs = allWithDetails + longOptions + Args(folders)
        allArgs shouldBe lsCommand.args

        val lsPlusArgs = Command(ls) + allArgs
        lsPlusArgs shouldBe lsCommand
        ls + allArgs shouldBe lsCommand
    }

    @Test
    fun `Args plus minus withArg`() {
        val withArg: WithArg = LsFlag.OnlyDirectory // -d
        val withArgs: Iterable<WithArg> = listOf(LsFlag.OnlyDirectory, LsFlag.OnePerLine) // -d -1

        Args("-a") + withArg shouldBe Args("-a", "-d")
        Args("-a") + withArgs shouldBe Args("-a", "-d", "-1")

        Args("-a", "-d") - withArg shouldBe Args("-a")
        Args("-a", "-d", "-1") - withArgs shouldBe Args("-a")
        Args("-a", "-d") - withArgs shouldBe Args("-a")
    }

    @Test
    fun `removing arguments`() {
        val expected = Args("--color=when", "src", "build")
        lsCommand.args - Args("-a", "-l") shouldBe expected
        lsCommand - Args("-a", "-l") shouldBe Command(ls, expected)
    }

    @Test
    fun `contains operators`() {
        ("-a" in lsCommand) shouldBe true
        ("-1" !in lsCommand) shouldBe true

        (LsFlag.AllFiles in lsCommand) shouldBe true
        (LsFlag.OnePerLine !in lsCommand) shouldBe true
    }

    @Test
    fun `transform all kind of classes to Args`() {
        Args(0, 1, 10.0, true, false, 'c') shouldBe Args("0", "1", "10.0", "true", "false", "c")

        Args(URL(url), URI(url)) shouldBe Args(url, url)

        Args(File("src"), File("/tmp")) shouldBe Args("src", "/tmp")
    }

    @Test
    fun `flattent Args`() {
        Args("a", Pair("b", "c")) shouldBe Args("a", "b", "c")

        Args("a", listOf("b", "c", "c")) shouldBe Args("a", "b", "c", "c")

        Args("a", setOf("b", "c", "c")) shouldBe Args("a", "b", "c")

        Args("a", listOf(listOf("b", "c"))) shouldBe Args("a", "b", "c")

        val triple = Triple(1, File("/home"), URL(url))
        Args(Triple("a", "b", "c")) shouldBe Args("a", "b", "c")
        Args(listOf(Pair("b", "c")), triple) shouldBe Args(
            "b", "c", "1", "/home", "http://example.com"
        )
    }

    @Test
    fun `Args options using a map`() {
        val options = mapOf(
            "--color" to "blue",
            "--verbose" to true,
            "--max" to 42
        )
        Args(options) shouldBe Args("--color", "blue", "--verbose", "true", "--max", 42)
    }

    @Test
    fun `Args with invalid arguments`() {
        shouldThrowMessage("Classes couldn't be converted to Args: [XorWowRandom, ProcessBuilder]") {
            Command(ls, Args("invalid", Random(42), ProcessBuilder()))
        }
    }

    @Test
    fun `you can define your own class extending WithArg`() {
        ls + Args(LsFlag.LongFormat, LsFlag.AllFiles, LsFlag.BySizeDesc) shouldBe ls + Args("-l", "-a", "-S")
    }

    enum class LsFlag(override val arg: String) : WithArg {
        OnePerLine("-1"),
        AllFiles("-a"),
        LongFormat("-l"),
        BySizeDesc("-S"),
        OnlyDirectory("-d"),
    }
}
