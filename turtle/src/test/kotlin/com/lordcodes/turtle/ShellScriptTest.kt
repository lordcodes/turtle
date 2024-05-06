package com.lordcodes.turtle

import java.io.File
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.io.TempDir

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
    fun commandSequence(@TempDir temporaryFolder: File) {
        val testFile = File(temporaryFolder, "testFile")
        testFile.createNewFile()
        testFile.appendText("Line 1\n")
        testFile.appendText("Line 2\n")
        testFile.appendText("Line 3\n")
        val shell = ShellScript(temporaryFolder)

        val sequence = shell.commandSequence("cat", listOf(testFile.name))

        assertEquals(sequence.toList(), listOf("Line 1", "Line 2", "Line 3"))
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
