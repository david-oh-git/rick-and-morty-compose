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
package io.davidosemwota.rickandmorty.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.ui.graphics.vector.ImageVector
import io.davidosemwota.rickandmorty.R
import io.davidosemwota.ui.components.icons.FormatListBulleted
import io.davidosemwota.ui.components.icons.Lists
import io.davidosemwota.ui.components.icons.MortyIcons
import io.davidosemwota.rickandmorty.characters.R as characters

enum class MainScreenDestinations(
    @StringRes val iconTitleId: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
) {
    CHARACTERS(
        iconTitleId = characters.string.feature_characters_title,
        selectedIcon = Icons.Filled.Face,
        unSelectedIcon = Icons.Outlined.Face,
    ),
    EPISODES(
        iconTitleId = R.string.episodes,
        selectedIcon = MortyIcons.Lists,
        unSelectedIcon = MortyIcons.FormatListBulleted,
    ),
}
