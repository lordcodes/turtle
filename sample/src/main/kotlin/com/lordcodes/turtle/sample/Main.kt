package com.lordcodes.turtle.sample

import com.lordcodes.turtle.GitCommands
import com.lordcodes.turtle.shellRun

/**
 * Run the sample
 */
fun main() {
    shellRun {
        println(git.gitAuthorName())

        command("mkdir", listOf("Test"))
        command("mkdir", listOf("Test2"))
        command("rm", listOf("-rf", "BLAH"))
        command("rm", listOf("-rf", "Test"))
    }
}

fun GitCommands.gitAuthorName() = gitCommand(listOf("--no-pager", "show", "-s", "--format=%an"))
