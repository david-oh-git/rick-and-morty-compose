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
import org.junit.Test

class CharacterAndEpisodeEntityRefTest {

    @Test
    fun `when created should set properties correctly`() {
        // Given
        val characterId = 123
        val episodeId = 456

        // When
        val entityRef = CharacterAndEpisodeEntityRef(
            characterId = characterId,
            episodeId = episodeId,
        )

        // Then
        assertThat(entityRef.characterId).isEqualTo(characterId)
        assertThat(entityRef.episodeId).isEqualTo(episodeId)
    }

    @Test
    fun `should have correct table name constant`() {
        // Then
        assertThat(CHARACTER_EPISODE_REF_TABLE).isEqualTo("character_episode_ref_table")
    }

    @Test
    fun `entities with same IDs should be equal`() {
        // Given
        val entityRef1 = CharacterAndEpisodeEntityRef(1, 2)
        val entityRef2 = CharacterAndEpisodeEntityRef(1, 2)

        // Then
        assertThat(entityRef1).isEqualTo(entityRef2)
    }

    @Test
    fun `entities with different IDs should not be equal`() {
        // Given
        val entityRef1 = CharacterAndEpisodeEntityRef(1, 2)
        val entityRef2 = CharacterAndEpisodeEntityRef(1, 3)
        val entityRef3 = CharacterAndEpisodeEntityRef(2, 2)

        // Then
        assertThat(entityRef1).isNotEqualTo(entityRef2)
        assertThat(entityRef1).isNotEqualTo(entityRef3)
        assertThat(entityRef2).isNotEqualTo(entityRef3)
    }
}
