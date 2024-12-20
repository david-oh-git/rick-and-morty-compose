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
package io.davidosemwota.rickandmorty.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.davidosemwota.rickandmorty.data.db.EpisodeWithCharacters
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndEpisodeEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.PageInfoEntity
import io.davidosemwota.rickandmorty.data.db.entities.getEpisodeIdentifier
import io.davidosemwota.rickandmorty.data.db.entities.toEntity
import io.davidosemwota.rickandmorty.data.db.mappers.toEpisodeEntity
import io.davidosemwota.rickandmorty.data.db.mappers.toListOfCharacterEntity
import io.davidosemwota.rickandmorty.network.RickAndMortyApiService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class EpisodesRemoteMediator constructor(
    private val database: RickAndMortyDatabase,
    private val rickAndMortyApiService: RickAndMortyApiService,
) : RemoteMediator<Int, EpisodeWithCharacters>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EpisodeWithCharacters>,
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.APPEND -> {
                    val lastItem = getPageInfoEntityForLastItem(state)
                    val nextKey = lastItem?.next
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = lastItem != null,
                        )
                    nextKey
                }
                LoadType.PREPEND -> {
                    val firstItem = getPageInfoForFirstItem(state)
                    val previousKey = firstItem?.prev
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = firstItem != null,
                        )
                    previousKey
                }
                LoadType.REFRESH -> {
                    1
                }
            }

            val response = rickAndMortyApiService.getEpisodes(page = page)
            if (response.errorResponse != null) {
                val errorResponse = response.errorResponse
                return MediatorResult.Error(errorResponse?.exception ?: Exception())
            }

            val pageIdentity = getEpisodeIdentifier(page)
            val pageInfo = response.info?.toEntity()?.copy(identifier = pageIdentity)

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
//                    empty both DB tables
                    database.episodeDao().deleteAllItems()
                    database.pageInfoDao().clearAllEpisodePageInfo()
                }
                // save pageInfo
                pageInfo?.let { database.pageInfoDao().insert(it) }
                // save episodes, characters
                response.results?.filterNotNull()?.map {
                    val episode = it.toEpisodeEntity()?.copy(pageIdentity = pageIdentity)
                    val characters = it.characters.toListOfCharacterEntity()

                    episode?.let {
                        database.episodeDao().insert(episode)

                        characters.map { character ->
                            database.characterDao().insert(character)
                            val characterAndEpisodeEntityRef = CharacterAndEpisodeEntityRef(
                                characterId = character.characterId,
                                episodeId = episode.episodeId,
                            )

                            database.characterEntitiesRefDao().insert(characterAndEpisodeEntityRef)
                        }
                    }
                }
            }

            return MediatorResult.Success(endOfPaginationReached = pageInfo?.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getPageInfoForFirstItem(
        state: PagingState<Int, EpisodeWithCharacters>,
    ): PageInfoEntity? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { episodeWithCharacters ->
                database.pageInfoDao().getPageInfo(
                    episodeWithCharacters.episode.pageIdentity,
                )
            }
    }

    private suspend fun getPageInfoEntityForLastItem(
        state: PagingState<Int, EpisodeWithCharacters>,
    ): PageInfoEntity? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { episodeWithCharacters ->
                database.pageInfoDao().getPageInfo(
                    episodeWithCharacters.episode.pageIdentity,
                )
            }
    }
}
