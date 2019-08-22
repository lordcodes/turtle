package com.lordcodes.turtle.sample

import com.lordcodes.turtle.shellRun

/**
 * Run the sample
 */
fun main() {
    shellRun {
        command("mkdir Test")
        command("mkdir Test2")
        command("rm -rf BLAH")
        command("rm -rf Test")
    }
}
