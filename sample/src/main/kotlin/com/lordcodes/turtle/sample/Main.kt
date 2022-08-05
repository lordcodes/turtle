package com.lordcodes.turtle.sample

import com.lordcodes.turtle.GitCommands
import com.lordcodes.turtle.shellRun

fun main() {
    shellRun {
        val gitLocation = files.which("git") ?: error("git must be installed")
        println("git: $gitLocation")

        println(git.gitAuthorName())

        command("mkdir", listOf("Test"))
        command("mkdir", listOf("Test2"))
        command("rm", listOf("-rf", "BLAH"))
        command("rm", listOf("-rf", "Test", "Test2"))
    }
}

fun GitCommands.gitAuthorName() = gitCommand(listOf("--no-pager", "show", "-s", "--format=%an"))
