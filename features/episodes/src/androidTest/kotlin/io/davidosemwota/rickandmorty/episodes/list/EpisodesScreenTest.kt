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
package io.davidosemwota.rickandmorty.episodes.list

import androidx.activity.ComponentActivity
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import io.davidosemwota.rickandmorty.data.repository.EpisodeRepository
import io.davidosemwota.rickandmorty.models.Episode
import io.davidosemwota.rickandmorty.models.PagingState
import io.davidosemwota.rickandmorty.testing.FakeEpisodesRepository
import io.davidosemwota.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class EpisodesScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var repository: EpisodeRepository

    @Before
    fun setup() {
        repository = FakeEpisodesRepository()
    }

    @Test
    fun verifyFullScreenLoading_whenDatabaseEmpty() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                EpisodesScreenContent(
                    episodes = flowOf(PagingData.from(emptyList<Episode>()))
                        .collectAsLazyPagingItems(
                            StandardTestDispatcher(),
                        ),
                    screenState = EpisodesScreenState(
                        isDatabaseEmpty = true,
                        fullScreenState = PagingState.Loading,
                    ),
                    fullScreenErrorButtonText = "",
                    updateFullScreenState = { },
                    updateAppendState = { },
                    onEpisodeClick = { },
                )
            }
        }

        composeTestRule
            .onNode(
                hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate),
            ).isDisplayed()
    }

    @Test
    fun verifyFullScreenError_whenDatabaseEmpty() = runTest {
        val errorMsg = "error msg displayed"
        val fullScreenErrorButtonText = "retry"
        val errorPagingData: PagingData<Episode> = PagingData.from(
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
                EpisodesScreenContent(
                    episodes = flowOf(errorPagingData).collectAsLazyPagingItems(
                        StandardTestDispatcher(),
                    ),
                    screenState = EpisodesScreenState(
                        isDatabaseEmpty = true,
                        fullScreenState = PagingState.Error,
                    ),
                    updateFullScreenState = { },
                    fullScreenErrorButtonText = fullScreenErrorButtonText,
                    updateAppendState = { },
                    onEpisodeClick = { },
                )
            }
        }

        // Error msg is displayed
        composeTestRule
            .onNode(
                hasText(errorMsg),
            ).isDisplayed()
        // Error retry button is displayed.
        composeTestRule
            .onNode(hasText(fullScreenErrorButtonText))
            .isDisplayed()
        // Error retry button is clickable
        composeTestRule
            .onNode(hasText(fullScreenErrorButtonText))
            .assertHasClickAction()
    }

    @Test
    fun verifyAppendLoading_whenDatabaseNotEmpty() {
        composeTestRule.setContent {
            RickAndMortyTheme {
                EpisodesScreenContent(
                    episodes = repository.getPagedEpisodes().collectAsLazyPagingItems(
                        StandardTestDispatcher(),
                    ),
                    screenState = EpisodesScreenState(
                        isDatabaseEmpty = false,
                        fullScreenState = PagingState.NotLoading,
                        appendState = PagingState.Loading,
                    ),
                    fullScreenErrorButtonText = "",
                    updateFullScreenState = { },
                    updateAppendState = { },
                    onEpisodeClick = { },
                )
            }
        }

        composeTestRule
            .onNode(
                hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate),
            ).isDisplayed()
    }

    @Test
    fun verifyNotLoading_whenDatabaseNotEmpty() {
        val episodes = listOf<Episode>(
            Episode(
                id = 20,
                name = "Linda Morris",
                airDate = "25-05-23",
                episode = "S02E07",
                pageIdentity = "ep-05",
                characters = emptyList(),
            ),
            Episode(
                id = 21,
                name = "Linda Morris",
                airDate = "25-05-23",
                episode = "S02E07",
                pageIdentity = "ep-05",
                characters = emptyList(),
            ),
            Episode(
                id = 22,
                name = "Linda Morris",
                airDate = "25-05-23",
                episode = "S02E07",
                pageIdentity = "ep-05",
                characters = emptyList(),
            ),
        )

        composeTestRule.setContent {
            EpisodesScreenContent(
                episodes = flowOf(PagingData.from(episodes)).collectAsLazyPagingItems(
                    StandardTestDispatcher(),
                ),
                screenState = EpisodesScreenState(
                    isDatabaseEmpty = false,
                    fullScreenState = PagingState.NotLoading,
                    appendState = PagingState.Loading,
                ),
                fullScreenErrorButtonText = "",
                updateFullScreenState = { },
                updateAppendState = { },
                onEpisodeClick = { },
            )
        }

        composeTestRule
    }
}
