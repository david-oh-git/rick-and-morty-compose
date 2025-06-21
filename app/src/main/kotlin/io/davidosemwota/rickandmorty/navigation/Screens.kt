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

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import io.davidosemwota.ui.components.icons.FormatListBulleted
import io.davidosemwota.ui.components.icons.Lists
import io.davidosemwota.ui.components.icons.MortyIcons
import kotlinx.serialization.Serializable

sealed class MainScreens(
    @StringRes open val iconTitleId: Int,
    open val selectedIcon: ImageVector,
    open val unSelectedIcon: ImageVector,
) : NavKey {

    @Serializable
    data class EpisodeList(
        @StringRes override val iconTitleId: Int = io.davidosemwota.ui.R.string.episodes,
        override val selectedIcon: ImageVector = MortyIcons.Lists,
        override val unSelectedIcon: ImageVector = MortyIcons.FormatListBulleted,
    ) : MainScreens(
        iconTitleId = iconTitleId,
        selectedIcon = selectedIcon,
        unSelectedIcon = unSelectedIcon,
    )

    @Serializable
    data class CharacterList(
        @StringRes override val iconTitleId: Int = io.davidosemwota.ui.R.string.feature_characters_title,
        override val selectedIcon: ImageVector = Icons.Filled.Face,
        override val unSelectedIcon: ImageVector = Icons.Outlined.Face,
    ) : MainScreens(
        iconTitleId = iconTitleId,
        selectedIcon = selectedIcon,
        unSelectedIcon = unSelectedIcon,
    )
}

@Serializable
data class CharacterDetails(
    val id: Int,
) : NavKey

@Serializable
data class EpisodeDetails(
    val id: Int,
) : NavKey
