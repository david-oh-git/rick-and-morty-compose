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
import io.davidosemwota.rickandmorty.plugins.RickAndMortyBuildType
import io.davidosemwota.rickandmorty.plugins.RickAndMortyBuildVersions

plugins {
    alias(libs.plugins.rickandmorty.android.application)
    alias(libs.plugins.rickandmorty.android.application.compose)
    alias(libs.plugins.rickandmorty.dagger.hilt)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "io.davidosemwota.rickandmorty"

    defaultConfig {
        applicationId = "io.davidosemwota.rickandmorty"
        versionCode = RickAndMortyBuildVersions.VERSION_CODE
        versionName = RickAndMortyBuildVersions.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {

        debug {
            applicationIdSuffix = RickAndMortyBuildType.DEBUG.applicationSuffix
        }

        release {
            isMinifyEnabled = RickAndMortyBuildType.RELEASE.isMinifyedEnabled
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        lintConfig = rootProject.file(".lint/lint.xml")
        checkAllWarnings = true
        warningsAsErrors = true
    }
}

dependencies {
    implementation(projects.features.characters)
    implementation(projects.features.episodes)
    implementation(projects.core.ui)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.profileinstaller)
    implementation(libs.timber)

    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.adaptive.android)
    implementation(libs.androidx.material3.window.size.class1.android)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.text.ext)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.jnit4)
    androidTestImplementation(libs.truth)
    baselineProfile(project(":baseline-profile"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
