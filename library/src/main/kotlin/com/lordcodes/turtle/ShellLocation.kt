package com.lordcodes.turtle

import java.io.File

/**
 * Useful file system locations.
 */
object ShellLocation {
    /**
     * The current user's home directory. For example on MacOS, '/Users/lordcodes/'.
     */
    val HOME = File(System.getProperty("user.home"))

    /**
     * The current working directory.
     */
    val CURRENT_WORKING = File(System.getProperty("user.dir"))
}
