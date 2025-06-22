/*
 * MIT License
 *
 * Copyright (c) 2025   David Osemwota.
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
package io.davidosemwota.rickandmorty.plugins

object RickAndMortyBuildVersions {
    const val COMPILE_SDK_VERSION = 36
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION: Int = COMPILE_SDK_VERSION
    const val VERSION_CODE: Int = 1

    /**
     * The version name of the application.
     * This is displayed to the user.
     * •MAJOR version when you make incompatible API changes,
     * •MINOR version when you add functionality in a backward-compatible manner, and
     * •PATCH version when you make backward-compatible bug fixes.
     */
    const val VERSION_NAME: String = "0.0.1" // MAJOR.MINOR.PATCH
}
