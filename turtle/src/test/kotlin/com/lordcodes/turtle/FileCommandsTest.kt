package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

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

        assertThat(shell.command("readlink", listOf("linkedFrom"))).isEqualTo("targetFolder")
    }

    @Test
    fun createSymlink_stringArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()

        files.createSymlink("targetFolder", "linkedFrom")

        assertThat(shell.command("readlink", listOf("linkedFrom"))).isEqualTo("targetFolder")
    }

    @Test
    fun readSymlink_fileArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()
        shell.command("ln", listOf("-s", "targetFolder", "linkedFrom"))

        val output = files.readSymlink(File("linkedFrom"))

        assertThat(output).isEqualTo("targetFolder")
    }

    @Test
    fun readSymlink_stringArguments() {
        val target = File(temporaryFolder, "targetFolder")
        target.mkdir()
        shell.command("ln", listOf("-s", "targetFolder", "linkedFrom"))

        val output = files.readSymlink("linkedFrom")

        assertThat(output).isEqualTo("targetFolder")
    }

    @Test
    fun which() {
        assertThat(shell.files.which("ls")).isEqualTo("/bin/ls")
        assertThat(shell.files.which("xrearsKJlsa")).isNull()
    }
}
