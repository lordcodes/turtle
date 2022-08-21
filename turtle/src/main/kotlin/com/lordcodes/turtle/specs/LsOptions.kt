@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.specs

@Suppress("ObjectPropertyNaming")
object LsOptions : CommandOptions() {
    /* Names of the options were found by unsing zsh auto-completion:
     * $ ls --<TAB>
    ...
     */
    val accessed by optionsMap
    val across by optionsMap
    val all by optionsMap
    val binary by optionsMap
    val blocks by optionsMap
    val bytes by optionsMap
    val changed by optionsMap
    val classify by optionsMap
    val color by optionsMap
    val colour by optionsMap
    val `color-scale` by optionsMap
    val `colour-scale` by optionsMap
    val created by optionsMap
    val extended by optionsMap
    val git by optionsMap
    val `git-ignore` by optionsMap
    val grid by optionsMap
    val group by optionsMap
    val `group-directories-first` by optionsMap
    val header by optionsMap
    val help by optionsMap
    val icons by optionsMap
    val `ignore-glob` by optionsMap
    val inode by optionsMap
    val level by optionsMap
    val links by optionsMap
    val `list-dirs` by optionsMap
    val long by optionsMap
    val modified by optionsMap
    val `no-filesize` by optionsMap
    val `no-icons` by optionsMap
    val `no-permissions` by optionsMap
    val `no-time` by optionsMap
    val `no-user` by optionsMap
    val numeric by optionsMap
    val `octal-permissions` by optionsMap
    val oneline by optionsMap
    val `only-dirs` by optionsMap
    val recurse by optionsMap
    val reverse by optionsMap
    val sort by optionsMap
    val time by optionsMap
    val `time-style` by optionsMap
    val tree by optionsMap
    val version by optionsMap

// ...
}
