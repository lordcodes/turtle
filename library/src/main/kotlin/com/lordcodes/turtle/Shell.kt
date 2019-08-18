package com.lordcodes.turtle

import java.io.File

fun shellRun(runLocation: File? = null, script: ShellScript.() -> Unit) {
    ShellScript(runLocation).script()
}

fun shellRun(command: String, arguments: List<String> = listOf(), runLocation: File? = null) =
    shellRun(runLocation) { command(command, arguments) }
