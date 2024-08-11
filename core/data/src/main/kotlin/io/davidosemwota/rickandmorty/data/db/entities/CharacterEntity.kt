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
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.models.Episode
import io.davidosemwota.rickandmorty.models.Location
import io.davidosemwota.rickandmorty.models.Origin
import io.davidosemwota.rickandmorty.network.NetworkCharacter
import io.davidosemwota.rickandmorty.network.NetworkEpisode
import io.davidosemwota.rickandmorty.network.NetworkResident
import kotlinx.serialization.Serializable

@Entity(tableName = "character_entity_table")
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: String,
    val image: String,
    val species: String,
    val type: String,
    val gender: String,
    val pageIdentity: String,
    @Embedded(prefix = "origin_") val origin: CharacterOriginEntity,
    @Embedded(prefix = "location_") val location: CharacterLocationEntity,
    @ColumnInfo(name = "character_episodes") val episodes: List<CharacterEpisodeEntity>,
)

data class CharacterOriginEntity(
    val id: String,
    val name: String,
    val dimension: String,
)

data class CharacterLocationEntity(
    val id: String,
    val name: String,
    val dimension: String,
    val residents: List<CharacterResidentEntity>,
)

@Serializable
data class CharacterResidentEntity(
    @ColumnInfo(name = "resident_id") val id: String,
    @ColumnInfo(name = "resident_name") val name: String,
    @ColumnInfo(name = "resident_image") val image: String,
)

@Serializable
data class CharacterEpisodeEntity(
    @ColumnInfo(name = "episode_id") val id: String,
    @ColumnInfo(name = "episode_name") val name: String,
)

/**
 *  Convert [CharacterEntity] object to [Character] object.
 */
internal fun CharacterEntity.toUiModel(): Character = Character(
    id = this.id,
    name = this.name,
    status = this.status,
    imageUrl = this.image,
    species = this.species,
    type = this.type,
    gender = this.gender,
    origin = Origin(
        id = this.origin.id,
        name = this.origin.name,
        dimension = this.origin.dimension,
    ),
    location = Location(
        id = this.location.id,
        name = this.location.name,
        dimension = this.location.dimension,
        residents = this.location.residents.map(::characterResidentEntityToCharacter),
    ),
    episodes = this.episodes.map(::characterEpisodeEntityToEpisode),
)

internal fun NetworkCharacter.toEntity(): CharacterEntity = CharacterEntity(
    id = this.id.toInt(),
    name = this.name,
    image = this.image,
    gender = this.gender,
    type = this.type,
    status = this.status,
    species = this.status,
    origin = CharacterOriginEntity(
        id = this.origin?.id.orEmpty(),
        name = this.origin?.name.orEmpty(),
        dimension = this.origin?.dimension.orEmpty(),
    ),
    location = CharacterLocationEntity(
        id = this.location?.id.orEmpty(),
        name = this.location?.name.orEmpty(),
        dimension = this.location?.dimension.orEmpty(),
        residents = this.location?.residents?.map(::networkResidentToCharacterResidentEntity) ?: emptyList(),
    ),
    episodes = this.episode.map(::networkEpisodeToCharacterEpisodeEntity),
    pageIdentity = "",
)

private fun networkEpisodeToCharacterEpisodeEntity(
    networkEpisode: NetworkEpisode,
): CharacterEpisodeEntity = CharacterEpisodeEntity(
    id = networkEpisode.id,
    name = networkEpisode.name,
)

private fun networkResidentToCharacterResidentEntity(
    networkResident: NetworkResident,
): CharacterResidentEntity = CharacterResidentEntity(
    id = networkResident.id,
    name = networkResident.name,
    image = networkResident.image,
)

private fun characterEpisodeEntityToEpisode(
    characterEpisodeEntity: CharacterEpisodeEntity,
): Episode = Episode(
    id = characterEpisodeEntity.id,
    name = characterEpisodeEntity.name,
)

private fun characterResidentEntityToCharacter(
    characterResidentEntity: CharacterResidentEntity,
): Character = Character(
    id = characterResidentEntity.id.toInt(),
    name = characterResidentEntity.name,
    imageUrl = characterResidentEntity.image,
)
