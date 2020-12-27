@file:Suppress("UnstableApiUsage")

import com.novoda.gradle.release.PublishExtension
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
    id(Plugins.dokka) version Versions.dokka
}

apply(plugin = Plugins.ktlint)
apply(plugin = Plugins.ktlintIdea)

dependencies {
    implementation(Libs.kotlinStdlib)

    testImplementation(TestLibs.assertJCore)
    testImplementation(TestLibs.junit5Api)
    testRuntimeOnly(TestLibs.junit5Runtime)
    testImplementation(TestLibs.mockk)
}

val dokka by tasks.getting(DokkaTask::class) {
    outputDirectory = "$buildDir/docs/dokka"

    configuration {
        jdkVersion = 8

        includes = listOf("module.md")

        sourceLink {
            path = "./"
            url = "https://github.com/lordcodes/turtle/blob/master/"
            lineSuffix = "#L"
        }
    }
}

apply(plugin = Plugins.bintrayRelease)

configure<PublishExtension> {
    bintrayUser = propertyOrEmpty("Turtle_Bintray_User")
    bintrayKey = propertyOrEmpty("Turtle_Bintray_ApiKey")

    userOrg = "lordcodes"
    repoName = "maven"

    groupId = "com.lordcodes.turtle"
    artifactId = "turtle"
    publishVersion = Turtle.VERSION_NAME

    desc = Turtle.DESCRIPTION
    setLicences("Apache-2.0")
    website = Turtle.WEBSITE
    issueTracker = "https://github.com/lordcodes/turtle/issues"
    repository = Turtle.SOURCE_CONTROL
}

if (project.isPublishing()) {
    apply(plugin = "maven")

    gradle.taskGraph.whenReady {
        tasks.withType<GenerateMavenPom>().configureEach {
            pom.description.set(Turtle.DESCRIPTION)
            pom.packaging = "jar"
            pom.url.set(Turtle.WEBSITE)

            pom.scm {
                url.set(Turtle.SOURCE_CONTROL)
                connection.set(Turtle.SOURCE_CONTROL)
                developerConnection.set(Turtle.SOURCE_CONTROL)
            }

            pom.licenses {
                license {
                    name.set("The Apache Software License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("repo")
                }
            }

            pom.developers {
                developer {
                    id.set("lordcodes")
                    name.set("Andrew Lord")
                }
            }
        }
    }
}

object Turtle {
    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 2
    private const val VERSION_PATCH = 0

    const val VERSION_NAME = "${VERSION_MAJOR}.${VERSION_MINOR}.${VERSION_PATCH}"
    const val VERSION_CODE = VERSION_MAJOR * 100_000 + VERSION_MINOR * 100 + VERSION_PATCH

    const val DESCRIPTION = "Run shell commands from a Kotlin script or application with ease \uD83D\uDC22"
    const val WEBSITE = "https://github.com/lordcodes/turtle"
    const val SOURCE_CONTROL = "https://github.com/lordcodes/turtle.git"
}
