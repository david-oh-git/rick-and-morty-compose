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
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEpisodeEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterLocationEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterOriginEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterResidentEntity
import io.davidosemwota.rickandmorty.data.db.entities.getCharacterIdentifier
import io.davidosemwota.rickandmorty.data.generateCharacters
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class CharacterDaoTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var characterDao: CharacterDao

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()

        characterDao = rickAndMortyDatabase.characterDao()
    }

    @After
    fun close() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun searchUnknownCharacter_verifyNullResponse() = runTest {
        // Given/Arrange . adds 2 characters with id 1 & 2
        characterDao.insertAll(generateCharacters())

        // When/Act search for id 100(non existent)
        val result = characterDao.getCharacter(99)

        // Then/Assert
        assertThat(result).isNull()
    }

    @Test
    fun searchItem_verifyResponse() = runTest {
        // Given/Arrange
        characterDao.insertAll(generateCharacters())
        val id = 46
        val evilMorty = CharacterEntity(
            id = id,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            origin = CharacterOriginEntity(
                id = "40",
                name = "C -139 Earth",
                dimension = "C-137",
            ),
            location = CharacterLocationEntity(
                id = "22",
                name = "londoninion",
                dimension = "C-137",
                residents = listOf(
                    CharacterResidentEntity(
                        id = "34",
                        name = "Evil morty",
                        image = "image url",
                    ),
                ),
            ),
            episodes = listOf(
                CharacterEpisodeEntity(
                    id = "56",
                    name = "S03E09",
                ),
                CharacterEpisodeEntity(
                    id = "16",
                    name = "S01E09",
                ),
            ),
            pageIdentity = getCharacterIdentifier(1),
        )
        characterDao.insertAll(listOf(evilMorty))
        // When/Act
        val result = characterDao.getCharacter(id)
        // Then/Assert
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(evilMorty)
    }

    @Test
    fun deleteAllItems_verifyItemsAreDeleted() = runTest {
        // Given/Arrange
        characterDao.insertAll(generateCharacters())

        // When/Act
        characterDao.deleteAllItems()
        val result = characterDao.getCharacters()

        // Then/Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun addMultipleCharacters_verifyPagedResult_NotNull() = runTest {
        // Given/Arrange

        val rick = CharacterEntity(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            origin = CharacterOriginEntity(
                id = "40",
                name = "C -137 Earth",
                dimension = "C-137",
            ),
            location = CharacterLocationEntity(
                id = "22",
                name = "londoninion",
                dimension = "C-137",
                residents = listOf(
                    CharacterResidentEntity(
                        id = "34",
                        name = "Evil morty",
                        image = "image url",
                    ),
                ),
            ),
            episodes = listOf(
                CharacterEpisodeEntity(
                    id = "56",
                    name = "S03E09",
                ),
            ),
            pageIdentity = getCharacterIdentifier(1),
        )

        val morty = CharacterEntity(
            id = 2,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            origin = CharacterOriginEntity(
                id = "40",
                name = "C -137 Earth",
                dimension = "C-137",
            ),
            location = CharacterLocationEntity(
                id = "22",
                name = "londoninion",
                dimension = "C-137",
                residents = listOf(
                    CharacterResidentEntity(
                        id = "34",
                        name = "Evil morty",
                        image = "image url",
                    ),
                ),
            ),
            episodes = listOf(
                CharacterEpisodeEntity(
                    id = "56",
                    name = "S03E09",
                ),
                CharacterEpisodeEntity(
                    id = "16",
                    name = "S01E09",
                ),
            ),
            pageIdentity = getCharacterIdentifier(1),
        )
        val characters = listOf(rick, morty)

        // When/Act
        characterDao.insertAll(characters)

        // Then/Assert

        val charactersResult = (
            characterDao.getPagedCharacters().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 50,
                    placeholdersEnabled = false,
                ),
            ) as PagingSource.LoadResult.Page
            ).data

        assertThat(charactersResult).isNotNull()
        assertThat(charactersResult).isNotEmpty()
        assertThat(charactersResult.size).isEqualTo(characters.size)
    }

    @Test
    fun addMultipleCharacters_verifyPagedResult_NullOrEmpty() = runTest {
        // Given/Arrange
        characterDao.insertAll(generateCharacters())

        // When/Act
        characterDao.deleteAllItems()

        // Then/Assert
        val charactersResult = (
            characterDao.getPagedCharacters().load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 50,
                    placeholdersEnabled = false,
                ),
            ) as PagingSource.LoadResult.Page
            ).data

        assertThat(charactersResult).isNotNull()
        assertThat(charactersResult).isEmpty()
    }
}
