@file:Suppress("UnstableApiUsage")

import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.9.10"
    id("com.vanniktech.maven.publish") version "0.26.0"
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.21")
    testImplementation("io.mockk:mockk:1.13.8")
}

tasks.test {
    useJUnitPlatform()
    environment("LANG", "en_US.UTF8")
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

signing {
    useInMemoryPgpKeys(propertyOrEmpty("Turtle_Signing_Key"), propertyOrEmpty("Turtle_Signing_Password"))
}

fun Project.propertyOrEmpty(name: String): String {
    val property = findProperty(name) as String?
    return property ?: environmentVariable(name)
}

fun environmentVariable(name: String) = System.getenv(name) ?: ""
