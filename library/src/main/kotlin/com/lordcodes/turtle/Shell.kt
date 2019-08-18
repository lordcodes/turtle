package com.lordcodes.turtle

import java.io.File

fun shellRun(workingDirectory: File? = null, script: ShellScript.() -> Unit) {
    ShellScript(workingDirectory).script()
}

fun shellRun(command: String, arguments: List<String> = listOf(), workingDirectory: File? = null) =
    shellRun(workingDirectory) { command(command, arguments) }
