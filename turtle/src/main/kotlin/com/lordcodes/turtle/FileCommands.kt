package com.lordcodes.turtle

import java.io.File

/**
 * Commands that deal with files and operate on the file system.
 */
class FileCommands internal constructor(
    private val shell: ShellScript
) {
    /**
     * Open a file using its default application.
     *
     * @param [path] The file to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused")
    fun openFile(path: File) = openFile(path.toString())

    /**
     * Open a file using its default application.
     *
     * @param [path] The full file path to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun openFile(path: String) = shell.command("open", listOf(path))

    /**
     * Open an application by name.
     *
     * @param [name] The name of the application to open.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    @Suppress("unused")
    fun openApplication(name: String) = shell.command("open", listOf("-a", name))

    /**
     * Create a symbolic link at a given path, that links back to a different location.
     *
     * @param [targetPath] The target to link to.
     * @param [linkPath] The location for the symlink.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun createSymlink(targetPath: File, linkPath: File) =
        createSymlink(targetPath.toString(), linkPath.toString())

    /**
     * Create a symbolic link at a given path, that links back to a different location.
     *
     * @param [targetPath] The full file path of the target to link to.
     * @param [linkPath] The full file path for the symlink.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun createSymlink(targetPath: String, linkPath: String) =
        shell.command("ln", listOf("-s", targetPath, linkPath))

    /**
     * Read the target path of a symbolic link.
     *
     * @param [linkPath] The location for the symlink.
     *
     * @return [String] The target file path the symlink links to.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun readSymlink(linkPath: File) = readSymlink(linkPath.toString())

    /**
     * Read the target path of a symbolic link.
     *
     * @param [linkPath] The full file path for the symlink.
     *
     * @return [String] The target file path the symlink links to.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun readSymlink(linkPath: String) = shell.command("readlink", listOf(linkPath))
}
