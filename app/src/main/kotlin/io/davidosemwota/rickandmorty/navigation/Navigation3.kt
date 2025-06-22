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

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import io.davidosemwota.rickandmorty.R
import io.davidosemwota.rickandmorty.characters.detail.CharacterDetailsRoute
import io.davidosemwota.rickandmorty.characters.list.CharactersRoute
import io.davidosemwota.rickandmorty.episodes.detail.EpisodeDetailsRoute
import io.davidosemwota.rickandmorty.episodes.list.EpisodesRoute
import io.davidosemwota.rickandmorty.navigation.MainScreens.CharacterList
import io.davidosemwota.rickandmorty.navigation.MainScreens.EpisodeList

/**
 * A CompositionLocal that provides a [SharedTransitionScope] for navigation.
 * This allows shared element transitions to be coordinated across different navigation destinations.
 * It throws an [IllegalStateException] if accessed without a [SharedTransitionScope] being provided
 * via [SharedTransitionLayout] or [SharedTransitionScope].
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val localNavSharedTransitionScope: ProvidableCompositionLocal<SharedTransitionScope> =
    compositionLocalOf {
        throw IllegalStateException(
            "Unexpected access to LocalNavSharedTransitionScope. You must provide a " +
                "SharedTransitionScope from a call to SharedTransitionLayout() or " +
                "SharedTransitionScope()",
        )
    }

/**
 * A [NavEntryDecorator] that enables shared element transitions within a navigation scene.
 * It wraps the content of a navigation entry in a [Box] with the `sharedElement` modifier.
 */
// @OptIn(ExperimentalSharedTransitionApi::class)
// val sharedEntryInSceneNavEntryDecorator = navEntryDecorator { entry ->
//    with(localNavSharedTransitionScope.current) {
//        Box(
//            Modifier.sharedElement(
//                rememberSharedContentState(entry.key),
//                animatedVisibilityScope = LocalNavAnimatedContentScope.current,
//            ),
//        ) {
// //            entry.content(entry.key)
//            entry.Content()
//        }
//    }
// }

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun Nav3App(
    backStack: NavBackStack,
    modifier: Modifier = Modifier,
) {
    SharedTransitionLayout {
        val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

        val twoPaneSceneStrategy = remember {
            TwoPaneSceneStrategy<Any>()
        }

        NavDisplay(
            modifier = modifier.fillMaxSize(),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategy = twoPaneSceneStrategy,
            entryProvider = entryProvider {
                entry<CharacterList>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = { },
                    ),
                ) {
                    CharactersRoute(
                        onCharacterClick = { backStack.add(CharacterDetails(it)) },
                    )
                }
                entry<CharacterDetails>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) { entry ->
                    CharacterDetailsRoute(
                        id = entry.id,
                        onBackNav = { backStack.removeLastOrNull() },
                    )
                }

                entry<EpisodeList>(
                    metadata = ListDetailSceneStrategy.listPane(
                        detailPlaceholder = { },
                    ),
                ) {
                    EpisodesRoute(
                        onEpisodeClick = { backStack.add(EpisodeDetails(it)) },
                    )
                }

                entry<EpisodeDetails>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) { entry ->
                    EpisodeDetailsRoute(
                        id = entry.id,
                        onBackNav = { backStack.removeLastOrNull() },
                    )
                }
            },
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        )
    }
}

@Composable
fun Navigation3BottomNavigation(
    backStack: NavBackStack,
    dashboardDestination: List<MainScreens> = listOf(
        CharacterList(),
        EpisodeList(),
    ),
) {
    val currentDestination = backStack.lastOrNull()

    NavigationBar(
        modifier = Modifier
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(8.dp)),
    ) {
        dashboardDestination.forEach { destination ->
            val selected = currentDestination == destination
            val icon = if (selected) destination.selectedIcon else destination.unSelectedIcon

            NavigationBarItem(
                selected = selected,
                onClick = {
                    backStack.add(destination)
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.main_screen_bottom_nav_unselected_icon_desc),
                    )
                },
                label = { Text(text = stringResource(id = destination.iconTitleId)) },

            )
        }
    }
}
