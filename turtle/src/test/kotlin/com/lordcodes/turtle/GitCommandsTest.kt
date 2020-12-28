package com.lordcodes.turtle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class GitCommandsTest {
    // push - create remote that is a local repo and push to it, check changes in other repo
    // pull - pull changes from the local remote
    // clone - clone from local remote
    // pushTag - push to local remote and check it is there in the remote repo

    @TempDir
    lateinit var temporaryFolder: File

    private val shell by lazy { ShellScript(temporaryFolder) }
    private val git by lazy { GitCommands(shell) }

    @Test
    fun gitInit() {
        git.gitInit()

        assertThat(File(temporaryFolder, ".git").isDirectory).isTrue()
    }

    @Test
    fun status() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()

        val output = git.status()

        assertThat(output).isEqualTo("?? ${newFile.name}")
    }

    @Test
    fun addAll() {
        initUsableRepository()
        val modifiedFile = File(temporaryFolder, "testFile.txt")
        modifiedFile.createNewFile()
        shell.command("git", listOf("add", "testFile.txt"))
        shell.command("git", listOf("commit", "-a", "-m", "Add testFile", "--quiet"))
        modifiedFile.writeText("changes")
        val newFile = File(temporaryFolder, "anotherFile.txt")
        newFile.createNewFile()

        git.addAll()

        val status = git.status()
        assertThat(status).isEqualTo(
            """
            A  ${newFile.name}
            M  ${modifiedFile.name}
            """.trimIndent()
        )
    }

    @Test
    fun commit() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()
        git.addAll()

        git.commit("Add testFile")

        val message = shell.command("git", listOf("log", "-1", "--pretty=%B"))
        assertThat(message).isEqualTo("Add testFile")
    }

    @Test
    fun commit_includesChanges_doesNotIncludeNewFiles() {
        initUsableRepository()
        val modifiedFile = File(temporaryFolder, "testFile.txt")
        modifiedFile.createNewFile()
        git.addAll()
        git.commit("Add testFile")
        modifiedFile.writeText("changes")
        val newFile = File(temporaryFolder, "anotherFile.txt")
        newFile.createNewFile()

        git.commit("Change testFile")

        val lastCommit = shell.command("git", listOf("show"))
        assertThat(lastCommit).contains("Change testFile")
        assertThat(lastCommit).contains("testFile.txt")
        assertThat(lastCommit).doesNotContain("anotherFile.txt")
    }

    @Test
    fun commitAllChanges_includesChangesAndNewFiles() {
        initUsableRepository()
        val modifiedFile = File(temporaryFolder, "testFile.txt")
        modifiedFile.createNewFile()
        git.addAll()
        git.commit("Add testFile")
        modifiedFile.writeText("changes")
        val newFile = File(temporaryFolder, "anotherFile.txt")
        newFile.createNewFile()

        git.commitAllChanges("Change testFile")

        val lastCommit = shell.command("git", listOf("show"))
        assertThat(lastCommit).contains("Change testFile")
        assertThat(lastCommit).contains("testFile.txt")
        assertThat(lastCommit).contains("anotherFile.txt")
    }

    @Test
    fun checkout_createIfNecessary() {
        initUsableRepository()

        git.checkout("newBranch")

        assertThat(git.currentBranch()).isEqualTo("newBranch")
    }

    @Test
    fun checkout_doNotCreateIfNecessary_givenBranchDoesNotExist() {
        initUsableRepository()

        val exception = assertThrows<ShellRunException> {
            git.checkout("newBranch", createIfNecessary = false)
        }

        assertThat(exception.message).contains("pathspec 'newBranch' did not match")
    }

    @Test
    fun checkout_doNotCreateIfNecessary_givenBranchExists() {
        initUsableRepository()
        shell.command("git", listOf("branch", "newBranch"))

        git.checkout("newBranch", createIfNecessary = false)

        assertThat(git.currentBranch()).isEqualTo("newBranch")
    }

    @Test
    fun addTag() {
        initUsableRepository()

        git.addTag(tagName = "v1.1.0", message = "Release v1.1.0")

        val lastTag = shell.command("git", listOf("describe", "--tags", "--abbrev=0"))
        assertThat(lastTag).isEqualTo("v1.1.0")
    }

    @Test
    fun currentBranch() {
        initUsableRepository()
        git.checkout("newBranch")

        val currentBranch = git.currentBranch()

        assertThat(currentBranch).isEqualTo("newBranch")
        assertThat(currentBranch).isEqualTo(shell.command("git", listOf("rev-parse", "--abbrev-ref", "HEAD")))
    }

    private fun initUsableRepository() {
        git.gitInit()
        shell.command("git", listOf("commit", "--allow-empty", "-n", "-m", "Initial commit", "--quiet"))
    }
}
