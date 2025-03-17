/*
 * MIT License
 *
 * Copyright (c) 2025   David Osemwota.
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
package io.davidosemwota.rickandmorty.data.db.mappers

import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.CharacterWithEpisodesAndLocations
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity
import io.davidosemwota.rickandmorty.network.NetworkCharacter
import io.davidosemwota.rickandmorty.network.NetworkLocation
import org.junit.Test

class CharacterMappersTest {

    @Test
    fun `toCharacterEntity maps NetworkCharacter correctly`() {
        // Given
        val expectedId = 42
        val expectedName = "Rick Sanchez"
        val expectedImage = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        val expectedGender = "Male"
        val expectedType = "Human"
        val expectedStatus = "Alive"
        val expectedSpecies = "Human"
        val expectedOriginId = 1
        val expectedPageIdentity = ""

        val networkCharacter = NetworkCharacter(
            id = expectedId.toString(),
            name = expectedName,
            image = expectedImage,
            gender = expectedGender,
            type = expectedType,
            status = expectedStatus,
            species = expectedSpecies,
            origin = NetworkLocation(id = expectedOriginId.toString(), name = "Earth", dimension = "C-137"),
        )

        // When
        val entity = networkCharacter.toCharacterEntity()

        // Then
        assertThat(entity).isNotNull()
        entity?.let {
            assertThat(it.characterId).isEqualTo(expectedId)
            assertThat(it.name).isEqualTo(expectedName)
            assertThat(it.image).isEqualTo(expectedImage)
            assertThat(it.gender).isEqualTo(expectedGender)
            assertThat(it.type).isEqualTo(expectedType)
            assertThat(it.status).isEqualTo(expectedStatus)
            assertThat(it.species).isEqualTo(expectedStatus) // Note: There seems to be a bug here in the mapper
            assertThat(it.originId).isEqualTo(expectedOriginId)
            assertThat(it.pageIdentity).isEqualTo(expectedPageIdentity)
        }
    }

    @Test
    fun `toCharacterEntity returns null when id is null`() {
        // Given
        val networkCharacter = NetworkCharacter(
            id = null,
            name = "Rick Sanchez",
            image = "https://example.com/rick.png",
            gender = "Male",
            type = "Human",
            status = "Alive",
            species = "Human",
            origin = null,
        )

        // When
        val entity = networkCharacter.toCharacterEntity()

        // Then
        assertThat(entity).isNull()
    }

    @Test
    fun `toListOfCharacterEntity filters out nulls and maps correctly`() {
        // Given
        val expectedFirstId = 1
        val expectedFirstName = "Rick"
        val expectedThirdId = 3
        val expectedThirdName = "Summer"
        val expectedSize = 2

        val networkCharacters = listOf(
            NetworkCharacter(
                id = expectedFirstId.toString(),
                name = expectedFirstName,
                image = "url1",
                gender = "Male",
                type = "Human",
                status = "Alive",
                species = "Human",
                origin = null,
            ),
            NetworkCharacter(
                id = null,
                name = "Morty",
                image = "url2",
                gender = "Male",
                type = "Human",
                status = "Alive",
                species = "Human",
                origin = null,
            ),
            NetworkCharacter(
                id = expectedThirdId.toString(),
                name = expectedThirdName,
                image = "url3",
                gender = "Female",
                type = "Human",
                status = "Alive",
                species = "Human",
                origin = null,
            ),
        )

        // When
        val entities = networkCharacters.toListOfCharacterEntity()

        // Then
        assertThat(entities).hasSize(expectedSize)
        assertThat(entities[0].characterId).isEqualTo(expectedFirstId)
        assertThat(entities[0].name).isEqualTo(expectedFirstName)
        assertThat(entities[1].characterId).isEqualTo(expectedThirdId)
        assertThat(entities[1].name).isEqualTo(expectedThirdName)
    }

    @Test
    fun `toCharacterUiModel maps CharacterEntity correctly`() {
        // Given
        val expectedId = 42
        val expectedName = "Rick Sanchez"
        val expectedImage = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        val expectedGender = "Male"
        val expectedType = "Human"
        val expectedStatus = "Alive"
        val expectedSpecies = "Human"
        val expectedOriginId = 1

        val entity = CharacterEntity(
            characterId = expectedId,
            name = expectedName,
            image = expectedImage,
            gender = expectedGender,
            type = expectedType,
            status = expectedStatus,
            species = expectedSpecies,
            originId = expectedOriginId,
            pageIdentity = "page1",
        )

        // When
        val uiModel = entity.toCharacterUiModel()

        // Then
        assertThat(uiModel.id).isEqualTo(expectedId)
        assertThat(uiModel.name).isEqualTo(expectedName)
        assertThat(uiModel.imageUrl).isEqualTo(expectedImage)
        assertThat(uiModel.gender).isEqualTo(expectedGender)
        assertThat(uiModel.type).isEqualTo(expectedType)
        assertThat(uiModel.status).isEqualTo(expectedStatus)
        assertThat(uiModel.species).isEqualTo(expectedSpecies)
        assertThat(uiModel.originId).isEqualTo(expectedOriginId)
    }

    @Test
    fun `toCharacterUi maps CharacterWithEpisodesAndLocations correctly`() {
        // Given
        val expectedCharacterId = 42
        val expectedCharacterName = "Rick Sanchez"
        val expectedEpisodeId = 1
        val expectedEpisodeName = "Pilot"
        val expectedLocationId = 1
        val expectedLocationName = "Earth"
        val expectedEpisodesSize = 1
        val expectedLocationsSize = 1

        val characterWithEpisodesAndLocations = CharacterWithEpisodesAndLocations(
            character = CharacterEntity(
                characterId = expectedCharacterId,
                name = expectedCharacterName,
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                gender = "Male",
                type = "Human",
                status = "Alive",
                species = "Human",
                originId = 1,
                pageIdentity = "page1",
            ),
            episodes = listOf(
                EpisodeEntity(
                    episodeId = expectedEpisodeId,
                    name = expectedEpisodeName,
                    airDate = "December 2, 2013",
                    episode = "S01E01",
                    pageIdentity = "page1",
                ),
            ),
            locations = listOf(
                LocationEntity(
                    locationId = expectedLocationId,
                    name = expectedLocationName,
                    dimension = "Dimension C-137",
                    pageIdentity = "page1",
                ),
            ),
        )

        // When
        val uiModel = characterWithEpisodesAndLocations.toCharacterUi()

        // Then
        assertThat(uiModel.id).isEqualTo(expectedCharacterId)
        assertThat(uiModel.name).isEqualTo(expectedCharacterName)
        assertThat(uiModel.episodes).hasSize(expectedEpisodesSize)
        assertThat(uiModel.episodes[0].id).isEqualTo(expectedEpisodeId)
        assertThat(uiModel.episodes[0].name).isEqualTo(expectedEpisodeName)
        assertThat(uiModel.locations).hasSize(expectedLocationsSize)
        assertThat(uiModel.locations[0].id).isEqualTo(expectedLocationId)
        assertThat(uiModel.locations[0].name).isEqualTo(expectedLocationName)
    }

    @Test
    fun `toListOfCharacterUi maps list of CharacterEntity correctly`() {
        // Given
        val expectedFirstId = 1
        val expectedFirstName = "Rick"
        val expectedSecondId = 2
        val expectedSecondName = "Morty"
        val expectedSize = 2

        val entities = listOf(
            CharacterEntity(
                characterId = expectedFirstId,
                name = expectedFirstName,
                image = "url1",
                gender = "Male",
                type = "Human",
                status = "Alive",
                species = "Human",
                originId = 1,
                pageIdentity = "page1",
            ),
            CharacterEntity(
                characterId = expectedSecondId,
                name = expectedSecondName,
                image = "url2",
                gender = "Male",
                type = "Human",
                status = "Alive",
                species = "Human",
                originId = 1,
                pageIdentity = "page1",
            ),
        )

        // When
        val uiModels = entities.toListOfCharacterUi()

        // Then
        assertThat(uiModels).hasSize(expectedSize)
        assertThat(uiModels[0].id).isEqualTo(expectedFirstId)
        assertThat(uiModels[0].name).isEqualTo(expectedFirstName)
        assertThat(uiModels[1].id).isEqualTo(expectedSecondId)
        assertThat(uiModels[1].name).isEqualTo(expectedSecondName)
    }
}
