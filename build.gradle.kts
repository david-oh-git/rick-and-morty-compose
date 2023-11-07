

import Build_gradle.BuildTaskGroups.gitHooks
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    apply<KtlintPlugin>()
    apply<SpotlessPlugin>()

    configure<SpotlessExtension> {

        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(libs.versions.ktlint.get()).userData(mapOf("android" to "true"))
            licenseHeaderFile(
                file("../spotless/copyright.kt"),
                "^(package|object|import|interface)"
            ).updateYearWithLatest(false)

            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }

        format("xml") {
            target("**/res/**/*.xml")
            indentWithSpaces(4)
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("**/build/**/*.kts")
            licenseHeaderFile(file("../spotless/copyright.kt"), "(^(?![\\/ ]\\*).*$)")
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.versions.ktlint.get())


            trimTrailingWhitespace()
            indentWithSpaces()
            endWithNewline()
        }
    }

    configure<KtlintExtension>() {

        version.set(rootProject.libs.versions.ktlint.get())
        debug.set(true)
        verbose.set(true)
        android.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        enableExperimentalRules.set(true)
        ignoreFailures.set(false)

        reporters {
            reporter(HTML)
            reporter(CHECKSTYLE)
        }
        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
            include("**/kotlin/**")
        }
    }

    tasks.withType<org.jlleitschuh.gradle.ktlint.tasks.GenerateReportsTask> {
        reportsOutputDirectory.set(
            project.layout.buildDirectory.dir("reports/$name")
        )
    }

    tasks {

        register<Copy>("copyGitHooks") {
            description = "Copies the git hooks from scripts/git-hooks to the .git folder."
            group = gitHooks
            from("$rootDir/scripts/git-hooks/") {
                include("**/*.sh")
                rename("(.*).sh", "$1")
            }
            into("$rootDir/.git/hooks")
        }

        register<Exec>("installGitHooks") {
            description = "Installs the pre-commit git hooks from scripts/git-hooks."
            group = gitHooks
            workingDir(rootDir)
            commandLine("chmod")
            args("-R", "+x", ".git/hooks/")
            dependsOn(named("copyGitHooks"))
            onlyIf {
                isLinuxOrMacOs()
            }
            doLast {
                logger.info("Git hooks installed successfully.")
            }
        }

        register<Delete>("deleteGitHooks") {
            description = "Delete the pre-commit git hooks."
            group = gitHooks
            delete(fileTree(".git/hooks/"))
        }

        afterEvaluate {
            tasks["clean"].dependsOn(tasks.named("installGitHooks"))
        }
    }

}

object BuildTaskGroups {
    const val gitHooks = "git hooks"
}

fun isLinuxOrMacOs(): Boolean = listOf("linux", "mac os", "macos").contains(
    System.getProperty("os.name").lowercase()
)
