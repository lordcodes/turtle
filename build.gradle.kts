import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(Plugins.bintrayReleaseVersioned)
    }
}

plugins {
    base
    kotlin("jvm") version Versions.kotlin apply false
    id(Plugins.detekt) version (Versions.detekt)
    id(Plugins.gradleVersions) version Versions.versionsPlugin
    id(Plugins.ktlint) version (Versions.ktlintPlugin)
    id(Plugins.ktlintIdea) version (Versions.ktlintPlugin)
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
    apply(plugin = Plugins.ktlint)

    ktlint {
        version.set(Versions.ktlint)
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
    toolVersion = Versions.detekt
    input = files(
        "$projectDir/turtle/src/main/kotlin",
        "$projectDir/turtle/src/test/kotlin",
        "$projectDir/buildSrc/src/main/kotlin"
    )
    parallel = true
    config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

apply(from = "scripts/project-setup.gradle.kts")
apply(from = "scripts/build-verify.gradle.kts")
