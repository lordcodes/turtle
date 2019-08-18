package com.lordcodes.turtle

import java.io.File

object ShellLocation {
    val HOME = File(System.getProperty("user.home"))
    val CURRENT_WORKING = File(System.getProperty("user.dir"))
}