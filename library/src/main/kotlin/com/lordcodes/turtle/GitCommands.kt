package com.lordcodes.turtle

import java.io.File
import java.net.URL

class GitCommands(
    private val shell: ShellScript
) {
    fun init() = gitCommand(listOf("init"))

    fun status() = gitCommand(listOf("status", "--porcelain"))

    fun commit(message: String) = gitCommand(listOf("commit", "-a", "-m", message, "--quiet"))

    fun commitAllChanges(message: String) =
        gitCommand(listOf("add", "--all", "&&", "git", "commit", "-a", "-m", message, "--quiet"))

    fun push(remote: String? = null, branch: String? = null): String {
        val arguments = mutableListOf("push")
        remote?.let { arguments.add(it) }
        branch?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    fun pull(remote: String? = null, branch: String? = null): String {
        val arguments = mutableListOf("pull")
        remote?.let { arguments.add(it) }
        branch?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    fun checkout(branch: String) =
        gitCommand(listOf("checkout", branch, "--quiet"))

    fun clone(repositoryUrl: URL, destination: File? = null) =
        clone(repositoryUrl.toString(), destination?.absolutePath)

    fun clone(repositoryUrl: String, destination: String? = null): String {
        val arguments = mutableListOf("clone", repositoryUrl)
        destination?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    fun addTag(tagName: String, message: String) =
        gitCommand(listOf("tag", "-a", tagName, "-m", message))

    fun pushTag(tagName: String) = gitCommand(listOf("push", "origin", tagName))

    fun currentBranch() = gitCommand(listOf("rev-parse", "--abbrev-ref", "HEAD"))

    private fun gitCommand(arguments: List<String>) =
        shell.command("git", arguments)
}
