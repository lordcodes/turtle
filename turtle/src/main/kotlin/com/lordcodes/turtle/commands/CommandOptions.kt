@file:Suppress("UndocumentedPublicClass", "UndocumentedPublicFunction")
package com.lordcodes.turtle.commands

abstract class CommandOptions {
    val optionsMap: Map<String, LongOption> =
        emptyMap<String, LongOption>().withDefault { key -> LongOption("--$key", null) }
}
