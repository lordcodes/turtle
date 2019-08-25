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
     * Access commands that deal with files or the filesystem.
     */
    val files = FileCommands(this)

    /**
     * Access commands that deal with Git.
     */
    val git = GitCommands(this)

    /**
     * Change the working directory for subsequent shell commands.
     *
     * @param [path] The path to set the working directory to.
     */
    fun changeWorkingDirectory(path: String) = changeWorkingDirectory(File(path))

    /**
     * Change the working directory for subsequent shell commands.
     *
     * @param [path] The path to set the working directory to.
     */
    fun changeWorkingDirectory(path: File) {
        processBuilder.directory(path)
    }

    /**
     * Run a shell command with the specified arguments.
     *
     * @param [command] A command to run.
     * @param [arguments] The arguments to pass to the command.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun command(
        command: String,
        arguments: List<String> = listOf()
    ): String = try {
        val splitCommand = listOf(command) + arguments
        val process = processBuilder
            .command(splitCommand)
            .start()
        process.waitFor(COMMAND_TIMEOUT, TimeUnit.MINUTES)
        process.retrieveOutput()
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

    companion object {
        private const val COMMAND_TIMEOUT = 60L
    }
}
