import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        jcenter()
    }
}

plugins {
    base
    kotlin("jvm") version Versions.kotlin apply false
    id(Plugins.detekt) version Versions.detekt
    id(Plugins.dokka) version Versions.dokka apply false
    id(Plugins.gradleVersions) version Versions.versionsPlugin
    id(Plugins.ktlint) version Versions.ktlintPlugin
    id(Plugins.ktlintIdea) version Versions.ktlintPlugin
}

allprojects {
    repositories {
        jcenter()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
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
    apply(plugin = Plugins.detekt)
    apply(plugin = Plugins.ktlint)
    apply(plugin = Plugins.ktlintIdea)

    detekt {
        toolVersion = Versions.detekt
        input = files(
            "src/main/kotlin",
            "src/test/kotlin"
        )
        parallel = true
        config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
        buildUponDefaultConfig = true
    }

    ktlint {
        version.set(Versions.ktlint)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        filter {
            include("**/src/**/kotlin/**")
        }
    }
}

apply(from = "scripts/project-setup.gradle.kts")
