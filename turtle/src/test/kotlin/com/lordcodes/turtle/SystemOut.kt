package com.lordcodes.turtle

import java.io.ByteArrayOutputStream
import java.io.PrintStream

internal fun withCapturedSystemOut(testBody: (ByteArrayOutputStream) -> Unit) {
    val standardOut = System.out
    val outputStreamCaptor = ByteArrayOutputStream()
    System.setOut(PrintStream(outputStreamCaptor))

    try {
        testBody(outputStreamCaptor)
    } finally {
        System.setOut(standardOut)
    }
}
