package com.lordcodes.turtle

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.io.TempDir

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

    @Test
    fun which() {
        assertEquals(shell.files.which("ls"), "/bin/ls")
        assertTrue(shell.files.which("xrearsKJlsa") == null)
    }
}
