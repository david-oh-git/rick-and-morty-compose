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

import io.davidosemwota.rickandmorty.data.db.CharacterWithEpisodesAndLocations
import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.models.Character
import io.davidosemwota.rickandmorty.network.NetworkCharacter

internal fun NetworkCharacter.toCharacterEntity(): CharacterEntity? =
    this.id?.let {
        CharacterEntity(
            characterId = it.toInt(),
            name = this.name.toString(),
            image = this.image.toString(),
            gender = this.gender.toString(),
            type = this.type.toString(),
            status = this.status.toString(),
            species = this.status.toString(),
            originId = this.origin?.id?.toInt() ?: -1,
            pageIdentity = "",
        )
    }

internal fun List<NetworkCharacter>.toListOfCharacterEntity(): List<CharacterEntity> =
    this.map { it.toCharacterEntity() }.filterNotNull()

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
    originId = this.originId,
)

internal fun CharacterWithEpisodesAndLocations.toCharacterUi(): Character = Character(
    id = this.character.characterId,
    name = this.character.name,
    status = this.character.status,
    imageUrl = this.character.image,
    species = this.character.species,
    type = this.character.type,
    gender = this.character.gender,
    originId = this.character.originId,
    locations = this.locations.toListOfLocationUi(),
    episodes = this.episodes.toListOfEpisodeUi(),
)

internal fun List<CharacterEntity>.toListOfCharacterUi(): List<Character> =
    this.map { it.toCharacterUiModel() }
