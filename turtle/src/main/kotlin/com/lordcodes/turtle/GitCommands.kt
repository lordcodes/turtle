package com.lordcodes.turtle

import java.io.File
import java.net.URL

/**
 * Commands that deal with Git.
 */
class GitCommands internal constructor(
    private val shell: ShellScript,
) {
    /**
     * Initialize a Git repository.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun gitInit(): String = gitCommand(listOf("init"))

    /**
     * Get the working tree status of a Git repository. The status can signify if there are any uncommitted changes.
     *
     * @return [String] The current working tree status.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun status(): String = gitCommand(listOf("status", "--porcelain"))

    /**
     * Stage any new, modified or deleted files, to be included in the next commit.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun addAll(): String = gitCommand(listOf("add", "--all"))

    /**
     * Create a Git commit with the given commit [message]. Any modified or deleted files will also be staged and
     * included in the commit.
     *
     * @param [message] The commit message to use.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commit(message: String): String = gitCommand(listOf("commit", "-a", "-m", message, "--quiet"))

    /**
     * Create a Git commit with the given commit [message]. Any new, modified or deleted files will also be staged and
     * included in the commit.
     *
     * @param [message] The commit message to use.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun commitAllChanges(message: String): String {
        addAll()
        return commit(message)
    }

    /**
     * Push changes to a Git repository, optionally specifying the [remote] to push to and the [branch] to push.
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
     * Push changes at HEAD to the origin remote on a Git repository.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun pushToOrigin(): String = push(remote = "origin", branch = "HEAD")

    /**
     * Pull changes for a Git repository, optionally specifying the [remote] to pull from and the [branch] to pull.
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
     * Check out a given Git [branch], optionally specifying whether to create it if it doesn't exist using
     * [createIfNecessary].
     *
     * @param [branch] The branch to check out.
     * @param [createIfNecessary] Whether to create the branch if not found.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun checkout(branch: String, createIfNecessary: Boolean = true): String {
        val arguments = mutableListOf("checkout")
        if (createIfNecessary) {
            arguments.add("-B")
        }
        arguments.add(branch)
        arguments.add("--quiet")
        return gitCommand(arguments)
    }

    /**
     * Clone a Git repository at a given [repositoryUrl], to [destination].
     *
     * @param [repositoryUrl] The URL of the Git repository to clone.
     * @param [destination] The path to create the clone at.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun clone(repositoryUrl: URL, destination: File? = null): String =
        clone(repositoryUrl.toString(), destination?.toString())

    /**
     * Clone a Git repository at a given [repositoryUrl] to [destination].
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
     * Tag a Git commit with [tagName] and [message].
     *
     * @param [tagName] The name of the tag to add.
     * @param [message] The message to use in the tag.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun addTag(tagName: String, message: String): String = gitCommand(listOf("tag", "-a", tagName, "-m", message))

    /**
     * Push the given Git [tagName] to origin.
     *
     * @param [tagName] The name of the tag to push.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun pushTag(tagName: String): String = gitCommand(listOf("push", "origin", tagName))

    /**
     * Get the current Git branch name.
     *
     * @return [String] The current branch name.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun currentBranch(): String = gitCommand(listOf("rev-parse", "--abbrev-ref", "HEAD"))

    /**
     * Get the current Git commit.
     *
     * @return [String] The current commit.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun currentCommit(): String = gitCommand(listOf("rev-parse", "--verify", "HEAD"))

    /**
     * Get the current Git commit author email.
     *
     * @return [String] The current commit author email.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun currentCommitAuthorEmail(): String = gitCommand(listOf("--no-pager", "show", "-s", "--format=%ae"))

    /**
     * Get the current Git commit author name.
     *
     * @return [String] The current commit author name.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun currentCommitAuthorName(): String = gitCommand(listOf("--no-pager", "show", "-s", "--format=%an"))

    /**
     * Run a Git command with the specified [arguments].
     *
     * @param [arguments] The arguments to pass to the Git command.
     *
     * @return [String] The output of running the command.
     *
     * @throws [ShellFailedException] There was an issue running the command.
     * @throws [ShellRunException] Running the command produced error output.
     */
    fun gitCommand(arguments: List<String>): String = shell.command("git", arguments)
}
