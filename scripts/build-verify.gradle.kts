tasks {
    runCodeFormatting()
    runChecks()
}

fun TaskContainerScope.runCodeFormatting() {
    val tasks = rootProject.getTasksByName("formatKotlin", true)
    register("lcformat") {
        dependsOn(tasks)
        group = "lordcodes"
    }
}

fun TaskContainerScope.runChecks() {
    val tasks = rootProject.getTasksByName("lintKotlin", true)
    register("lcchecks") {
        dependsOn(tasks, "detekt")
        group = "lordcodes"
    }
}
