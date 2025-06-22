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
package io.davidosemwota.rickandmorty.data.db.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LocationEntityTest {

    @Test
    fun `create LocationEntity with valid values`() {
        // Given
        val id = 1
        val name = "Earth"
        val dimension = "Dimension C-137"
        val pageIdentity = "page_1"

        // When
        val locationEntity = LocationEntity(
            locationId = id,
            name = name,
            dimension = dimension,
            pageIdentity = pageIdentity,
        )

        // Then
        assertThat(locationEntity.locationId).isEqualTo(id)
        assertThat(locationEntity.name).isEqualTo(name)
        assertThat(locationEntity.dimension).isEqualTo(dimension)
        assertThat(locationEntity.pageIdentity).isEqualTo(pageIdentity)
    }

    @Test
    fun `equals returns true for identical objects`() {
        // Given
        val location1 = LocationEntity(
            locationId = 1,
            name = "Earth",
            dimension = "Dimension C-137",
            pageIdentity = "page_1",
        )
        val location2 = LocationEntity(
            locationId = 1,
            name = "Earth",
            dimension = "Dimension C-137",
            pageIdentity = "page_1",
        )

        // Then
        assertThat(location1).isEqualTo(location2)
        assertThat(location1.hashCode()).isEqualTo(location2.hashCode())
    }

    @Test
    fun `equals returns false for different objects`() {
        // Given
        val location1 = LocationEntity(
            locationId = 1,
            name = "Earth",
            dimension = "Dimension C-137",
            pageIdentity = "page_1",
        )
        val location2 = LocationEntity(
            locationId = 2,
            name = "Earth",
            dimension = "Dimension C-137",
            pageIdentity = "page_1",
        )

        // Then
        assertThat(location1).isNotEqualTo(location2)
    }

    @Test
    fun `verify LOCATION_ENTITY_TABLE_NAME constant value`() {
        // Then
        assertThat(LOCATION_ENTITY_TABLE_NAME).isEqualTo("location_entity_table")
    }
}
