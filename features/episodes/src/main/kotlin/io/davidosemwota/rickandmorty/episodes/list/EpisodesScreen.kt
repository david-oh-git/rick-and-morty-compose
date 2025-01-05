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
package io.davidosemwota.rickandmorty.episodes.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import io.davidosemwota.rickandmorty.episodes.R
import io.davidosemwota.rickandmorty.models.Episode
import io.davidosemwota.rickandmorty.models.PagingState
import io.davidosemwota.ui.GeneralPreview
import io.davidosemwota.ui.PreviewComposable
import io.davidosemwota.ui.components.FullScreenError
import io.davidosemwota.ui.components.FullScreenLoading
import io.davidosemwota.ui.components.HorizontalLoadingItem
import io.davidosemwota.ui.components.JerryCard
import io.davidosemwota.ui.util.getDisplayMessage
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

@Composable
fun EpisodesRoute(
    viewModel: EpisodesViewModel = hiltViewModel(),
) {
    val episodes = viewModel.episodesPagingDataState.collectAsLazyPagingItems()
    val screenState: EpisodesScreenState by viewModel.screenState.collectAsStateWithLifecycle()

    viewModel.hasEmptyDatabase(episodes.itemCount == 0)
    EpisodesScreenContent(
        updateFullScreenState = viewModel::updateFullScreenState,
        updateAppendState = viewModel::updateAppendScreenState,
        screenState = screenState,
        episodes = episodes,
        fullScreenErrorButtonText = stringResource(R.string.episodes_error_retry_btn),
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
fun EpisodesScreenContent(
    updateFullScreenState: (PagingState) -> Unit,
    updateAppendState: (PagingState) -> Unit,
    screenState: EpisodesScreenState,
    episodes: LazyPagingItems<Episode>,
    fullScreenErrorButtonText: String,
    modifier: Modifier = Modifier,
) {
    var error = LoadState.Error(Exception())
    val context = LocalContext.current

    episodes.apply {
        when (loadState.refresh) {
            is LoadState.Error -> {
                Timber.tag("xxx").d("FULL REFRESH ERROR EMPTY")
                error = loadState.refresh as LoadState.Error
                updateFullScreenState(PagingState.Error)
            }
            LoadState.Loading -> {
                Timber.tag("xxx").d("FULL REFRESH LOADING")
                updateFullScreenState(PagingState.Loading)
            }
            is LoadState.NotLoading -> {
                Timber.tag("xxx").d("FULL REFRESH NOT LOADING")
                updateFullScreenState(PagingState.NotLoading)
            }
        }

        when (loadState.append) {
            is LoadState.Error -> {
                error = loadState.append as LoadState.Error
                Timber.tag("xxx").d("APPEND ERROR")
                updateAppendState(PagingState.Error)
            }
            LoadState.Loading -> {
                Timber.tag("xxx").d("APPEND LOADING")
                updateAppendState(PagingState.Loading)
            }
            is LoadState.NotLoading -> {
                Timber.tag("xxx").d("APPEND NOT LOADING")
                updateAppendState(PagingState.NotLoading)
            }
        }
    }

    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        if (screenState.hasFullScreenError) {
            FullScreenError(
                errorMessage = getDisplayMessage(
                    context = context,
                    throwable = error.error,
                ),
                retryButtonText = fullScreenErrorButtonText,
                retry = {
                    updateFullScreenState(PagingState.Loading)
                    episodes.retry()
                },
                modifier = Modifier.fillMaxSize(),
            )
        } else if (screenState.hasFullScreenLoading) {
            FullScreenLoading()
        } else {
            val listState = rememberLazyListState()

            Box {
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                        .fillMaxSize(),
                    state = listState,
                ) {
                    items(
                        episodes.itemCount,
                        key = episodes.itemKey { it.id },
                    ) { index ->
                        val episode = episodes[index]
                        if (episode != null) {
                            EpisodeItem(
                                episode = episode,
                            )
                        }
                    }

                    if (screenState.hasAppendScreenLoading) {
                        item {
                            Spacer(modifier = Modifier.size(8.dp))
                            HorizontalLoadingItem()
                        }
                    } else if (screenState.hasAppendScreenError) {
                        item {
                            ErrorItem(
                                errorMessage = getDisplayMessage(
                                    context = context,
                                    throwable = error.error,
                                ),
                                retry = {
                                },
                                retryButtonText = fullScreenErrorButtonText,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: Episode,
    modifier: Modifier = Modifier,
) {
    JerryCard(
        modifier = modifier
            .padding(4.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = 2.dp,
        cardColour = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = episode.episode,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(2.dp),
                    )
                    .padding(4.dp),
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = episode.name,
            )
        }
    }
}

@Composable
fun ErrorItem(
    errorMessage: String,
    retryButtonText: String,
    retry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = errorMessage,
        )

        OutlinedButton(
            onClick = retry,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = retryButtonText,
            )
        }
    }
}

@GeneralPreview
@Composable
fun PreviewErrorItem(
    modifier: Modifier = Modifier,
) {
    PreviewComposable {
        ErrorItem(
            errorMessage = "Do not resist the beat !!",
            retry = { },
            retryButtonText = "retry",
            modifier = modifier,
        )
    }
}

@GeneralPreview
@Composable
fun PreviewEpisodeItem() {
    PreviewComposable {
        EpisodeItem(
            episode = Episode(
                id = 2,
                name = "Lawnmower Dog",
                airDate = "December 9, 2013",
                episode = "S01E02",
                pageIdentity = "",
                characters = emptyList(),
            ),
        )
    }
}

@GeneralPreview
@Composable
private fun PreviewEpisodesSreen(
    modifier: Modifier = Modifier,
) {
    PreviewComposable {
        EpisodesScreenContent(
            episodes = flowOf(
                PagingData.from(
                    listOf<Episode>(
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
                    ),
                ),
            ).collectAsLazyPagingItems(),
            screenState = EpisodesScreenState(
                isDatabaseEmpty = false,
                fullScreenState = PagingState.NotLoading,
                appendState = PagingState.Loading,
            ),
            fullScreenErrorButtonText = "",
            updateFullScreenState = { },
            updateAppendState = { },
            modifier = modifier,
        )
    }
}
