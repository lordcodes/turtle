import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm")
    id(Plugins.dokka)
}

dependencies {
    implementation(Libs.kotlinStdlib)

    testImplementation(TestLibs.assertJCore)
    testImplementation(TestLibs.junit5Api)
    testRuntimeOnly(TestLibs.junit5Runtime)
    testImplementation(TestLibs.mockk)
}

val dokka by tasks.getting(DokkaTask::class) {
    outputDirectory = "$buildDir/docs/dokka"
    jdkVersion = 8

    linkMapping {
        dir = "./"
        url = "https://github.com/lordcodes/turtle/blob/master/"
        suffix = "#L"
    }
}
