package com.lordcodes.turtle.sample

import com.lordcodes.turtle.*
import java.io.File
import java.net.URL

fun main() {
    shellRun {
        val gitLocation = files.which("git") ?: error("git must be installed")
        println("git: $gitLocation")

        println(git.gitAuthorName())

        command("mkdir", listOf("Test"))
        command("mkdir", listOf("Test2"))
        command("rm", listOf("-rf", "BLAH"))
        command("rm", listOf("-rf", "Test", "Test2"))

        println(Git.add(listOf()).executeOrElse { it.message ?: "Error" })

        "Finished"
    }
}

fun GitCommands.gitAuthorName() = gitCommand(listOf("--no-pager", "show", "-s", "--format=%an"))

object Git {
    val executable = Executable("git", URL("https://git-scm.com/docs"))

    enum class Action {
        Init, Clone, Add;

        fun command(): Command = Command(executable, Arguments(name.lowercase()))
    }

    fun clone(url: URL, destination: File? = null): Command =
        Action.Clone.command() + Arguments(url, destination)

    fun init(): Command = Action.Init.command()

    fun add(files: List<File>): Command = Action.Add.command() + Arguments(files)
}
