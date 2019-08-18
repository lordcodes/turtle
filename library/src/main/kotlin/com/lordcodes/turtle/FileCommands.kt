package com.lordcodes.turtle

import java.io.File

class FileCommands internal constructor(
    private val shell: ShellScript
) {
    fun openFile(path: File) = openFile(path.absolutePath)

    fun openFile(path: String) = shell.command("open", listOf(path))

    fun openApplication(name: String) = shell.command("open", listOf("-a", name))

    fun createSymlink(targetPath: File, linkPath: File) =
        createSymlink(targetPath.absolutePath, linkPath.absolutePath)

    fun createSymlink(targetPath: String, linkPath: String) =
        shell.command("ln", listOf("-s", targetPath, linkPath))

    fun readSymlink(linkPath: File) = readSymlink(linkPath.absolutePath)

    fun readSymlink(linkPath: String) = shell.command("readlink", listOf(linkPath))
}
