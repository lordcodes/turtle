package com.lordcodes.templatekotlinjvmlibrary.hooks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

open class UninstallGitHooksTask : DefaultTask() {
    @Suppress("unused")
    @TaskAction
    fun uninstallGitHooks() {
        val gitHooksDirectory = try {
            resolveGitHooksDirectory()
        } catch (error: IOException) {
            System.err.println(error.message)
            exitProcess(1)
        }

        InstallGitHooksTask.HOOK_FILES.forEach { name ->
            val hookFile = File(gitHooksDirectory, name)
            uninstallHook(hookFile = hookFile)
        }
    }

    @Throws(IOException::class)
    private fun resolveGitHooksDirectory(): File {
        val hooksDirectory = File(".git/hooks")
        if (!hooksDirectory.exists() && !hooksDirectory.isDirectory) {
            throw IOException("Couldn't find .git/hooks directory.")
        }
        return hooksDirectory
    }

    private fun uninstallHook(hookFile: File) {
        if (hookFile.exists() && hookFile.isProjectGeneratedHook()) {
            hookFile.delete()

            val backupFile = File("${hookFile.absolutePath}.backup")
            if (backupFile.exists()) {
                backupFile.copyTo(hookFile)
            }
        }
    }

    private fun File.isProjectGeneratedHook(): Boolean {
        return readText().indexOf(InstallGitHooksTask.UPDATEABLE_IDENTIFIER) > -1
    }
}