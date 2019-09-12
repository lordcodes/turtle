import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath(Plugins.bintrayReleaseVersioned)
    }
}

plugins {
    base
    kotlin("jvm") version Versions.kotlin apply false
    id(Plugins.gradleVersions) version Versions.versionsPlugin
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

apply(from = "scripts/project-setup.gradle.kts")
