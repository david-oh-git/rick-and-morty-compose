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
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity
import io.davidosemwota.rickandmorty.network.NetworkLocation
import org.junit.Test

class LocationMappersTest {

    @Test
    fun `toLocationEntity maps NetworkLocation correctly with pageIdentity`() {
        // Given
        val expectedId = 42
        val expectedName = "Earth"
        val expectedDimension = "Dimension C-137"
        val expectedPageIdentity = "page1"

        val networkLocation = NetworkLocation(
            id = expectedId.toString(),
            name = expectedName,
            dimension = expectedDimension,
        )

        // When
        val entity = networkLocation.toLocationEntity(pageIdentity = expectedPageIdentity)

        // Then
        assertThat(entity).isNotNull()
        entity?.let {
            assertThat(it.locationId).isEqualTo(expectedId)
            assertThat(it.name).isEqualTo(expectedName)
            assertThat(it.dimension).isEqualTo(expectedDimension)
            assertThat(it.pageIdentity).isEqualTo(expectedPageIdentity)
        }
    }

    @Test
    fun `toLocationEntity maps NetworkLocation correctly with default pageIdentity`() {
        // Given
        val expectedId = 42
        val expectedName = "Earth"
        val expectedDimension = "Dimension C-137"
        val expectedPageIdentity = ""

        val networkLocation = NetworkLocation(
            id = expectedId.toString(),
            name = expectedName,
            dimension = expectedDimension,
        )

        // When
        val entity = networkLocation.toLocationEntity()

        // Then
        assertThat(entity).isNotNull()
        entity?.let {
            assertThat(it.locationId).isEqualTo(expectedId)
            assertThat(it.name).isEqualTo(expectedName)
            assertThat(it.dimension).isEqualTo(expectedDimension)
            assertThat(it.pageIdentity).isEqualTo(expectedPageIdentity)
        }
    }

    @Test
    fun `toLocationEntity returns null when id is null`() {
        // Given
        val networkLocation = NetworkLocation(
            id = null,
            name = "Unknown Planet",
            dimension = "Unknown Dimension",
        )

        // When
        val entity = networkLocation.toLocationEntity()

        // Then
        assertThat(entity).isNull()
    }

    @Test
    fun `toLocationUi maps LocationEntity correctly`() {
        // Given
        val expectedId = 42
        val expectedName = "Earth"
        val expectedDimension = "Dimension C-137"

        val locationEntity = LocationEntity(
            locationId = expectedId,
            name = expectedName,
            dimension = expectedDimension,
            pageIdentity = "page1",
        )

        // When
        val uiModel = locationEntity.toLocationUi()

        // Then
        assertThat(uiModel.id).isEqualTo(expectedId)
        assertThat(uiModel.name).isEqualTo(expectedName)
        assertThat(uiModel.dimension).isEqualTo(expectedDimension)
        assertThat(uiModel.residents).isEmpty()
    }

    @Test
    fun `toListOfLocationUi maps list of LocationEntity correctly`() {
        // Given
        val expectedFirstId = 1
        val expectedFirstName = "Earth"
        val expectedSecondId = 2
        val expectedSecondName = "Mars"
        val expectedSize = 2

        val entities = listOf(
            LocationEntity(
                locationId = expectedFirstId,
                name = expectedFirstName,
                dimension = "Dimension C-137",
                pageIdentity = "page1",
            ),
            LocationEntity(
                locationId = expectedSecondId,
                name = expectedSecondName,
                dimension = "Dimension C-500",
                pageIdentity = "page1",
            ),
        )

        // When
        val uiModels = entities.toListOfLocationUi()

        // Then
        assertThat(uiModels).hasSize(expectedSize)
        assertThat(uiModels[0].id).isEqualTo(expectedFirstId)
        assertThat(uiModels[0].name).isEqualTo(expectedFirstName)
        assertThat(uiModels[1].id).isEqualTo(expectedSecondId)
        assertThat(uiModels[1].name).isEqualTo(expectedSecondName)
    }
}
