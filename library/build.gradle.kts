plugins {
    kotlin("jvm")
}

dependencies {
    implementation(Libs.kotlinStdlib)

    testImplementation(TestLibs.assertJCore)
    testImplementation(TestLibs.junit5Api)
    testRuntimeOnly(TestLibs.junit5Runtime)
    testImplementation(TestLibs.mockk)
}
