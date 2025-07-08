import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    base
    kotlin("jvm") version "2.2.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("com.github.ben-manes.versions") version "0.52.0"
    id("org.jmailen.kotlinter") version "5.1.1" apply false
}

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
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
