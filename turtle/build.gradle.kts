@file:Suppress("UnstableApiUsage")

import java.net.URL

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.33.0"
    id("org.jmailen.kotlinter")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.20")
    testImplementation("io.mockk:mockk:1.14.4")
}

tasks.test {
    useJUnitPlatform()
    environment("LANG", "en_US.UTF8")
}

kotlinter {
    failBuildWhenCannotAutoFormat = true
    reporters = arrayOf("checkstyle", "html")
}

tasks.dokkaHtml.configure {
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
