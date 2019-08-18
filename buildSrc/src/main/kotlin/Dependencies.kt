object Plugins {
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val gradleVersions = "com.github.ben-manes.versions"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val ktlintIdea = "org.jlleitschuh.gradle.ktlint-idea"
}

object Libs {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
}

object TestLibs {
    const val assertJCore = "org.assertj:assertj-core:${Versions.assertJCore}"
    const val junit5Api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5}"
    const val junit5Parameterized = "org.junit.jupiter:junit-jupiter-params:${Versions.junit5}"
    const val junit5Runtime = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}

object Groups {
    const val kotlin = "org.jetbrains.kotlin"
}
