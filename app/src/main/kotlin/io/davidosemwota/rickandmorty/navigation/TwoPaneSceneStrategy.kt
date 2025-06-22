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
package io.davidosemwota.rickandmorty.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.Scene
import androidx.navigation3.ui.SceneStrategy
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND

class TwoPaneSceneStrategy<T : Any> : SceneStrategy<T> {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit,
    ): Scene<T>? {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        // Condition 1: Only return a Scene if the window is sufficiently wide to render two panes.
        // We use isWidthAtLeastBreakpoint with WIDTH_DP_MEDIUM_LOWER_BOUND (600dp).

        if (!windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }

        val lastTwoEntries = entries.takeLast(2)

        // Condition 2: Only return a Scene if there are two entries, and both have declared
        // they can be displayed in a two pane scene.
        return if (lastTwoEntries.size == 2 &&
            lastTwoEntries.all { it.metadata.containsKey(TwoPaneScene.TWO_PANE_KEY) }
        ) {
            val firstEntry = lastTwoEntries.first()
            val secondEntry = lastTwoEntries.last()
            firstEntry.contentKey

            // The scene key must uniquely represent the state of the scene.
            val sceneKey = Pair(firstEntry.contentKey, secondEntry.contentKey)

            TwoPaneScene(
                key = sceneKey,
                // Where we go back to is a UX decision. In this case, we only remove the top
                // entry from the back stack, despite displaying two entries in this scene.
                // This is because in this app we only ever add one entry to the
                // back stack at a time. It would therefore be confusing to the user to add one
                // when navigating forward, but remove two when navigating back.
                previousEntries = entries.dropLast(1),
                firstEntry = firstEntry,
                secondEntry = secondEntry,
            )
        } else {
            null
        }
    }
}
