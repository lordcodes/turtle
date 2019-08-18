import com.lordcodes.turtle.hooks.InstallGitHooksTask
import com.lordcodes.turtle.hooks.UninstallGitHooksTask

tasks {
    register<InstallGitHooksTask>("lcGitHooksInstall") {
        group = "lordcodes"
    }
    register<UninstallGitHooksTask>("lcGitHooksUninstall") {
        group = "lordcodes"
    }
}
