package com.lordcodes.turtle

enum class OperatingSystem {
    LINUX,
    MAC,
    WINDOWS;

    override fun toString(): String = when (this) {
        LINUX -> "Linux"
        MAC -> "Mac"
        WINDOWS -> "Windows"
    }

    companion object {
        fun fromSystem(): OperatingSystem {
            val osName = System.getProperty("os.name")
            return when {
                osName.contains("Mac") -> MAC
                osName.contains("Windows") -> WINDOWS
                else -> LINUX
            }
        }
    }
}
