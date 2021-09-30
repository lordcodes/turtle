@file:Suppress("UnstableApiUsage")

import java.net.URL
import org.gradle.api.Project

project.ext.set("mavenCentralUsername", propertyOrEmpty("Turtle_Sonatype_Nexus_Username"))
project.ext.set("mavenCentralPassword", propertyOrEmpty("Turtle_Sonatype_Nexus_Password"))
project.ext.set("signingInMemoryKey", propertyOrEmpty("Turtle_Signing_Key"))
project.ext.set("signingInMemoryKeyPassword", propertyOrEmpty("Turtle_Signing_Password"))

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.5.30"
    id("com.vanniktech.maven.publish") version "0.18.0"
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")
apply(plugin = "org.jlleitschuh.gradle.ktlint-idea")

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.31")

    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")
    testImplementation("io.mockk:mockk:1.12.0")
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

fun Project.propertyOrEmpty(name: String): String {
    val property = findProperty(name) as String?
    return property ?: environmentVariable(name)
}

fun environmentVariable(name: String) = System.getenv(name) ?: ""
