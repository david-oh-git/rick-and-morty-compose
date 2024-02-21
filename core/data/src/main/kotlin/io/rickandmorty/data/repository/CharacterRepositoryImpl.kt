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
package io.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rickandmorty.models.Character
import io.davidosemwota.network.RickAndMortyApiService
import io.rickandmorty.data.CharactersRemoteMediator
import io.rickandmorty.data.db.RickAndMortyDatabase
import io.rickandmorty.data.db.entities.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl constructor(
    private val database: RickAndMortyDatabase,
    private val service: RickAndMortyApiService,
    private val pagingConfig: PagingConfig = PagingConfig(
        pageSize = PAGE_SIZE,
        enablePlaceholders = false,
    ),
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = CharactersRemoteMediator(
                database = database,
                rickAndMortyApiService = service,
            ),
        ) {
            database.characterDao().getPagedCharacters()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toModel()
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 25
    }
}
