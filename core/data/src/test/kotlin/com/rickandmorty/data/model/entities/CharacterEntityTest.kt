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
package com.rickandmorty.data.model.entities

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharacterEntityTest {

    @Test
    fun testEntityToModelExtension() = runTest {
        // Given/Arrange
        val episodeOneId = "55"
        val episodeOneName = "S02E07"
        val id = 3
        val name = "Kava stone"
        val status = "status"
        val image = "http://bla.jpeg"
        val species = "alien"
        val type = "TYPE"
        val gender = "unknown"
        val characterOriginEntity = CharacterOriginEntity(
            id = "23",
            name = "earth",
            dimension = "c-137",
        )
        val locationId = "21"
        val locationName = "Ajegunle"
        val locationDimension = "c-137"
        val locationEntity = CharacterLocationEntity(
            id = locationId,
            name = locationName,
            dimension = locationDimension,
            residents = listOf(
                CharacterResidentEntity(
                    id = "23",
                    name = "Morty smith",
                    image = "image_url_morty",
                ),
            ),
        )
        val episodes = listOf(
            CharacterEpisodeEntity(
                id = episodeOneId,
                name = episodeOneName,
            ),
        )

        val entity = CharacterEntity(
            id = id,
            name = name,
            status = status,
            image = image,
            species = species,
            type = type,
            gender = gender,
            origin = characterOriginEntity,
            location = locationEntity,
            episodes = episodes,
        )

        // When/Act
        val result = entity.toModel()

        // Then/Assert
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(id)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.gender).isEqualTo(gender)
        assertThat(result.imageUrl).isEqualTo(image)
        assertThat(result.status).isEqualTo(status)
        assertThat(result.type).isEqualTo(type)

        assertThat(result.episodes).isNotEmpty()
        assertThat(result.episodes[0]).isNotNull()
        assertThat(result.episodes[0].id).isEqualTo(episodeOneId)
        assertThat(result.episodes[0].name).isEqualTo(episodeOneName)

        assertThat(result.location).isNotNull()
        val location = result.location
        assertThat(location?.id).isEqualTo(locationId)
        assertThat(location?.name).isEqualTo(locationName)
        assertThat(location?.dimension).isEqualTo(locationDimension)
        assertThat(location?.residents).isNotEmpty()
        assertThat(location?.residents?.size).isEqualTo(1)
    }
}
