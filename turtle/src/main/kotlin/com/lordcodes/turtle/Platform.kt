package com.lordcodes.turtle

/**
 * The platform commands are being executed on.
 */
enum class Platform {
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
        internal fun fromSystem(): Platform {
            val osName = System.getProperty("os.name")
            return when {
                osName.contains("Mac") -> MAC
                osName.contains("Windows") -> WINDOWS
                else -> LINUX
            }
        }
    }
}
