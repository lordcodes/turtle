@file:Suppress("UnstableApiUsage")

import com.novoda.gradle.release.PublishExtension
import java.net.URL
import org.gradle.api.Project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.4.20"
    id("com.vanniktech.maven.publish") version "0.14.2"
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")

dependencies {
    implementation(enforcedPlatform(project(":meta:dependencies")))

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("com.google.truth:truth")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk")
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("docs/dokka"))

    dokkaSourceSets {
        configureEach {
            jdkVersion.set(8)

            includes.from("module.md")

            sourceLink {
                localDirectory.set(file("./"))
                remoteUrl.set(URL("https://github.com/lordcodes/turtle/blob/master/"))
                remoteLineSuffix.set("#L")
            }
        }
    }
}

publishing {
    repositories {
        withType<MavenArtifactRepository> {
            if (name == "local") {
                return@withType
            }

            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = propertyOrEmpty("Turtle_Sonatype_Nexus_Username")
                password = propertyOrEmpty("Turtle_Sonatype_Nexus_Password")
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(propertyOrEmpty("Turtle_Signing_Key"), propertyOrEmpty("Turtle_Signing_Password"))
}

apply(plugin = "com.novoda.bintray-release")

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

    const val VERSION_NAME = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"
    const val VERSION_CODE = VERSION_MAJOR * 100_000 + VERSION_MINOR * 100 + VERSION_PATCH

    const val DESCRIPTION = "Run shell commands from a Kotlin script or application with ease \uD83D\uDC22"
    const val WEBSITE = "https://github.com/lordcodes/turtle"
    const val SOURCE_CONTROL = "https://github.com/lordcodes/turtle.git"
}

fun Project.propertyOrEmpty(name: String): String {
    val property = findProperty(name) as String?
    return property ?: environmentVariable(name)
}

fun environmentVariable(name: String) = System.getenv(name) ?: ""

fun Project.isPublishing() = gradle.startParameter.taskNames.any { it.contains("bintrayUpload") }
