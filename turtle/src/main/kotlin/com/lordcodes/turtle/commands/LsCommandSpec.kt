@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

@Suppress("ObjectPropertyNaming")
object LsCommandSpec : CommandSpec() {
    override val documentationUrl = "https://www.tutorialspoint.com/unix_commands/ls.htm"

    /* Names of the options were found by unsing zsh auto-completion:
     * $ ls -<TAB>
     */
    override val shortOptions = "1@AaBbCcdeFfGgHhikLlmnOoPpqRrSsTtUuWwx"

    /* Names of the options were found by unsing zsh auto-completion:
     * $ ls --<TAB>
    ...
     */
    val accessed by longOptionsMap
    val across by longOptionsMap
    val all by longOptionsMap
    val binary by longOptionsMap
    val blocks by longOptionsMap
    val bytes by longOptionsMap
    val changed by longOptionsMap
    val classify by longOptionsMap
    val color by longOptionsMap
    val colour by longOptionsMap
    val `color-scale` by longOptionsMap
    val `colour-scale` by longOptionsMap
    val created by longOptionsMap
    val extended by longOptionsMap
    val git by longOptionsMap
    val `git-ignore` by longOptionsMap
    val grid by longOptionsMap
    val group by longOptionsMap
    val `group-directories-first` by longOptionsMap
    val header by longOptionsMap
    val help by longOptionsMap
    val icons by longOptionsMap
    val `ignore-glob` by longOptionsMap
    val inode by longOptionsMap
    val level by longOptionsMap
    val links by longOptionsMap
    val `list-dirs` by longOptionsMap
    val long by longOptionsMap
    val modified by longOptionsMap
    val `no-filesize` by longOptionsMap
    val `no-icons` by longOptionsMap
    val `no-permissions` by longOptionsMap
    val `no-time` by longOptionsMap
    val `no-user` by longOptionsMap
    val numeric by longOptionsMap
    val `octal-permissions` by longOptionsMap
    val oneline by longOptionsMap
    val `only-dirs` by longOptionsMap
    val recurse by longOptionsMap
    val reverse by longOptionsMap
    val sort by longOptionsMap
    val time by longOptionsMap
    val `time-style` by longOptionsMap
    val tree by longOptionsMap
    val version by longOptionsMap

// ...
}
