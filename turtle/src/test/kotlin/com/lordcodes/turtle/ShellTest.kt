package com.lordcodes.turtle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.Locale
import java.util.UUID

internal class ShellTest {
    @Test
    fun shellRun_functionSyntax_singleCommand() {
        val output = shellRun { command("uuidgen") }

        val uuid = UUID.fromString(output)
        assertThat(uuid.toString()).isEqualTo(output.toLowerCase(Locale.US))
    }

    @Test
    fun shellRun_functionSyntax_seriesOfCommands() {
        val output = shellRun {
            command("uuidgen")
            command("echo", listOf("Hello world!"))
        }

        assertThat(output).isEqualTo("Hello world!")
    }

    @Test
    fun shellRun_functionSyntax_commandWithWorkingDirectory(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")

        val output = shellRun(temporaryFolder) {
            command("cat", listOf(testFile.name))
        }

        assertThat(output).isEqualTo("expectedValue")
    }

    @Test
    fun shellRun_functionSyntax_commandWithChangingWorkingDirectory(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")

        val output = shellRun {
            changeWorkingDirectory(temporaryFolder)
            command("cat", listOf(testFile.name))
        }

        assertThat(output).isEqualTo("expectedValue")
    }

    @Test
    fun shellRun_singleCommandWithoutArguments() {
        val output = shellRun("uuidgen")

        val uuid = UUID.fromString(output)
        assertThat(uuid.toString()).isEqualTo(output.toLowerCase(Locale.US))
    }

    @Test
    fun shellRun_singleCommandWithArguments() {
        val output = shellRun("echo", listOf("Hello world!"))

        assertThat(output).isEqualTo("Hello world!")
    }

    @Test
    fun shellRun_singleCommandWithWorkingDirectory(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")

        val output = shellRun("cat", listOf(testFile.name), temporaryFolder)

        assertThat(output).isEqualTo("expectedValue")
    }
}
