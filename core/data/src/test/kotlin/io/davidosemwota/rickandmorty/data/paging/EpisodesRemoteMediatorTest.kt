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

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.davidosemwota.rickandmorty.data.FakeApiService.RemoteServiceSuccessNoMoreData
import io.davidosemwota.rickandmorty.data.FakeApiService.RemoteServiceSuccessWithMoreData
import io.davidosemwota.rickandmorty.data.FakeApiService.RemoteServiceWithError
import io.davidosemwota.rickandmorty.data.db.EpisodeWithCharacters
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.network.RickAndMortyApiService
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalPagingApi::class)
@RunWith(RobolectricTestRunner::class)
internal class EpisodesRemoteMediatorTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var episodesRemoteMediator: EpisodesRemoteMediator
    private lateinit var rickAndMortyApiService: RickAndMortyApiService

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun reset() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun verifyRefreshLoadReturnsSuccessWhenMoreData() = runTest {
        // Arrange
        rickAndMortyApiService = RemoteServiceSuccessWithMoreData()
        episodesRemoteMediator = EpisodesRemoteMediator(
            database = rickAndMortyDatabase,
            rickAndMortyApiService = rickAndMortyApiService,
        )
        val pagingState = PagingState<Int, EpisodeWithCharacters>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )

        // When/Act
        val result = episodesRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then/Assert
        assertTrue { result is MediatorResult.Success }
        assertFalse { (result as MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    fun verifyRefreshLoadReturnsSuccessAndPaginationEndWhenNoMoreData() = runTest {
        // Given/Arrange
        rickAndMortyApiService = RemoteServiceSuccessNoMoreData()
        episodesRemoteMediator = EpisodesRemoteMediator(
            database = rickAndMortyDatabase,
            rickAndMortyApiService = rickAndMortyApiService,
        )
        val pagingState = PagingState<Int, EpisodeWithCharacters>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )

        // When/Act
        val result = episodesRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then/Assert
        assertTrue { result is MediatorResult.Success }
        assertTrue { (result as MediatorResult.Success).endOfPaginationReached }
    }

    @Test
    fun verifyRefreshLoadReturnsErrorWhenItOccurs() = runTest {
        // Given/Arrange
        rickAndMortyApiService = RemoteServiceWithError()
        episodesRemoteMediator = EpisodesRemoteMediator(
            database = rickAndMortyDatabase,
            rickAndMortyApiService = rickAndMortyApiService,
        )
        val pagingState = PagingState<Int, EpisodeWithCharacters>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )

        // When/Act
        val result = episodesRemoteMediator.load(LoadType.REFRESH, pagingState)

        // Then/Assert
        assertTrue { result is MediatorResult.Error }
    }
}
