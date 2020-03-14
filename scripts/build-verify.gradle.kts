tasks {
    runCodeFormatting()
    runChecks()
}

fun TaskContainerScope.runCodeFormatting() {
    val tasks = rootProject.getTasksByName("ktlintFormat", true)
    register("lcCodeFormat") {
        dependsOn(tasks)
        group = "lordcodes"
    }
}

fun TaskContainerScope.runChecks() {
    val tasks = rootProject.getTasksByName("ktlintCheck", true)
    register("lcChecks") {
        dependsOn(tasks, "detekt")
        group = "lordcodes"
    }
}
