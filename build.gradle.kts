import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    base
    kotlin("jvm") version "1.9.23" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.5"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "11.6.1"
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
    }

    tasks.withType<Test>().configureEach {
        reports {
            html.required.set(true)
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        version.set("0.49.1")
        reporters {
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.HTML)
        }
        filter {
            include("**/src/**/kotlin/**")
        }
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
    }
}

detekt {
    source.from(
        "$projectDir/turtle/src/main/kotlin",
        "$projectDir/turtle/src/test/kotlin",
        "$projectDir/buildSrc/src/main/kotlin"
    )
    parallel = true
    config.from("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

apply(from = "scripts/build-verify.gradle.kts")
