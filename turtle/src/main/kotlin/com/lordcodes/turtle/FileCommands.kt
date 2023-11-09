package com.lordcodes.turtle

import java.io.File

/**
 * Commands that deal with files and operate on the file system.
 */
class FileCommands internal constructor(
    private val shell: ShellScript,
) {
    /**
     * Open a [url] using its default application. This URL can be a file path or web URL.
     *
     * @param [url] The URL to open, file path or web URL.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    fun open(url: String): String = shell.multiplatform { operatingSystem ->
        when (operatingSystem) {
            OperatingSystem.LINUX -> Executable("xdg-open") + Arguments(url)
            OperatingSystem.MAC -> Executable("open") + Arguments(url)
            OperatingSystem.WINDOWS -> Executable("cmd.exe") + Arguments("/c", "start", url)
        }
    }

    /**
     * Open a file at [path] using its default application, returning any output as a [String].
     *
     * @param [path] The file to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused", "MemberVisibilityCanBePrivate")
    fun openFile(path: File): String = open(path.toString())

    /**
     * Open a file at [path] using its default application, returning any output as a [String].
     *
     * @param [path] The full file path to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused")
    fun openFile(path: String): String = open(path)

    /**
     * Open an application by [name], returning any output as a [String]. Only available on Mac.
     *
     * @param [name] The name of the application to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellCommandNotFoundException] When called on Linux or Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused")
    fun openApplication(name: String): String = shell.multiplatform { operatingSystem ->
        when (operatingSystem) {
            OperatingSystem.LINUX -> null
            OperatingSystem.MAC -> Executable("open") + Arguments("-a", name)
            OperatingSystem.WINDOWS -> null
        }
    }

    /**
     * Create a symbolic link at a given [linkPath], that links back to [targetPath] at a different location. Any
     * output will be returned as a [String]. Only available on Mac or Linux.
     *
     * @param [targetPath] The target to link to.
     * @param [linkPath] The location for the symlink.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellCommandNotFoundException] When called on Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun createSymlink(targetPath: File, linkPath: File): String =
        createSymlink(targetPath.toString(), linkPath.toString())

    /**
     * Create a symbolic link at a given [linkPath], that links back to [targetPath] at a different location. Any
     * output will be returned as a [String]. Only available on Mac or Linux.
     *
     * @param [targetPath] The full file path of the target to link to.
     * @param [linkPath] The full file path for the symlink.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellCommandNotFoundException] When called on Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun createSymlink(targetPath: String, linkPath: String): String = shell.multiplatform { operatingSystem ->
        when (operatingSystem) {
            OperatingSystem.LINUX -> Executable("ln") + Arguments("-s", targetPath, linkPath)
            OperatingSystem.MAC -> Executable("ln") + Arguments("-s", targetPath, linkPath)
            OperatingSystem.WINDOWS -> null
        }
    }

    /**
     * Read the target path as a [String] of a symbolic link located at [linkPath]. Only available on Mac or Linux.
     *
     * @param [linkPath] The location for the symlink.
     *
     * @return [String] The target file path the symlink links to.
     *
     * @throws [ShellCommandNotFoundException] When called on Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun readSymlink(linkPath: File): String = readSymlink(linkPath.toString())

    /**
     * Read the target path of a symbolic link. Only available on Mac or Linux.
     *
     * @param [linkPath] The full file path for the symlink.
     *
     * @return [String] The target file path the symlink links to.
     *
     * @throws [ShellCommandNotFoundException] When called on Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun readSymlink(linkPath: String): String = shell.multiplatform { operatingSystem ->
        when (operatingSystem) {
            OperatingSystem.LINUX -> Executable("readlink") + Arguments(linkPath)
            OperatingSystem.MAC -> Executable("readlink") + Arguments(linkPath)
            OperatingSystem.WINDOWS -> null
        }
    }

    /**
     * Retrieve the location of the provided command, can be used to determine if a command is available.
     * Only available on Mac and Linux.
     *
     * ```kotlin
     * shellRun {
     *   which("git") ?: error("git is not installed")
     * }
     * ```
     *
     * @param [command] The command for which to get the location.
     *
     * @return [String] The location of the provided command or null if not found.
     *
     * @throws [ShellCommandNotFoundException] When called on Windows.
     * @throws [ShellFailedException] There was an issue running the command.
     */
    @Suppress("SwallowedException")
    fun which(command: String): String? = try {
        shell.multiplatform { operatingSystem ->
            when (operatingSystem) {
                OperatingSystem.LINUX -> Executable("which") + Arguments(command)
                OperatingSystem.MAC -> Executable("which") + Arguments(command)
                OperatingSystem.WINDOWS -> null
            }
        }
    } catch (ex: ShellRunException) {
        null
    }
}
