package com.lordcodes.turtle

import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

        assertTrue(File(temporaryFolder, ".git").isDirectory)
    }

    @Test
    fun status() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()

        val output = git.status()

        assertEquals(output, "?? ${newFile.name}")
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
        assertEquals(
            status,
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
        assertEquals(message, "Add testFile")
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
        assertContains(lastCommit, "Change testFile")
        assertContains(lastCommit, "testFile.txt")
        assertFalse(lastCommit.contains("anotherFile.txt"))
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
        assertContains(lastCommit, "Change testFile")
        assertContains(lastCommit, "testFile.txt")
        assertContains(lastCommit, "anotherFile.txt")
    }

    @Test
    fun checkout_createIfNecessary() {
        initUsableRepository()

        git.checkout("newBranch")

        assertEquals(git.currentBranch(), "newBranch")
    }

    @Test
    fun checkout_doNotCreateIfNecessary_givenBranchDoesNotExist() {
        initUsableRepository()

        val exception = assertThrows<ShellRunException> {
            git.checkout("newBranch", createIfNecessary = false)
        }

        val message = exception.message ?: ""
        assertContains(message, "pathspec 'newBranch' did not match")
    }

    @Test
    fun checkout_doNotCreateIfNecessary_givenBranchExists() {
        initUsableRepository()
        shell.command("git", listOf("branch", "newBranch"))

        git.checkout("newBranch", createIfNecessary = false)

        assertEquals(git.currentBranch(), "newBranch")
    }

    @Test
    fun addTag() {
        initUsableRepository()

        git.addTag(tagName = "v1.1.0", message = "Release v1.1.0")

        val lastTag = shell.command("git", listOf("describe", "--tags", "--abbrev=0"))
        assertEquals(lastTag, "v1.1.0")
    }

    @Test
    fun currentBranch() {
        initUsableRepository()
        git.checkout("newBranch")

        val currentBranch = git.currentBranch()

        assertEquals(currentBranch, "newBranch")
        assertEquals(currentBranch, shell.command("git", listOf("rev-parse", "--abbrev-ref", "HEAD")))
    }

    @Test
    fun currentCommit() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()
        git.addAll()
        git.commit("Add testFile")

        val currentCommit = git.currentCommit()

        assertEquals(currentCommit, shell.command("git", listOf("rev-parse", "--verify", "HEAD")))
    }

    @Test
    fun currentCommitAuthorEmail() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()
        git.addAll()
        git.commit("Add testFile")

        val email = git.currentCommitAuthorEmail()

        assertEquals(email, shell.command("git", listOf("--no-pager", "show", "-s", "--format=%ae")))
    }

    @Test
    fun currentCommitAuthorName() {
        initUsableRepository()
        val newFile = File(temporaryFolder, "testFile.txt")
        newFile.createNewFile()
        git.addAll()
        git.commit("Add testFile")

        val email = git.currentCommitAuthorName()

        assertEquals(email, shell.command("git", listOf("--no-pager", "show", "-s", "--format=%an")))
    }

    private fun initUsableRepository() {
        git.gitInit()
        shell.command("git", listOf("commit", "--allow-empty", "-n", "-m", "Initial commit", "--quiet"))
    }
}
