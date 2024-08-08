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
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.rickandmorty.feature.library)
    alias(libs.plugins.rickandmorty.compose.library)
}

android {
    namespace = "io.davidosemwota.rickandmorty.characters"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.models)
    implementation(projects.core.ui)

    implementation(libs.androidx.paging.compose)
    implementation(libs.coil)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(projects.core.testing)
    testImplementation(libs.test.core)
    testImplementation(projects.core.data)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)

    androidTestImplementation(libs.androidx.text.ext)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.jnit4)
    androidTestImplementation(libs.truth)
    androidTestImplementation(projects.core.testing)

    debugImplementation(libs.androidx.ui.test.manifest)
}
