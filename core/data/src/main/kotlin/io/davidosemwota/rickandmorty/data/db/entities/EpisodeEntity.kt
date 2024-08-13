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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 *  @param id unique id for entity, also serves as primary key
 *  @param airDate Episode first aired date.
 *  @param episode Episode code eg S01E01
 *  @param name Episode name
 *  @param pageIdentity Unique identifier for associated [PageInfoEntity].
 *  @param characters Episode characters.
 */
@Entity(tableName = "episode_entity_table")
data class EpisodeEntity(
    @PrimaryKey
    val id: Int,
    val airDate: String,
    val episode: String,
    val name: String,
    val pageIdentity: String,
    @ColumnInfo(name = "episode_characters") val characters: List<EpisodeCharacter>,
)

@Serializable
data class EpisodeCharacter(
    @ColumnInfo(name = "character_id") val id: Int,
    @ColumnInfo(name = "character_name") val name: String,
    @ColumnInfo(name = "character_image") val image: String,
)
