package com.lordcodes.turtle.internal

import java.io.OutputStream

internal class EmptyOutputStream : OutputStream() {
    override fun write(value: Int) {}

    override fun write(bytes: ByteArray, offset: Int, length: Int) {}

    override fun close() {}
}
