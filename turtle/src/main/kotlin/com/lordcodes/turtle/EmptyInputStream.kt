package com.lordcodes.turtle

import java.io.InputStream

internal class EmptyInputStream : InputStream() {
    override fun available(): Int = 0

    override fun read(): Int = -1

    override fun read(bytes: ByteArray, offset: Int, length: Int): Int {
        if (length == 0) {
            return 0
        }
        return -1
    }

    override fun skip(n: Long): Long = 0L

    override fun close() {}
}
