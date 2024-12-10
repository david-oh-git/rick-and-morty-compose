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
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndEpisodeEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndLocationEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity
import io.davidosemwota.rickandmorty.data.db.entities.getCharacterIdentifier
import io.davidosemwota.rickandmorty.data.db.entities.getEpisodeIdentifier
import io.davidosemwota.rickandmorty.data.db.entities.getLocationIdentifier
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
internal class CharacterEpisodeCrossRefDaoTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var characterDao: CharacterDao
    private lateinit var episodeDao: EpisodeDao
    private lateinit var locationDao: LocationDao
    private lateinit var characterEntitiesRefDao: CharacterEntitiesRefDao

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()

        characterDao = rickAndMortyDatabase.characterDao()
        episodeDao = rickAndMortyDatabase.episodeDao()
        locationDao = rickAndMortyDatabase.locationDao()
        characterEntitiesRefDao = rickAndMortyDatabase.characterEpisodeCrossRef()
    }

    @After
    fun close() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun addCrossRefData_verifyDataSaved() = runTest {
        // Arrange
        // Characters
        val expectedEvilMortyId = 46
        val evilMorty = CharacterEntity(
            characterId = expectedEvilMortyId,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            originId = 2,
            pageIdentity = getCharacterIdentifier(1),
        )
        // Episodes
        val expectedRickId = 1
        val rick = CharacterEntity(
            characterId = expectedRickId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 22,
        )

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
        // Location
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )

        // Act: save all characters, episodes & references,
        // retrieve all saved data from the database.
        characterDao.insert(evilMorty, rick)
        episodeDao.insert(s01e01, s01e02, s01e03, s01e04)
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // Evil Morty character referenced with 3 episodes & 2 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = earthReplacement.locationId,
            ),
        )
        // Rick character referenced with all 4 episodes & 3 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e03.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = earthReplacement.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = abadango.locationId,
            ),
        )

        val totalDataResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations().first()
        val evilMortyResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(
            expectedEvilMortyId,
        )
        val rickResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(expectedRickId)

        // Assert
        assertThat(totalDataResult).isNotEmpty()
        assertThat(totalDataResult.size).isEqualTo(2) // No of characters is 2.

        assertThat(evilMortyResult).isNotNull()
        assertThat(evilMortyResult?.episodes).isNotEmpty()
        assertThat(evilMortyResult?.locations).isNotEmpty()

        assertThat(rickResult).isNotNull()
        assertThat(rickResult?.episodes).isNotEmpty()
        assertThat(rickResult?.locations).isNotEmpty()
    }

    @Test
    fun addCrossRefData_MixedWithEmptyLocation_verifyDataSaved() = runTest {
        // Arrange
        // Characters
        val expectedEvilMortyId = 46
        val evilMorty = CharacterEntity(
            characterId = expectedEvilMortyId,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            originId = 2,
            pageIdentity = getCharacterIdentifier(1),
        )
        // Episodes
        val expectedRickId = 1
        val rick = CharacterEntity(
            characterId = expectedRickId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 22,
        )

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
        // Location
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )

        // Act: save all characters, episodes & references,
        // retrieve all saved data from the database.
        characterDao.insert(evilMorty, rick)
        episodeDao.insert(s01e01, s01e02, s01e03, s01e04)
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // Evil Morty character referenced with 3 episodes.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        // Rick character referenced with all 4 episodes & 3 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e03.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = earthReplacement.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = abadango.locationId,
            ),
        )

        val totalDataResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations().first()
        val evilMortyResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(
            expectedEvilMortyId,
        )
        val rickResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(expectedRickId)

        // Assert
        assertThat(totalDataResult).isNotEmpty()
        assertThat(totalDataResult.size).isEqualTo(2) // No of characters is 2.

        assertThat(evilMortyResult).isNotNull()
        assertThat(evilMortyResult?.episodes).isNotEmpty()
        // No locations referenced for evil Morty
        assertThat(evilMortyResult?.locations).isEmpty()

        assertThat(rickResult).isNotNull()
        assertThat(rickResult?.episodes).isNotEmpty()
        assertThat(rickResult?.locations).isNotEmpty()
    }

    @Test
    fun addCrossRefData_MixedWithEmptyEpisode_verifyDataSaved() = runTest {
        // Arrange
        // Characters
        val expectedEvilMortyId = 46
        val evilMorty = CharacterEntity(
            characterId = expectedEvilMortyId,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            originId = 2,
            pageIdentity = getCharacterIdentifier(1),
        )
        // Episodes
        val expectedRickId = 1
        val rick = CharacterEntity(
            characterId = expectedRickId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 22,
        )

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
        // Location
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )

        // Act: save all characters, episodes & references,
        // retrieve all saved data from the database.
        characterDao.insert(evilMorty, rick)
        episodeDao.insert(s01e01, s01e02, s01e03, s01e04)
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // Evil Morty character referenced with 2 locations.
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = earthReplacement.locationId,
            ),
        )
        // Rick character referenced with all 4 episodes & 3 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e03.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = earthReplacement.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = abadango.locationId,
            ),
        )

        val totalDataResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations().first()
        val evilMortyResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(
            expectedEvilMortyId,
        )
        val rickResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(expectedRickId)

        // Assert
        assertThat(totalDataResult).isNotEmpty()
        assertThat(totalDataResult.size).isEqualTo(2) // No of characters is 2.

        assertThat(evilMortyResult).isNotNull()
        assertThat(evilMortyResult?.episodes).isEmpty()
        assertThat(evilMortyResult?.locations).isNotEmpty()

        assertThat(rickResult).isNotNull()
        assertThat(rickResult?.episodes).isNotEmpty()
        assertThat(rickResult?.locations).isNotEmpty()
    }

    @Test
    fun addCrossRefData_MixedWithEmptyEpisodesAndLocations_verifyDataSaved() = runTest {
        // Arrange
        // Characters
        val expectedEvilMortyId = 46
        val evilMorty = CharacterEntity(
            characterId = expectedEvilMortyId,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            originId = 2,
            pageIdentity = getCharacterIdentifier(1),
        )
        // Episodes
        val expectedRickId = 1
        val rick = CharacterEntity(
            characterId = expectedRickId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 22,
        )

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
        // Location
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )

        // Act: save all characters, episodes & references,
        // retrieve all saved data from the database.
        characterDao.insert(evilMorty, rick)
        episodeDao.insert(s01e01, s01e02, s01e03, s01e04)
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // Evil Morty character referenced with 0 episodes & 0 locations.

        // Rick character referenced with all 4 episodes & 3 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e03.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = earthReplacement.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = abadango.locationId,
            ),
        )

        val totalDataResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations().first()
        val evilMortyResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(
            expectedEvilMortyId,
        )
        val rickResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(expectedRickId)

        // Assert
        assertThat(totalDataResult).isNotEmpty()
        assertThat(totalDataResult.size).isEqualTo(2) // No of characters is 2.

        assertThat(evilMortyResult).isNotNull()
        assertThat(evilMortyResult?.episodes).isEmpty()
        assertThat(evilMortyResult?.locations).isEmpty()

        assertThat(rickResult).isNotNull()
        assertThat(rickResult?.episodes).isNotEmpty()
        assertThat(rickResult?.locations).isNotEmpty()
    }

    @Test
    fun addCrossRefDataThenDeleteAll_verifyDataDeleted() = runTest {
        // Arrange
        // Characters
        val expectedEvilMortyId = 46
        val evilMorty = CharacterEntity(
            characterId = expectedEvilMortyId,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            originId = 2,
            pageIdentity = getCharacterIdentifier(1),
        )
        // Episodes
        val expectedRickId = 1
        val rick = CharacterEntity(
            characterId = expectedRickId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 22,
        )

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
        // Location
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )

        characterDao.insert(evilMorty, rick)
        episodeDao.insert(s01e01, s01e02, s01e03, s01e04)
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // Evil Morty character referenced with 3 episodes & 2 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = evilMorty.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = evilMorty.characterId,
                locationId = earthReplacement.locationId,
            ),
        )
        // Rick character referenced with all 4 episodes & 3 locations.
        characterEntitiesRefDao.insert(
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e01.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e02.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e03.episodeId,
            ),
            CharacterAndEpisodeEntityRef(
                characterId = rick.characterId,
                episodeId = s01e04.episodeId,
            ),
        )
        characterEntitiesRefDao.insert(
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = citadelOfRicks.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = earthReplacement.locationId,
            ),
            CharacterAndLocationEntityRef(
                characterId = rick.characterId,
                locationId = abadango.locationId,
            ),
        )

        // Act: Delete all references table.
        characterEntitiesRefDao.deleteCharacterEpisodeRefDb()
        characterEntitiesRefDao.deleteCharacterLocationRefDb()

        // Assert
        val totalDataResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations().first()
        val evilMortyResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(
            expectedEvilMortyId,
        )
        val rickResult = characterEntitiesRefDao.getCharacterWithEpisodesAndLocations(expectedRickId)

        assertThat(totalDataResult).isNotEmpty() // References deleted but not character entities.
        assertThat(totalDataResult.size).isEqualTo(2) // No of characters is 2.

        assertThat(evilMortyResult).isNotNull()
        assertThat(evilMortyResult?.episodes).isEmpty()
        assertThat(evilMortyResult?.locations).isEmpty()

        assertThat(rickResult).isNotNull()
        assertThat(rickResult?.episodes).isEmpty()
        assertThat(rickResult?.locations).isEmpty()
    }
}
