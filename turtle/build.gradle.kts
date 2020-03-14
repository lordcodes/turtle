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

    userOrg = Turtle.BINTRAY_USER
    repoName = Turtle.BINTRAY_REPOSITORY

    groupId = Turtle.GROUP_ID
    artifactId = Turtle.ARTIFACT_ID
    publishVersion = Turtle.VERSION_NAME

    desc = Turtle.DESCRIPTION
    setLicences(Turtle.LICENSE)
    website = Turtle.WEBSITE
    issueTracker = Turtle.ISSUE_TRACKER
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
                    id.set(Turtle.DEVELOPER_USER)
                    name.set(Turtle.DEVELOPER_NAME)
                }
            }
        }
    }
}
