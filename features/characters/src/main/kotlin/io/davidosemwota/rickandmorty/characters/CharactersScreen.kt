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
package io.davidosemwota.rickandmorty.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun CharactersRoute(
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val characters = viewModel.charactersPagingDataState.collectAsLazyPagingItems()

    LazyColumn {
        items(characters.itemCount) { index ->
            val character = characters[index]
            character?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = it.status,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }

        characters.apply {
            val pagingItems = this

            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = characters.loadState.refresh as LoadState.Error
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(text = error.error.message ?: "Default error meg HERE")
                            Spacer(modifier = Modifier)
                            Button(
                                onClick = {
                                    pagingItems.retry()
                                },
                            ) {
                                Text(text = "Error loading extra items")
                            }
                        }
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        Text("Loading extra items")
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = characters.loadState.refresh as LoadState.Error
                    item {
                        Column {
                            Text(text = error.error.message ?: "Default error meg HERE")
                            Spacer(modifier = Modifier)
                            Button(
                                onClick = {
                                    pagingItems.retry()
                                },
                            ) {
                                Text(text = "Error loading extra items")
                            }
                        }
                    }
                }
            }
        }
    }
}
