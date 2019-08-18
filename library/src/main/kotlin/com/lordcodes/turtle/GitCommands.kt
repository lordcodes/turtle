package com.lordcodes.turtle

class GitCommands(
    private val shell: ShellScript
) {
    fun init() = shell.command("git init")
}
