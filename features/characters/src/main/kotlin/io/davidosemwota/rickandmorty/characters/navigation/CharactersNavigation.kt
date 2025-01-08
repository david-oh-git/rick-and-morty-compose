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
package io.davidosemwota.rickandmorty.characters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.davidosemwota.rickandmorty.characters.detail.CharacterDetailsRoute
import io.davidosemwota.rickandmorty.characters.list.CharactersRoute
import kotlinx.serialization.Serializable

const val CHARACTERS_ROUTE = "characters"

fun NavController.navigateToCharacters(navOptions: NavOptions) = navigate(
    CHARACTERS_ROUTE,
    navOptions,
)

@Serializable
data class CharacterRoute(
    val id: Int? = null,
)

fun NavController.characterScreen(
    id: Int? = null,
    navOptions: NavOptions? = null,
) {
    navigate(route = CharacterRoute(id), navOptions)
}

fun NavGraphBuilder.charactersScreen(
    onCharacterClick: (Int) -> Unit,
) {
    composable(
        route = CHARACTERS_ROUTE,
    ) {
        CharactersRoute(
            onCharacterClick = onCharacterClick,
        )
    }
}

fun NavGraphBuilder.characterDetailScreen(
    onBackNav: () -> Unit,
) {
    composable<CharacterRoute> { backStackEntry ->
        val characterRoute: CharacterRoute = backStackEntry.toRoute()
        CharacterDetailsRoute(
            id = characterRoute.id,
            onBackNav = onBackNav,
        )
    }
}
