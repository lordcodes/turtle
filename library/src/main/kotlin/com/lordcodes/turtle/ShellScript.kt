package com.lordcodes.turtle

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class ShellScript(runLocation: File? = null) {
    private val processBuilder = ProcessBuilder(listOf())
        .directory(runLocation)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)

    val git = GitCommands(this)

    fun command(
        command: String,
        arguments: List<String> = listOf()
    ): String = try {
        val splitCommand = command.split(' ') + arguments
        val process = processBuilder
            .command(splitCommand)
            .start()
        process.waitFor(60, TimeUnit.MINUTES)
        process.retrieveOutput()
    } catch (exception: IOException) {
        throw ShellRunException(exception)
    } catch (exception: InterruptedException) {
        throw ShellRunException(exception)
    }

    private fun Process.retrieveOutput(): String {
        val outputText = inputStream.bufferedReader().use(BufferedReader::readText)
        if (exitValue() != 0) {
            val errorText = errorStream.bufferedReader().use(BufferedReader::readText)
            if (errorText.isNotEmpty()) {
                throw ShellRunException(errorText.trim())
            }
        }
        return outputText.trim()
    }
}
