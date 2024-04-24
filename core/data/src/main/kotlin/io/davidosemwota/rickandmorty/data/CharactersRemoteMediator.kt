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
package io.davidosemwota.rickandmorty.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.PageInfoEntity
import io.davidosemwota.rickandmorty.data.db.entities.toEntity
import io.davidosemwota.rickandmorty.network.RickAndMortyApiService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator constructor(
    private val database: RickAndMortyDatabase,
    private val rickAndMortyApiService: RickAndMortyApiService,
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
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
                    val closestItem = getPageInfoClosestToCurrentPosition(state)
                    closestItem?.prev
                }
            }

            val response = rickAndMortyApiService.getCharacters(page = page)
            if (response.errorResponse != null) {
                val errorResponse = response.errorResponse
                return MediatorResult.Error(errorResponse?.exception ?: Exception())
            }
            val pageInfo = response.info

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
//                    empty both DB tables
                    database.characterDao().deleteAllItems()
                    database.pageInfoDao().getAllItems()
                }

                val pageInfoList = mutableListOf<PageInfoEntity>()
                val characters = response.results?.map {
                    pageInfo?.toEntity(it.id)?.let { info -> pageInfoList.add(info) }
                    it.toEntity()
                }
                if (characters != null) {
                    database.characterDao().insertAll(characters)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = pageInfo?.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPageInfoClosestToCurrentPosition(
        state: PagingState<Int, CharacterEntity>,
    ): PageInfoEntity? {
        return state.anchorPosition?.let { index ->
            database.pageInfoDao().getPageInfo(index)
        }
    }

    private suspend fun getPageInfoForFirstItem(
        state: PagingState<Int, CharacterEntity>,
    ): PageInfoEntity? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                database.pageInfoDao().getPageInfo(character.id)
            }
    }

    private suspend fun getPageInfoEntityForLastItem(
        state: PagingState<Int, CharacterEntity>,
    ): PageInfoEntity? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                database.pageInfoDao().getPageInfo(character.id)
            }
    }
}
