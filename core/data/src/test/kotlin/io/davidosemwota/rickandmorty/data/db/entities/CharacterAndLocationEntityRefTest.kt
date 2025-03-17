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

class CharacterAndLocationEntityRefTest {

    @Test
    fun `create CharacterAndLocationEntityRef with valid values`() {
        // Given
        val characterId = 1
        val locationId = 100

        // When
        val entityRef = CharacterAndLocationEntityRef(
            characterId = characterId,
            locationId = locationId,
        )

        // Then
        assertThat(entityRef.characterId).isEqualTo(characterId)
        assertThat(entityRef.locationId).isEqualTo(locationId)
    }

    @Test
    fun `equals returns true for identical objects`() {
        // Given
        val entityRef1 = CharacterAndLocationEntityRef(characterId = 1, locationId = 100)
        val entityRef2 = CharacterAndLocationEntityRef(characterId = 1, locationId = 100)

        // Then
        assertThat(entityRef1).isEqualTo(entityRef2)
        assertThat(entityRef1.hashCode()).isEqualTo(entityRef2.hashCode())
    }

    @Test
    fun `equals returns false for different objects`() {
        // Given
        val entityRef1 = CharacterAndLocationEntityRef(characterId = 1, locationId = 100)
        val entityRef2 = CharacterAndLocationEntityRef(characterId = 2, locationId = 100)
        val entityRef3 = CharacterAndLocationEntityRef(characterId = 1, locationId = 200)

        // Then
        assertThat(entityRef1).isNotEqualTo(entityRef2)
        assertThat(entityRef1).isNotEqualTo(entityRef3)
    }

    @Test
    fun `verify CHARACTER_LOCATION_REF_TABLE constant value`() {
        // Then
        assertThat(CHARACTER_LOCATION_REF_TABLE).isEqualTo("character_location_ref_table")
    }
}
