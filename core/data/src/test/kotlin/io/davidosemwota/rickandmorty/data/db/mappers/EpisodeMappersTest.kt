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
import io.davidosemwota.rickandmorty.data.db.EpisodeWithCharacters
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.network.NetworkCharacter
import io.davidosemwota.rickandmorty.network.NetworkEpisode
import io.davidosemwota.rickandmorty.network.NetworkLocation
import org.junit.Test

class EpisodeMappersTest {

    @Test
    fun `toEpisodeUiModel maps EpisodeEntity correctly`() {
        // Given
        val expectedId = 1
        val expectedName = "Pilot"
        val expectedAirDate = "December 2, 2013"
        val expectedEpisodeCode = "S01E01"
        val expectedPageIdentity = "page1"
        val expectedCharactersSize = 0

        val entity = EpisodeEntity(
            episodeId = expectedId,
            name = expectedName,
            airDate = expectedAirDate,
            episode = expectedEpisodeCode,
            pageIdentity = expectedPageIdentity,
        )

        // When
        val uiModel = entity.toEpisodeUiModel()

        // Then
        assertThat(uiModel.id).isEqualTo(expectedId)
        assertThat(uiModel.name).isEqualTo(expectedName)
        assertThat(uiModel.airDate).isEqualTo(expectedAirDate)
        assertThat(uiModel.episode).isEqualTo(expectedEpisodeCode)
        assertThat(uiModel.pageIdentity).isEqualTo(expectedPageIdentity)
        assertThat(uiModel.characters).hasSize(expectedCharactersSize)
    }

    @Test
    fun `toListOfEpisodeUi maps list correctly`() {
        // Given
        val expectedFirstId = 1
        val expectedFirstName = "Pilot"
        val expectedSecondId = 2
        val expectedSecondName = "Lawnmower Dog"
        val expectedSize = 2

        val entities = listOf(
            EpisodeEntity(
                episodeId = expectedFirstId,
                name = expectedFirstName,
                airDate = "December 2, 2013",
                episode = "S01E01",
                pageIdentity = "page1",
            ),
            EpisodeEntity(
                episodeId = expectedSecondId,
                name = expectedSecondName,
                airDate = "December 9, 2013",
                episode = "S01E02",
                pageIdentity = "page1",
            ),
        )

        // When
        val uiModels = entities.toListOfEpisodeUi()

        // Then
        assertThat(uiModels).hasSize(expectedSize)
        assertThat(uiModels[0].id).isEqualTo(expectedFirstId)
        assertThat(uiModels[0].name).isEqualTo(expectedFirstName)
        assertThat(uiModels[1].id).isEqualTo(expectedSecondId)
        assertThat(uiModels[1].name).isEqualTo(expectedSecondName)
    }

    @Test
    fun `toEpisodeEntity maps NetworkEpisode correctly`() {
        // Given
        val expectedId = 1
        val expectedName = "Pilot"
        val expectedAirDate = "December 2, 2013"
        val expectedEpisode = "S01E01"
        val expectedPageIdentity = ""

        val networkEpisode = NetworkEpisode(
            id = expectedId.toString(),
            name = expectedName,
            airDate = expectedAirDate,
            episode = expectedEpisode,
            characters = listOf(
                NetworkCharacter(
                    id = "1",
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    gender = "Male",
                    type = "Human",
                    status = "Alive",
                    species = "Human",
                    origin = NetworkLocation(id = "1", name = "Earth", dimension = "C-137"),
                ),
            ),
        )

        // When
        val entity = networkEpisode.toEpisodeEntity()

        // Then
        assertThat(entity).isNotNull()
        entity?.let {
            assertThat(it.episodeId).isEqualTo(expectedId)
            assertThat(it.name).isEqualTo(expectedName)
            assertThat(it.airDate).isEqualTo(expectedAirDate)
            assertThat(it.episode).isEqualTo(expectedEpisode)
            assertThat(it.pageIdentity).isEqualTo(expectedPageIdentity)
        }
    }

    @Test
    fun `toEpisodeEntity returns null when id is null`() {
        // Given
        val networkEpisode = NetworkEpisode(
            id = null,
            name = "Pilot",
            airDate = "December 2, 2013",
            episode = "S01E01",
            characters = emptyList(),
        )

        // When
        val entity = networkEpisode.toEpisodeEntity()

        // Then
        assertThat(entity).isNull()
    }

    @Test
    fun `toListOfEntity filters out nulls and maps correctly`() {
        // Given
        val expectedFirstId = 1
        val expectedSecondId = 3
        val expectedSize = 2

        val networkEpisodes = listOf(
            NetworkEpisode(
                id = expectedFirstId.toString(),
                name = "Pilot",
                airDate = "December 2, 2013",
                episode = "S01E01",
                characters = emptyList(),
            ),
            NetworkEpisode(
                id = null,
                name = "Invalid",
                airDate = "Unknown",
                episode = "S00E00",
                characters = emptyList(),
            ),
            NetworkEpisode(
                id = expectedSecondId.toString(),
                name = "Anatomy Park",
                airDate = "December 16, 2013",
                episode = "S01E03",
                characters = emptyList(),
            ),
        )

        // When
        val entities = networkEpisodes.toListOfEntity()

        // Then
        assertThat(entities).hasSize(expectedSize)
        assertThat(entities[0].episodeId).isEqualTo(expectedFirstId)
        assertThat(entities[1].episodeId).isEqualTo(expectedSecondId)
    }

    @Test
    fun `toEpisodeUi maps EpisodeWithCharacters correctly`() {
        // Given
        val expectedEpisodeId = 1
        val expectedEpisodeName = "Pilot"
        val expectedEpisodeAirDate = "December 2, 2013"
        val expectedEpisodeCode = "S01E01"
        val expectedPageIdentity = "page1"
        val expectedCharacterId = 1
        val expectedCharacterName = "Rick Sanchez"
        val expectedCharactersSize = 1

        val episodeWithCharacters = EpisodeWithCharacters(
            episode = EpisodeEntity(
                episodeId = expectedEpisodeId,
                name = expectedEpisodeName,
                airDate = expectedEpisodeAirDate,
                episode = expectedEpisodeCode,
                pageIdentity = expectedPageIdentity,
            ),
            characters = listOf(
                CharacterEntity(
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
            ),
        )

        // When
        val uiModel = episodeWithCharacters.toEpisodeUi()

        // Then
        assertThat(uiModel.id).isEqualTo(expectedEpisodeId)
        assertThat(uiModel.name).isEqualTo(expectedEpisodeName)
        assertThat(uiModel.airDate).isEqualTo(expectedEpisodeAirDate)
        assertThat(uiModel.episode).isEqualTo(expectedEpisodeCode)
        assertThat(uiModel.pageIdentity).isEqualTo(expectedPageIdentity)
        assertThat(uiModel.characters).hasSize(expectedCharactersSize)
        assertThat(uiModel.characters[0].id).isEqualTo(expectedCharacterId)
        assertThat(uiModel.characters[0].name).isEqualTo(expectedCharacterName)
    }
}
