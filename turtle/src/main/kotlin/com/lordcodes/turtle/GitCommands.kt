package com.lordcodes.turtle

import java.io.File
import java.net.URL

/**
 * Commands that deal with Git.
 */
class GitCommands internal constructor(
    private val shell: ShellScript
) {
    /**
     * Initialize a Git repository.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun gitInit() = gitCommand(listOf("init"))

    /**
     * Get the working tree status of a Git repository. The status can signify if there are any uncommitted changes.
     *
     * @return [String] The current working tree status.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun status() = gitCommand(listOf("status", "--porcelain"))

    /**
     * Create a Git commit with the given commit message. Any modified or deleted files will also be staged and
     * included in the commit.
     *
     * @param [message] The commit message to use.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commit(message: String) = gitCommand(listOf("commit", "-a", "-m", message, "--quiet"))

    /**
     * Create a Git commit with the given commit message. Any new, modified or deleted files will also be staged and
     * included in the commit.
     *
     * @param [message] The commit message to use.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commitAllChanges(message: String) =
        gitCommand(listOf("add", "--all", "&&", "git", "commit", "-a", "-m", message, "--quiet"))

    /**
     * Push changes to a Git repository.
     *
     * @param [remote] The remote to push to, optional.
     * @param [branch] The branch to push, optional.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun push(remote: String? = null, branch: String? = null): String {
        val arguments = mutableListOf("push")
        remote?.let { arguments.add(it) }
        branch?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    /**
     * Pull changes for a Git repository.
     *
     * @param [remote] The remote to pull from, optional.
     * @param [branch] The branch to pull, optional.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun pull(remote: String? = null, branch: String? = null): String {
        val arguments = mutableListOf("pull")
        remote?.let { arguments.add(it) }
        branch?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    /**
     * Checkout a given Git branch.
     *
     * @param [branch] The branch to checkout.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun checkout(branch: String) =
        gitCommand(listOf("checkout", branch, "--quiet"))

    /**
     * Clone a Git repository at a given URL.
     *
     * @param [repositoryUrl] The URL of the Git repository to clone.
     * @param [destination] The path to create the clone at.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun clone(repositoryUrl: URL, destination: File? = null) =
        clone(repositoryUrl.toString(), destination?.toString())

    /**
     * Clone a Git repository at a given URL.
     *
     * @param [repositoryUrl] The URL of the Git repository to clone.
     * @param [destination] The path to create the clone at.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun clone(repositoryUrl: String, destination: String? = null): String {
        val arguments = mutableListOf("clone", repositoryUrl)
        destination?.let { arguments.add(it) }
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    /**
     * Tag a Git commit.
     *
     * @param [tagName] The name of the tag to add.
     * @param [message] The message to use in the tag.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun addTag(tagName: String, message: String) =
        gitCommand(listOf("tag", "-a", tagName, "-m", message))

    /**
     * Push the given Git tag.
     *
     * @param [tagName] The name of the tag to push.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun pushTag(tagName: String) = gitCommand(listOf("push", "origin", tagName))

    /**
     * Get the current Git branch name.
     *
     * @return [String] The current branch name.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun currentBranch() = gitCommand(listOf("rev-parse", "--abbrev-ref", "HEAD"))

    private fun gitCommand(arguments: List<String>) =
        shell.command("git", arguments)
}
