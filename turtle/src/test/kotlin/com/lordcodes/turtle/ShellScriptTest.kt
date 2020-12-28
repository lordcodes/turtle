package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.Locale
import java.util.UUID

internal class ShellScriptTest {
    @Test
    fun command_withoutArguments() {
        val output = ShellScript().command("uuidgen")

        val uuid = UUID.fromString(output)
        assertThat(uuid.toString()).isEqualTo(output.toLowerCase(Locale.US))
    }

    @Test
    fun command_withArguments() {
        val output = ShellScript().command("echo", listOf("Hello world!"))

        assertThat(output).isEqualTo("Hello world!")
    }

    @Test
    fun command_withWorkingDirectory(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript(temporaryFolder)

        val output = script.command("cat", listOf(testFile.name))

        assertThat(output).isEqualTo("expectedValue")
    }
    @Test
    fun changeWorkingDirectory_stringPath(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript()

        script.changeWorkingDirectory(temporaryFolder.absolutePath)
        val output = script.command("cat", listOf(testFile.name))

        assertThat(output).isEqualTo("expectedValue")
    }
    @Test
    fun changeWorkingDirectory_file(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript()

        script.changeWorkingDirectory(temporaryFolder)
        val output = script.command("cat", listOf(testFile.name))

        assertThat(output).isEqualTo("expectedValue")
    }
}
