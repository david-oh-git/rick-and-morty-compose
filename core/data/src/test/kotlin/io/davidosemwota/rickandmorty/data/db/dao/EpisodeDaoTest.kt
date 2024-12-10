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
package io.davidosemwota.rickandmorty.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.data.db.entities.getEpisodeIdentifier
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class EpisodeDaoTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var episodeDao: EpisodeDao

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()

        episodeDao = rickAndMortyDatabase.episodeDao()
    }

    @After
    fun close() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun saveItems_verifySavedToDb() = runTest {
        // Given
        val s01e01 = EpisodeEntity(
            episodeId = 1,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val s01e02 = EpisodeEntity(
            episodeId = 2,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(1),
        )
        val s01e03 = EpisodeEntity(
            episodeId = 3,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val s01e04 = EpisodeEntity(
            episodeId = 4,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )

        val episodes = listOf(s01e01, s01e02, s01e03, s01e04)
        episodeDao.insertAll(episodes)

        // When
        val result = episodeDao.getEpisodes()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(episodes.size)
    }

    @Test
    fun saveItem_verifySaved() = runTest {
        // Given
        val s01e01 = EpisodeEntity(
            episodeId = 1,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        episodeDao.insert(s01e01)

        // When
        val result = episodeDao.getEpisodes()

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)

        val item = result[0]

        assertThat(item.episodeId).isEqualTo(s01e01.episodeId)
        assertThat(item.airDate).isEqualTo(s01e01.airDate)
        assertThat(item.episode).isEqualTo(s01e01.episode)
        assertThat(item.name).isEqualTo(s01e01.name)
    }

    @Test
    fun saveItems_verifyDeleted() = runTest {
        // Given
        val s01e01 = EpisodeEntity(
            episodeId = 1,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(5),
        )
        val s01e02 = EpisodeEntity(
            episodeId = 2,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val s01e03 = EpisodeEntity(
            episodeId = 3,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val s01e04 = EpisodeEntity(
            episodeId = 4,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )

        val episodes = listOf(s01e01, s01e02, s01e03, s01e04)
        episodeDao.insertAll(episodes)

        // When
        episodeDao.deleteAllItems()
        val result = episodeDao.getEpisodes()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun saveItemsAndSearch_verifySearchResponse() = runTest {
        // Given
        val expectedId = 9
        val s01e01 = EpisodeEntity(
            episodeId = 1,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(5),
        )
        val s01e02 = EpisodeEntity(
            episodeId = 2,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val expectedItem = EpisodeEntity(
            episodeId = expectedId,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )
        val s01e04 = EpisodeEntity(
            episodeId = 4,
            airDate = "03-02-19",
            episode = "The birth",
            name = "S01E01",
            pageIdentity = getEpisodeIdentifier(2),
        )

        val episodes = listOf(s01e01, s01e02, expectedItem, s01e04)
        episodeDao.insertAll(episodes)

        // When
        val result = episodeDao.getEpisode(expectedId)

        // Then
        assertThat(result).isNotNull()
        assertThat(expectedItem.episodeId).isEqualTo(result?.episodeId)
        assertThat(expectedItem.name).isEqualTo(result?.name)
        assertThat(expectedItem.airDate).isEqualTo(result?.airDate)
        assertThat(expectedItem.episode).isEqualTo(result?.episode)
        assertThat(expectedItem.pageIdentity).isEqualTo(result?.pageIdentity)
    }
}
