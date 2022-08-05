package com.lordcodes.turtle

import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

internal class FileCommandsTest {
    @TempDir
    lateinit var temporaryFolder: File

    private val shell by lazy { ShellScript(temporaryFolder) }
    private val files by lazy { FileCommands(shell) }

    @Test
    fun createSymlink_fileArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()

        files.createSymlink(File("targetFolder"), File("linkedFrom"))

        assertEquals(shell.command("readlink", listOf("linkedFrom")), "targetFolder")
    }

    @Test
    fun createSymlink_stringArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()

        files.createSymlink("targetFolder", "linkedFrom")

        assertEquals(shell.command("readlink", listOf("linkedFrom")), "targetFolder")
    }

    @Test
    fun readSymlink_fileArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()
        shell.command("ln", listOf("-s", "targetFolder", "linkedFrom"))

        val output = files.readSymlink(File("linkedFrom"))

        assertEquals(output, "targetFolder")
    }

    @Test
    fun readSymlink_stringArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()
        shell.command("ln", listOf("-s", "targetFolder", "linkedFrom"))

        val output = files.readSymlink("linkedFrom")

        assertEquals(output, "targetFolder")
    }
}
