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
package io.davidosemwota.rickandmorty.data.db.entities

import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.mappers.toCharacterUiModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class CharacterEntityTest {

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
        val originId = 22

        val entity = CharacterEntity(
            characterId = id,
            name = name,
            status = status,
            image = image,
            species = species,
            type = type,
            gender = gender,
            pageIdentity = getCharacterIdentifier(1),
            originId = originId,
        )

        // When/Act
        val result = entity.toCharacterUiModel()

        // Then/Assert
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(id)
        assertThat(result.name).isEqualTo(name)
        assertThat(result.gender).isEqualTo(gender)
        assertThat(result.imageUrl).isEqualTo(image)
        assertThat(result.status).isEqualTo(status)
        assertThat(result.type).isEqualTo(type)
    }
}
