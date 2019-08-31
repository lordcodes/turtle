object Turtle {
    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 1
    private const val VERSION_PATCH = 0

    const val VERSION_NAME = "${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}"
    const val VERSION_CODE = VERSION_MAJOR * 100000 + VERSION_MINOR * 100 + VERSION_PATCH

    const val GROUP_ID = "com.lordcodes.turtle"
    const val ARTIFACT_ID = "turtle"

    const val DESCRIPTION = "Run shell commands from a Kotlin script or application with ease \uD83D\uDC22"
    const val LICENSE = "Apache-2.0"
    const val WEBSITE = "https://github.com/lordcodes/turtle"
    const val ISSUE_TRACKER = "https://github.com/lordcodes/turtle/issues"
    const val SOURCE_CONTROL = "https://github.com/lordcodes/turtle.git"

    const val BINTRAY_USER = "lordcodes"
    const val BINTRAY_REPOSITORY = "maven"

    const val DEVELOPER_USER = "lordcodes"
    const val DEVELOPER_NAME = "Andrew Lord"
}
