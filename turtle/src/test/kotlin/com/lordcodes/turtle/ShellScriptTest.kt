package com.lordcodes.turtle

import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ShellScriptTest {
    @Test
    fun command_withoutArguments() {
        val output = ShellScript().command("uuidgen")

        val uuid = UUID.fromString(output)
        assertEquals(uuid.toString(), output.lowercase())
    }

    @Test
    fun command_withArguments() {
        val output = ShellScript().command("echo", listOf("Hello world!"))

        assertEquals(output, "Hello world!")
    }

    @Test
    fun command_withWorkingDirectory(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript(temporaryFolder)

        val output = script.command("cat", listOf(testFile.name))

        assertEquals(output, "expectedValue")
    }

    @Test
    fun command_processCallback() {
        var defaultCallbacksProcess: Process? = null
        val script = ShellScript()
        script.defaultCallbacks = object : ProcessCallbacks {
            override fun onProcessStart(process: Process) {
                defaultCallbacksProcess = process
            }
        }
        var commandProcess: Process? = null
        val callback = object : ProcessCallbacks {
            override fun onProcessStart(process: Process) {
                commandProcess = process
            }
        }

        script.command("echo", listOf("Hello world!"), callback)

        assertTrue(commandProcess != null)
        assertTrue(defaultCallbacksProcess != null)
        assertEquals(defaultCallbacksProcess, commandProcess)
    }

    @Test
    fun commandStreaming() {
        val output = ShellScript().commandStreaming("echo", listOf("Hello world!"))

        assertEquals(output.standardOutput.bufferedReader().readText().trim(), "Hello world!")
    }

    @Test
    fun changeWorkingDirectory_stringPath(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript()

        script.changeWorkingDirectory(temporaryFolder.absolutePath)
        val output = script.command("cat", listOf(testFile.name))

        assertEquals(output, "expectedValue")
    }

    @Test
    fun changeWorkingDirectory_file(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.writeText("expectedValue")
        val script = ShellScript()

        script.changeWorkingDirectory(temporaryFolder)
        val output = script.command("cat", listOf(testFile.name))

        assertEquals(output, "expectedValue")
    }
}
