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
package io.davidosemwota.rickandmorty.data.repository

import android.content.Context
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.davidosemwota.rickandmorty.data.FakeApiService
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.network.RickAndMortyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
internal class CharacterRepositoryTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var rickAndMortyApiService: RickAndMortyApiService
    private lateinit var characterRepository: CharacterRepository

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
    fun test() = runTest {
        // TODO unsure how to test this properly
        // Given/Arrange
        rickAndMortyApiService = FakeApiService.getNetworkResponseSuccess(
            numberOfCharacters = 10,
        )
        val pagingConfig = PagingConfig(
            pageSize = 5,
            enablePlaceholders = false,
        )
        characterRepository = CharacterRepositoryImpl(
            rickAndMortyDatabase,
            rickAndMortyApiService,
            pagingConfig,
        )

        val characters: Flow<PagingData<Character>> = characterRepository.getPagedCharacters()

        val snapshot: List<Character> = characters.asSnapshot {
            scrollTo(index = 0)
        }
        // When/Act

        // Then/Assert
        assertEquals(
            expected = 9,
            actual = snapshot.size,
        )
    }
}
