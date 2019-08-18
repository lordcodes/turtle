package com.lordcodes.turtle.sample

import com.lordcodes.turtle.shellRun

fun main() {
    shellRun {
        command("mkdir", listOf("TestDir"))
    }
}
