/*
 * MIT License
 *
 * Copyright (c) 2024   David Osemwota.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import Build_gradle.BuildTaskGroups.gitHooks
import Build_gradle.RickAndMortyBuildTasks.COPY_GIT_HOOKS
import Build_gradle.RickAndMortyBuildTasks.DELETE_GIT_HOOKS
import Build_gradle.RickAndMortyBuildTasks.INSTALL_GIT_HOOKS
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
    alias(libs.plugins.apollo3) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
}

allprojects {

    apply<KtlintPlugin>()
    apply<SpotlessPlugin>()

    configure<SpotlessExtension> {

        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint(libs.versions.ktlint.get()).userData(mapOf("android" to "true"))
            licenseHeaderFile(
                rootProject.file("spotless/copyright.txt"),
                "^(package|object|import|interface)",
            )
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
            licenseHeaderFile(
                rootProject.file("spotless/copyright.txt"),
                "(^(?![\\/ ]\\*).*$)",
            )
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
            project.layout.buildDirectory.dir("reports/$name"),
        )
    }

    tasks {

        register<Copy>(COPY_GIT_HOOKS.taskName) {
            description = COPY_GIT_HOOKS.taskDescription
            group = COPY_GIT_HOOKS.taskGroup
            from("$rootDir/scripts/git-hooks/") {
                include("**/*.sh")
                rename("(.*).sh", "$1")
            }
            into("$rootDir/.git/hooks")
        }

        register<Exec>(INSTALL_GIT_HOOKS.taskName) {
            description = INSTALL_GIT_HOOKS.taskGroup
            group = INSTALL_GIT_HOOKS.taskGroup
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

        register<Delete>(DELETE_GIT_HOOKS.taskName) {
            description = DELETE_GIT_HOOKS.taskDescription
            group = DELETE_GIT_HOOKS.taskGroup
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

enum class RickAndMortyBuildTasks(
    val taskName: String,
    val taskDescription: String,
    val taskGroup: String,
) {
    COPY_GIT_HOOKS(
        "copyGitHooks",
        "Installs the pre-commit git hooks from scripts/git-hooks.",
        gitHooks,
    ),
    INSTALL_GIT_HOOKS(
        "installGitHooks",
        "Installs the pre-commit git hooks from scripts/git-hooks.",
        gitHooks,
    ),
    DELETE_GIT_HOOKS(
        "deleteGitHooks",
        "Delete the pre-commit git hooks.",
        gitHooks,
    ),
}

fun isLinuxOrMacOs(): Boolean = listOf("linux", "mac os", "macos").contains(
    System.getProperty("os.name").lowercase(),
)
