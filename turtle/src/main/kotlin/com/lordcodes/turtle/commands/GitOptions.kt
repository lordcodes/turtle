package com.lordcodes.turtle.commands

import java.io.File
import java.net.URL

typealias GitOptionsLambda = GitOptions.() -> List<LongOption>

object GitCommands {
    const val git = "git"

    val currentFolder = File(".")

    fun init(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(git, listOf("init"), GitOptions.longOptions())

    fun status(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(git, listOf("status", "--porcelain"), GitOptions.longOptions())

    fun add(
        files: List<File> = emptyList(),
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(git, listOf("add"), GitOptions.longOptions(), files)

    fun commit(
        message: String,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(
        executable = git,
        argsBeforeOptions = listOf("commit"),
        longArgs = listOf(GitOptions.message.withValue(message)) + GitOptions.longOptions()
    )

    fun log(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(
        executable = git,
        argsBeforeOptions = listOf("log"),
        longArgs = GitOptions.longOptions()
    )

    fun show(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(
        executable = git,
        argsBeforeOptions = listOf("show"),
        longArgs = GitOptions.longOptions()
    )

    fun checkout(
        branch: GitBranch,
        createIfNecessary: Boolean = true,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "checkout",
            "-B".takeIf { createIfNecessary },
            branch
        ),
        longArgs = GitOptions.longOptions()
    )

    fun currentBranch(): Command =
        command(git, "rev-parse", "--abbrev-ref", "HEAD")

    fun currentCommit(): Command =
        command(git, "rev-parse", "--verify", "HEAD")

    fun createBranch(
        branch: GitBranch,
        startPoint: GitTreeIsh? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "branch",
            branch,
            startPoint,
        ),
        longArgs = GitOptions.longOptions()
    )

    fun addTag(
        tag: GitTag,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "tag",
            "-a", tag,
            "-m", tag.message
        ),
        longArgs = GitOptions.longOptions()
    )

    fun clone(
        url: URL,
        destination: File? = null,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "clone",
            url,
            destination
        ),
        longArgs = GitOptions.longOptions()
    )

    fun push(
        remote: GitRemote? = null,
        localBranch: GitBranch? = null,
        remoteBranch: GitBranch? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "push",
            remote,
            when {
                remote == null -> null
                localBranch == null -> null
                remoteBranch == null -> localBranch
                else -> "${localBranch.arg}:${remoteBranch.arg}"
            }
        ),
        longArgs = GitOptions.longOptions()
    )

    fun pull(
        remote: GitRemote? = null,
        remoteBranch: GitBranch? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(executable = git,
        argsBeforeOptions = listOfNotNull(
            "pull",
            remote,
            remoteBranch.takeIf { remote != null }
        ),
        longArgs = GitOptions.longOptions()
    )

    fun configSet(
        key: String,
        value: String,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "config",
            key,
            value,
        ),
        longArgs = GitOptions.longOptions()
    )

    fun getRemotes(): Command =
        command(git, "remote", "-v")

    fun addRemote(
        remote: GitRemote,
        longOptions: GitOptionsLambda = NoOptions
    ) = createCommand(executable = git,
        argsBeforeOptions = listOfNotNull(
            "remote",
            "add",
            remote.arg,
            requireNotNull(remote.url) { "URL required in git.addRemote()" }
        ),
        longArgs = GitOptions.longOptions()
    )

    fun fetch(
        remote: GitRemote? = null,
        longOptions: GitOptionsLambda = NoOptions,
    ) = createCommand(
        executable = git,
        argsBeforeOptions = listOfNotNull(
            "fetch",
            remote,
        ),
        longArgs = GitOptions.longOptions()
    )
}

data class GitRemote(override val arg: String, val url: URL? = null) :
    HasCommandArgument

interface GitTreeIsh: HasCommandArgument

data class GitBranch(override val arg: String) : GitTreeIsh

data class GitTag(override val arg: String, val message: String) : GitTreeIsh

data class GitHash(override val arg: String) : GitTreeIsh

object GitOptions : CommandOptions() {

    val `set-upstream` by optionsMap
    val all by optionsMap
    val branch by optionsMap
    val delete by optionsMap
    val edit by optionsMap
    val global by optionsMap
    val help by optionsMap
    val list by optionsMap
    val local by optionsMap
    val message by optionsMap
    val oneline by optionsMap
    val pretty by optionsMap
    val prune by optionsMap
    val quiet by optionsMap
    val rebase by optionsMap
    val recursive by optionsMap
    val summary by optionsMap
    val tags by optionsMap
}
