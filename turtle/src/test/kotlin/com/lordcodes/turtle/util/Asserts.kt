package com.lordcodes.turtle.util

import org.junit.jupiter.api.Assertions as JunitAssertions

// Poor man's minimal reimplementation of https://kotest.io/docs/assertions/assertions.html

internal infix fun <T> T.shouldBe(other: T?) =
    JunitAssertions.assertEquals(other, this)

internal fun shouldThrowMessage(message: String, fn: () -> Unit) {
    JunitAssertions.assertThrows(Throwable::class.java) {
        fn()
    }.message shouldBe message
}
