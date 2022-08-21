@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.specs

import java.io.File

/** Type-safe builder for creating files-related commands **/
object FileSpec {

    /** Build a ls command **/
    fun ls(
        files: List<File> = listOf(File(".")),
        args: List<Any> = emptyList(),
        commandOptions: LsOptions.() -> List<CommandOption> = { emptyList() }
    ): Command = command("ls", args + LsOptions.commandOptions() + files)
}

/**
 * Names of the options were found by unsing zsh auto-completion:
 * $ ls --<TAB>
...
 */
@Suppress("ObjectPropertyNaming")
object LsOptions : CommandOptions() {
    val all by optionsMap
    val blocks by optionsMap
    val colour by optionsMap
    val git by optionsMap
    val version by optionsMap

// ...
}
