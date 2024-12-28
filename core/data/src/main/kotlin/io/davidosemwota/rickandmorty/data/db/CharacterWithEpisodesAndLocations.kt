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
package io.davidosemwota.rickandmorty.data.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndEpisodeEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndLocationEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity

data class CharacterWithEpisodesAndLocations(
    @Embedded val character: CharacterEntity,

    @Relation(
        parentColumn = "characterId",
        entityColumn = "episodeId",
        associateBy = Junction(
            value = CharacterAndEpisodeEntityRef::class,
            parentColumn = "characterId",
            entityColumn = "episodeId",
        ),
    )
    val episodes: List<EpisodeEntity>,

    @Relation(
        parentColumn = "characterId",
        entityColumn = "locationId",
        associateBy = Junction(
            value = CharacterAndLocationEntityRef::class,
            parentColumn = "characterId",
            entityColumn = "locationId",
        ),
    )
    val locations: List<LocationEntity>,
)
