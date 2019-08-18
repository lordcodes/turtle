plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":library"))
    implementation(Libs.kotlinStdlib)
}
