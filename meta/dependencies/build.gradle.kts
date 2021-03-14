plugins {
    id("java-platform")
    id("maven-publish")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:1.4.31"))

    constraints {
        junit5()
        mockk()
        truth()
    }
}

fun DependencyConstraintHandler.junit5() {
    api("org.junit.jupiter:junit-jupiter-api:5.7.1")
    api("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

fun DependencyConstraintHandler.mockk() {
    api("io.mockk:mockk:1.10.6")
}

fun DependencyConstraintHandler.truth() {
    api("com.google.truth:truth:1.1.2")
}

publishing {
    publications {
        create<MavenPublication>("turtlePlatform") {
            from(components["javaPlatform"])
        }
    }
}
