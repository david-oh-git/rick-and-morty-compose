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
package io.davidosemwota.rickandmorty.characters.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.printToLog
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.CHARACTER_SCREEN_VERTICAL_GRID
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_BUTTON
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_BUTTON_TEXT
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_TEXT
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.LOADING_SCREEN
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.LOADING_SCREEN_APPEND
import io.davidosemwota.rickandmorty.data.repository.CharacterRepository
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.models.PagingState
import io.davidosemwota.rickandmorty.testing.FakeCharacterRepository
import io.davidosemwota.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class CharactersScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        characterRepository = FakeCharacterRepository()
    }

    @Test
    fun verifyFullScreenLoading_whenDatabaseEmpty() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharactersScreenContent(
                    characters = characterRepository.getPagedCharacters().collectAsLazyPagingItems(),
                    screenState = CharacterScreenState(
                        isCharacterDatabaseEmpty = true,
                        fullScreenState = PagingState.Loading,
                    ),
                    updateFullScreenState = { },
                    updateAppendState = { },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(LOADING_SCREEN)
            .isDisplayed()

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN)
            .isNotDisplayed()
    }

    @Test
    fun verifyFullScreenError_whenDatabaseEmpty() {
        val errorMsg = "error msg displayed"
        val errorPagingData: PagingData<Character> = PagingData.from(
            data = emptyList(),
            sourceLoadStates = LoadStates(
                refresh = LoadState.Error(Exception(errorMsg)),
                append = LoadState.Error(Exception(errorMsg)),
                prepend = LoadState.Error(Exception(errorMsg)),
            ),
            mediatorLoadStates = LoadStates(
                refresh = LoadState.Error(Exception(errorMsg)),
                append = LoadState.Error(Exception(errorMsg)),
                prepend = LoadState.Error(Exception(errorMsg)),
            ),
        )

        composeTestRule.setContent {
            RickAndMortyTheme {
                CharactersScreenContent(
                    characters = flowOf(errorPagingData).collectAsLazyPagingItems(),
                    screenState = CharacterScreenState(
                        isCharacterDatabaseEmpty = true,
                        fullScreenState = PagingState.Error,
                    ),
                    updateFullScreenState = { },
                    updateAppendState = { },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN)
            .isDisplayed()

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN_TEXT)
            .isDisplayed()

        composeTestRule
            .onNode(
                hasTestTag(ERROR_SCREEN_TEXT) and
                    hasText(errorMsg),
            )

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN_BUTTON)
            .isDisplayed()

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN_BUTTON)
            .assertHasClickAction()

        composeTestRule
            .onNodeWithTag(ERROR_SCREEN_BUTTON_TEXT)
            .isDisplayed()

        composeTestRule
            .onNodeWithTag(LOADING_SCREEN)
            .isNotDisplayed()
    }

    @Test
    fun verifyAppendLoading_whenDatabaseNotEmpty() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharactersScreenContent(
                    characters = characterRepository.getPagedCharacters().collectAsLazyPagingItems(
                        StandardTestDispatcher(),
                    ),
                    screenState = CharacterScreenState(
                        isCharacterDatabaseEmpty = false,
                        fullScreenState = PagingState.NotLoading,
                        appendState = PagingState.Loading,
                    ),
                    updateFullScreenState = { },
                    updateAppendState = { },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(LOADING_SCREEN_APPEND)
            .isDisplayed()
    }

    @Test
    fun verifyNotLoading_whenDatabaseNotEmpty() {
        val rick = "Rick Smith"
        val summer = "Summer Smith"
        val primeRick = "Prime Rick"
        composeTestRule.setContent {
            RickAndMortyTheme {
                CharactersScreenContent(
                    characters = flowOf(
                        PagingData.from(
                            listOf(
                                Character(
                                    id = 1,
                                    name = rick,
                                    gender = "Male",
                                    imageUrl = "https:samada.com/kenny_chan",
                                    originId = 3,
                                ),
                                Character(
                                    id = 2,
                                    name = primeRick,
                                    gender = "Male",
                                    imageUrl = "https:samada.com/kenny_chan",
                                    originId = 2,
                                ),
                                Character(
                                    id = 3,
                                    name = summer,
                                    gender = "Female",
                                    imageUrl = "https:samada.com/kenny_chan",
                                    originId = 7,
                                ),
                            ),
                        ),
                    ).collectAsLazyPagingItems(
                        StandardTestDispatcher(),
                    ),
                    screenState = CharacterScreenState(
                        isCharacterDatabaseEmpty = false,
                        fullScreenState = PagingState.NotLoading,
                        appendState = PagingState.NotLoading,
                    ),
                    updateFullScreenState = { },
                    updateAppendState = { },
                )
            }
        }

        composeTestRule
            .onNodeWithTag(CHARACTER_SCREEN_VERTICAL_GRID)
            .printToLog(CHARACTER_SCREEN_VERTICAL_GRID)

        composeTestRule
            .onNodeWithTag(CHARACTER_SCREEN_VERTICAL_GRID)
            .isDisplayed()
    }
}
