package com.lordcodes.turtle.specs

import java.io.File
import java.net.URL

/** Type-safe lambda for adding git command options like --message **/
typealias GitOptionsLambda = GitOptions.() -> List<CommandOption>

/** Type-safe wrapper for building git commands **/
@Suppress("UndocumentedPublicFunction")
object GitSpec {
    const val git = "git"

    val currentFolder = File(".")

    fun init(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(git, listOf("init") + GitOptions.longOptions())

    fun status(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(git, listOf("status", "--porcelain") + GitOptions.longOptions())

    fun add(
        files: List<File> = emptyList(),
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(git, listOf("add") + GitOptions.longOptions() + files)

    fun commit(
        message: String,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(
        executable = git,
        typeUnsafeArgs = listOf("commit") + (listOf(GitOptions.message.withValue(message)) + GitOptions.longOptions())
    )

    fun log(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(
        executable = git,
        typeUnsafeArgs = listOf("log") + GitOptions.longOptions()

    )

    fun show(
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(
        executable = git,
        typeUnsafeArgs = listOf("show") + GitOptions.longOptions()

    )

    fun checkout(
        branch: GitBranch,
        createIfNecessary: Boolean = true,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "checkout",
            "-B".takeIf { createIfNecessary },
            branch
        ) + GitOptions.longOptions()

    )

    fun currentBranch(): Command =
        command(git, listOf("rev-parse", "--abbrev-ref", "HEAD"))

    fun currentCommit(): Command =
        command(git, listOf("rev-parse", "--verify", "HEAD"))

    fun createBranch(
        branch: GitBranch,
        startPoint: GitTreeIsh? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        git,
        listOf(
            "branch",
            branch,
            startPoint,
        ) + GitOptions.longOptions()
    )

    fun addTag(
        tag: GitTag,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "tag",
            "-a", tag,
            "-m", tag.message
        ) + GitOptions.longOptions()

    )

    fun clone(
        url: URL,
        destination: File? = null,
        longOptions: GitOptionsLambda = NoOptions
    ): Command = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "clone",
            url,
            destination
        ) + GitOptions.longOptions()

    )

    fun push(
        remote: GitRemote? = null,
        localBranch: GitBranch? = null,
        remoteBranch: GitBranch? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "push",
            remote,
            when {
                remote == null -> null
                localBranch == null -> null
                remoteBranch == null -> localBranch
                else -> "${localBranch.name}:${remoteBranch.name}"
            }
        ) + GitOptions.longOptions()

    )

    fun pull(
        remote: GitRemote? = null,
        remoteBranch: GitBranch? = null,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "pull",
            remote,
            remoteBranch.takeIf { remote != null }
        ) + GitOptions.longOptions()
    )

    fun configSet(
        key: String,
        value: String,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        executable = git,
        typeUnsafeArgs = listOfNotNull(
            "config",
            key,
            value,
        ) + GitOptions.longOptions()

    )

    fun getRemotes(): Command =
        command(git, listOf("remote", "-v"))

    fun addRemote(
        remote: GitRemote,
        longOptions: GitOptionsLambda = NoOptions
    ) = command(
        git,
        listOf(
            "remote",
            "add",
            remote,
            requireNotNull(remote.url),
        ) + GitOptions.longOptions()
    )

    fun fetch(
        remote: GitRemote? = null,
        longOptions: GitOptionsLambda = NoOptions,
    ) = command(
        executable = git,
        typeUnsafeArgs = listOf(
            "fetch",
            remote,
        ) + GitOptions.longOptions()
    )
}

/** A git remote with its URL **/
data class GitRemote(val name: String, val url: URL? = null) : HasSingleCommandArgument(name)

/** Either a [GitBranch] or a [GitTag] or a [GitHash] */
interface GitTreeIsh : HasCommandArguments

/** A git branch */
data class GitBranch(val name: String) : GitTreeIsh, HasSingleCommandArgument(name)

/** A git tag */
data class GitTag(val name: String, val message: String) : GitTreeIsh, HasSingleCommandArgument(name)

/** A git hash */
data class GitHash(val name: String) : GitTreeIsh, HasSingleCommandArgument(name)

/**
 * /**
 * Names of the options were found by unsing zsh auto-completion:
 * $ git clone --<TAB>
 * $ git branch --<TAB>
 * ...
*/
 */
@Suppress("ObjectPropertyNaming")
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
