import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
}

plugins {
    base
    kotlin("jvm") version "1.5.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.16.0"
    id("com.github.ben-manes.versions") version "0.38.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.0.0"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    tasks.withType<Test>().configureEach {
        reports {
            @Suppress("UnstableApiUsage")
            html.isEnabled = true
        }
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        version.set("0.40.0")
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
    input = files(
        "$projectDir/turtle/src/main/kotlin",
        "$projectDir/turtle/src/test/kotlin",
        "$projectDir/buildSrc/src/main/kotlin"
    )
    parallel = true
    config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

apply(from = "scripts/build-verify.gradle.kts")
