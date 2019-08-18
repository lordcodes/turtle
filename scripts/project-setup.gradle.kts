import com.lordcodes.templatekotlinjvmlibrary.hooks.InstallGitHooksTask
import com.lordcodes.templatekotlinjvmlibrary.hooks.UninstallGitHooksTask

tasks {
    register<InstallGitHooksTask>("lcGitHooksInstall") {
        group = "lordcodes"
    }
    register<UninstallGitHooksTask>("lcGitHooksUninstall") {
        group = "lordcodes"
    }
}
