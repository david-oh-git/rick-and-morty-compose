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
package io.davidosemwota.rickandmorty.data.db.mappers

import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterLocationEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterOriginEntity
import io.davidosemwota.rickandmorty.data.db.entities.CharacterResidentEntity
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.models.Location
import io.davidosemwota.rickandmorty.models.Origin
import io.davidosemwota.rickandmorty.network.NetworkCharacter
import io.davidosemwota.rickandmorty.network.NetworkResident

internal fun NetworkCharacter.toCharacterEntity(): CharacterEntity = CharacterEntity(
    characterId = this.id.toInt(),
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
    pageIdentity = "",
)

internal fun List<NetworkCharacter>.toListOfCharacterEntity(): List<CharacterEntity> =
    this.map { it.toCharacterEntity() }

/**
 *  Convert [CharacterEntity] object to [Character] object.
 */
internal fun CharacterEntity.toCharacterUiModel(): Character = Character(
    id = this.characterId,
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
)

private fun networkResidentToCharacterResidentEntity(
    networkResident: NetworkResident,
): CharacterResidentEntity = CharacterResidentEntity(
    id = networkResident.id,
    name = networkResident.name,
    image = networkResident.image,
)

private fun characterResidentEntityToCharacter(
    characterResidentEntity: CharacterResidentEntity,
): Character = Character(
    id = characterResidentEntity.id.toInt(),
    name = characterResidentEntity.name,
    imageUrl = characterResidentEntity.image,
)
