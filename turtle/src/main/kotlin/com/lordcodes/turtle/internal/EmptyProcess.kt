package com.lordcodes.turtle.internal

import java.io.InputStream
import java.io.OutputStream

internal class EmptyProcess : Process() {
    override fun getOutputStream(): OutputStream = EmptyOutputStream()

    override fun getInputStream(): InputStream = EmptyInputStream()

    override fun getErrorStream(): InputStream = EmptyInputStream()

    override fun waitFor(): Int = 0

    override fun exitValue(): Int = 0

    override fun destroy() {}
}
