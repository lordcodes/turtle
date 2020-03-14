@file:Suppress("InvalidPackageDeclaration")

object Plugins {
    const val bintrayRelease = "com.novoda.bintray-release"
    const val bintrayReleaseVersioned = "com.novoda:bintray-release:${Versions.bintrayRelease}"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val dokka = "org.jetbrains.dokka"
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
    const val junit5Runtime = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
