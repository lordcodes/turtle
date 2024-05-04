plugins {
    kotlin("jvm")
    id("org.jmailen.kotlinter")
}

dependencies {
    implementation(project(":turtle"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

kotlinter {
    failBuildWhenCannotAutoFormat = true
    reporters = arrayOf("checkstyle", "html")
}
