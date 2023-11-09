package com.lordcodes.turtle

/**
 * An Operating System.
 */
enum class OperatingSystem {
    LINUX,
    MAC,
    WINDOWS,
    ;

    override fun toString(): String = when (this) {
        LINUX -> "Linux"
        MAC -> "Mac"
        WINDOWS -> "Windows"
    }

    companion object {
        internal fun fromSystem(): OperatingSystem {
            val osName = System.getProperty("os.name")
            return when {
                osName.contains("Mac") -> MAC
                osName.contains("Windows") -> WINDOWS
                else -> LINUX
            }
        }
    }
}
