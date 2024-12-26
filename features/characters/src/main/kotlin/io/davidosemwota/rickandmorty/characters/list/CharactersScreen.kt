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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.davidosemwota.rickandmorty.characters.R
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.CHARACTER_SCREEN_ERROR_BUTTON
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.CHARACTER_SCREEN_VERTICAL_GRID
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.CHARACTER_SCREEN_VERTICAL_GRID_ITEM
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_BUTTON
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_BUTTON_TEXT
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.ERROR_SCREEN_TEXT
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.LOADING_SCREEN
import io.davidosemwota.rickandmorty.characters.list.CharactersScreenTestTags.LOADING_SCREEN_APPEND
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.models.PagingState
import io.davidosemwota.ui.GeneralPreview
import io.davidosemwota.ui.PreviewComposable
import io.davidosemwota.ui.components.FullScreenLoading
import timber.log.Timber

@Composable
fun CharactersRoute(
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val characters = viewModel.charactersPagingDataState.collectAsLazyPagingItems()
    val screenState: CharacterScreenState by viewModel.screenState.collectAsStateWithLifecycle()

    viewModel.hasEmptyCharacterDatabase(characters.itemCount == 0)

    CharactersScreenContent(
        modifier = Modifier.fillMaxSize(),
        screenState = screenState,
        characters = characters,
        updateFullScreenState = viewModel::updateFullScreenState,
        updateAppendState = viewModel::updateAppendScreenState,
    )
}

@Composable
fun CharactersScreenContent(
    modifier: Modifier = Modifier,
    screenState: CharacterScreenState,
    characters: LazyPagingItems<Character>,
    updateFullScreenState: (PagingState) -> Unit,
    updateAppendState: (PagingState) -> Unit,
) {
    var error = LoadState.Error(Exception())

    characters.apply {
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
            ErrorScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag(ERROR_SCREEN),
                error = error,
                retry = {
                    Timber.tag("xxx").d("CLICKED")
                    updateFullScreenState(PagingState.Loading)
                    characters.retry()
                },
            )
        } else if (screenState.hasFullScreenLoading) {
            FullScreenLoading(
                modifier = Modifier.testTag(LOADING_SCREEN),
            )
        } else {
            val listState = rememberLazyStaggeredGridState()
            val refreshButtonVisibility by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex == 0
                }
            }
            Box {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                        .fillMaxSize()
                        .testTag(CHARACTER_SCREEN_VERTICAL_GRID),
                    columns = StaggeredGridCells.Adaptive(minSize = 135.dp),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    state = listState,
                ) {
                    items(
                        characters.itemCount,
                        key = characters.itemKey { it.id },
                    ) { index ->
                        val character = characters[index]

                        if (character != null) {
                            CharacterGridItem(
                                character = character,
                            )
                        }
                    }

                    if (screenState.hasAppendScreenLoading) {
                        item(
                            span = StaggeredGridItemSpan.FullLine,
                        ) {
                            LoadingItem()
                        }
                    } else if (screenState.hasAppendScreenError) {
                        item(
                            span = StaggeredGridItemSpan.FullLine,
                        ) {
                            ErrorScreenContent(
                                modifier = Modifier.fillMaxSize(),
                                error = error,
                                retry = {
                                    Timber.tag("xxx").d("CLICKED")
                                    updateAppendState(PagingState.Loading)
                                    characters.retry()
                                },
                            )
                        }
                    }
                } // END LAzy Vertical grid

                // TODO when to show this error button :(
                ErrorButton(
                    modifier = Modifier.align(Alignment.TopCenter),
                    buttonVisibility = refreshButtonVisibility,
                    retry = characters::retry,
                )
            }
        }
    }
}

@Composable
fun ErrorButton(
    modifier: Modifier = Modifier,
    retry: () -> Unit,
    buttonVisibility: Boolean,
) {
//    val density = LocalDensity.current
    AnimatedVisibility(
        modifier = modifier,
        visible = buttonVisibility,
    ) {
        Column() {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .testTag(CHARACTER_SCREEN_ERROR_BUTTON),
                onClick = {
                    retry.invoke()
                },
            ) {
                Text(
                    text = "Refresh",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
fun ErrorScreenContent(
    modifier: Modifier = Modifier,
    error: LoadState.Error,
    retry: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // TODO : default error message for various exceptions. eg IllegalStateException
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier
                .testTag(ERROR_SCREEN_TEXT),
            text = error.error.message ?: stringResource(R.string.api_default_error_response),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            modifier = Modifier
                .testTag(ERROR_SCREEN_BUTTON),
            onClick = {
                retry.invoke()
            },
        ) {
            Text(
                modifier = Modifier
                    .testTag(ERROR_SCREEN_BUTTON_TEXT),
                text = "Try again",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}

@GeneralPreview
@Composable
fun ErrorScreenContentPreview() {
    PreviewComposable {
        ErrorScreenContent(
            error = LoadState.Error(error = Throwable("Unauthorized access")),
            modifier = Modifier.fillMaxSize(),
            retry = { },
        )
    }
}

@Composable
fun LoadingItem(
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .testTag(LOADING_SCREEN_APPEND),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun CharacterGridItem(
    modifier: Modifier = Modifier,
    character: Character,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable { /** TODO item click then navigate **/ }
            .testTag(CHARACTER_SCREEN_VERTICAL_GRID_ITEM),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(character.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.character_image_desc, character.name),
                contentScale = ContentScale.Crop,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(2.dp)
                    .align(Alignment.BottomCenter),
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterGridItemPreview() {
    CharacterGridItem(
        character = Character(
            id = 1,
            imageUrl = "",
            name = "Prime Rick",
            type = "Human",
            gender = "Male",
            status = "Unknown",
            species = "Human",
            originId = null,
            locations = emptyList(),
            episodes = emptyList(),
        ),
    )
}
