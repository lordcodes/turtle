package com.lordcodes.turtle.commands

import com.lordcodes.turtle.specs.Command
import com.lordcodes.turtle.specs.GitBranch
import com.lordcodes.turtle.specs.GitRemote
import com.lordcodes.turtle.specs.GitSpec
import com.lordcodes.turtle.specs.GitTag
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL
import kotlin.test.assertEquals

@Suppress("LocalVariableName")
internal class GitSpecTest {
    val git = GitSpec
    private infix fun List<Command>.shouldBe(message: String) =
        assertEquals(message, joinToString("\n"))

    val expectedGitCommands = """
git init
git add .
git add --all
git status --porcelain
git commit --message 'Add testFile' --all --quiet
git log --oneline --pretty %B
git show --oneline --summary
git checkout -B newBranch --quiet
git rev-parse --abbrev-ref HEAD
git rev-parse --verify HEAD
git tag -a v1.1.0 -m 'Release v1.1.0'
git clone https://github.com/jmfayard/refreshVersions.git destination --recursive --branch newBranch
git push
git pull --rebase
git push remote_name local_branch:remote_branch
git pull remote_name remote_branch
git config user.name 'John Doe' --global
git branch newBranch v1.1.0 --set-upstream
git remote add remote_name https://github.com/jmfayard/refreshVersions.git
git remote -v
git fetch
git fetch remote_name --prune --tags --all
            """

    @Test
    fun testGitCommands() {
        val newBranch = GitBranch("newBranch")
        val localBranch = GitBranch("local_branch")
        val remoteBranch = GitBranch("remote_branch")
        val newTag = GitTag(name = "v1.1.0", message = "Release v1.1.0")
        val remoteGitUrl = URL("https://github.com/jmfayard/refreshVersions.git")
        val remote = GitRemote("remote_name", remoteGitUrl)

        val commands: List<Command> = listOf(
            git.init(),
            git.add(listOf(git.currentFolder)),
            git.add { listOf(all) },
            git.status(),
            git.commit("Add testFile") {
                listOf(all, quiet)
            },
            git.log() {
                listOf(oneline, pretty.withValue("%B"))
            },
            git.show() {
                listOf(oneline, summary)
            },
            git.checkout(newBranch, createIfNecessary = true) {
                listOf(quiet)
            },
            git.currentBranch(),
            git.currentCommit(),
            git.addTag(newTag),
            git.clone(remoteGitUrl, File("destination")) {
                listOf(recursive, branch.withValue(newBranch))
            },
            git.push(),
            git.pull { listOf(rebase) },
            git.push(remote, localBranch, remoteBranch),
            git.pull(remote, remoteBranch),
            git.configSet("user.name", "John Doe") {
                listOf(global)
            },
            git.createBranch(newBranch, newTag) {
                listOf(`set-upstream`)
            },
            git.addRemote(remote),
            git.getRemotes(),
            git.fetch(),
            git.fetch(remote) {
                listOf(prune, tags, all)
            },
        )
        commands shouldBe expectedGitCommands.trimIndent()
    }
}
