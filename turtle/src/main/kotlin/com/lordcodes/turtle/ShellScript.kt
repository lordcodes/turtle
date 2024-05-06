package com.lordcodes.turtle

import com.lordcodes.turtle.internal.EmptyInputStream
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Create and run either built-in or specified shell commands.
 */
class ShellScript constructor(
    workingDirectory: File? = null,
    private val dryRun: Boolean = false,
) {
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
     * @throws [ShellCommandNotFoundException] The command wasn't found.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun command(
        command: String,
        arguments: List<String> = listOf(),
        callbacks: ProcessCallbacks = EmptyProcessCallbacks,
    ): String {
        if (dryRun) {
            println(dryRunCommand(command, arguments))
            return ""
        }
        return try {
            val splitCommand = listOf(command) + arguments
            val process = processBuilder
                .command(splitCommand)
                .start()
            onProcessStart(process, callbacks)
            process.waitFor(COMMAND_TIMEOUT, TimeUnit.MINUTES)
            process.retrieveOutput()
        } catch (exception: IOException) {
            if (exception.message?.contains("Cannot run program") == true) {
                throw ShellCommandNotFoundException(command, exception)
            }
            throw ShellFailedException(exception)
        } catch (exception: InterruptedException) {
            throw ShellFailedException(exception)
        }
    }

    private fun dryRunCommand(command: String, arguments: List<String>): String {
        val formattedArguments = arguments.joinToString(" ")
        return "$command $formattedArguments"
    }

    private fun Process.retrieveOutput(): String {
        val outputText = inputStream.bufferedReader().use(BufferedReader::readText)
        val exitCode = exitValue()
        if (exitCode != 0) {
            val errorText = errorStream.bufferedReader().use(BufferedReader::readText)
            throw ShellRunException(exitCode, errorText.trim())
        }
        return outputText.trim()
    }

    /**
     * Run a shell [command] with the specified [arguments], allowing standard output or error to be read as a stream,
     * within [ProcessOutput].
     *
     * @throws [ShellCommandNotFoundException] The command wasn't found.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Deprecated(
        message = "Will be removed in the next major release as it hangs when streaming large amount of output. " +
            "Please use 'commandSequence' instead.",
        replaceWith = ReplaceWith(
            "commandSequence(command, arguments, callbacks)",
        )
    )
    fun commandStreaming(
        command: String,
        arguments: List<String> = listOf(),
        callbacks: ProcessCallbacks = EmptyProcessCallbacks,
    ): ProcessOutput {
        if (dryRun) {
            println(dryRunCommand(command, arguments))
            return ProcessOutput(
                exitCode = 0,
                standardOutput = EmptyInputStream(),
                standardError = EmptyInputStream(),
            )
        }
        return try {
            val splitCommand = listOf(command) + arguments
            val process = processBuilder
                .command(splitCommand)
                .start()
            onProcessStart(process, callbacks)
            process.waitFor(COMMAND_TIMEOUT, TimeUnit.MINUTES)
            ProcessOutput(
                exitCode = process.exitValue(),
                standardOutput = process.inputStream,
                standardError = process.errorStream,
            )
        } catch (exception: IOException) {
            if (exception.message?.contains("Cannot run program") == true) {
                throw ShellCommandNotFoundException(command, exception)
            }
            throw ShellFailedException(exception)
        } catch (exception: InterruptedException) {
            throw ShellFailedException(exception)
        }
    }

    /**
     * Run a shell [command] with the specified [arguments], returning standard output and standard error
     * line-by-line as a [Sequence].
     *
     * @throws [ShellCommandNotFoundException] The command wasn't found.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commandSequence(
        command: String,
        arguments: List<String> = listOf(),
        callbacks: ProcessCallbacks = EmptyProcessCallbacks,
    ): Sequence<String> {
        if (dryRun) {
            println(dryRunCommand(command, arguments))
            return sequenceOf("")
        }
        return try {
            val splitCommand = listOf(command) + arguments
            val process = processBuilder
                .redirectErrorStream(true)
                .command(splitCommand)
                .start()
            onProcessStart(process, callbacks)

            sequence {
                yieldAll(process.inputStream.bufferedReader().lineSequence())

                val exitCode = process.waitFor()
                if (exitCode != 0) {
                    throw ShellRunException(exitCode)
                }
            }
        } catch (exception: IOException) {
            if (exception.message?.contains("Cannot run program") == true) {
                throw ShellCommandNotFoundException(command, exception)
            }
            throw ShellFailedException(exception)
        } catch (exception: InterruptedException) {
            throw ShellFailedException(exception)
        }
    }

    internal fun multiplatform(createCommand: (Platform) -> Command?): String {
        val operatingSystem = Platform.fromSystem()
        val command = createCommand(operatingSystem)
            ?: throw ShellCommandNotFoundException("Command not available for $operatingSystem", null)
        return command.executeOrThrow(this)
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
