package com.lordcodes.turtle

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Create and run either built-in or specified shell commands.
 */
class ShellScript internal constructor(workingDirectory: File? = null) {
    private val processBuilder = ProcessBuilder(listOf())
        .directory(workingDirectory)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)

    /**
     * Default callbacks into the process started for each command that is executed.
     */
    var defaultCallbacks: ProcessCallbacks = EmptyProcessCallbacks

    /**
     * Access commands that deal with files or the filesystem.
     */
    val files = FileCommands(this)

    /**
     * Access commands that deal with Git.
     */
    val git = GitCommands(this)

    /**
     * Run a shell command with the specified arguments, receiving the output as a String.
     *
     * @param [command] A command to run.
     * @param [arguments] The arguments to pass to the command.
     * @param [callbacks] Callbacks into the process
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun command(
        command: String,
        arguments: List<String> = listOf(),
        callbacks: ProcessCallbacks = EmptyProcessCallbacks
    ): String = runCommand(command, arguments, callbacks) { it.retrieveOutput() }

    /**
     * Run a shell command with the specified arguments, allowing standard output or error to be read as a stream.
     *
     * @param [command] A command to run.
     * @param [arguments] The arguments to pass to the command.
     *
     * @return [ProcessOutput] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commandStreaming(
        command: String,
        arguments: List<String> = listOf(),
        callbacks: ProcessCallbacks = EmptyProcessCallbacks
    ): ProcessOutput = runCommand(command, arguments, callbacks) { process ->
        ProcessOutput(
            exitCode = process.exitValue(),
            standardOutput = process.inputStream,
            standardError = process.errorStream
        )
    }

    private fun <OutputT> runCommand(
        command: String,
        arguments: List<String>,
        callbacks: ProcessCallbacks,
        prepareOutput: (Process) -> OutputT
    ): OutputT = try {
        val splitCommand = listOf(command) + arguments
        val process = processBuilder
            .command(splitCommand)
            .start()
        onProcessStart(process, callbacks)
        process.waitFor(COMMAND_TIMEOUT, TimeUnit.MINUTES)
        prepareOutput(process)
    } catch (exception: IOException) {
        throw ShellFailedException(exception)
    } catch (exception: InterruptedException) {
        throw ShellFailedException(exception)
    }

    private fun Process.retrieveOutput(): String {
        val outputText = inputStream.bufferedReader().use(BufferedReader::readText)
        val exitCode = exitValue()
        if (exitCode != 0) {
            val errorText = errorStream.bufferedReader().use(BufferedReader::readText)
            if (errorText.isNotEmpty()) {
                throw ShellRunException(exitCode, errorText.trim())
            }
        }
        return outputText.trim()
    }

    /**
     * Change the working directory for subsequent shell commands.
     *
     * @param [path] The path to set the working directory to.
     */
    fun changeWorkingDirectory(path: String) {
        changeWorkingDirectory(File(path))
    }

    /**
     * Change the working directory for subsequent shell commands.
     *
     * @param [path] The path to set the working directory to.
     */
    fun changeWorkingDirectory(path: File) {
        processBuilder.directory(path)
    }

    private fun onProcessStart(process: Process, callbacks: ProcessCallbacks) {
        defaultCallbacks.onProcessStart(process)
        callbacks.onProcessStart(process)
    }

    private object EmptyProcessCallbacks : ProcessCallbacks

    companion object {
        private const val COMMAND_TIMEOUT = 60L
    }
}
