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
package io.davidosemwota.rickandmorty.data

import io.davidosemwota.rickandmorty.data.db.entities.CharacterEntity
import io.davidosemwota.rickandmorty.data.db.entities.getCharacterIdentifier

private fun generateFirstName(): String = listOf(
    "Rick",
    "Jerry",
    "Summer",
).random()

private fun generateLastName(): String = listOf(
    "Sanchez",
    "Smith",
    "Summer",
).random()

internal fun generateCharacters(
    startId: Int = 1,
): List<CharacterEntity> {
    return listOf(
        CharacterEntity(
            characterId = startId,
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 1,
        ),
        CharacterEntity(
            characterId = startId + 1,
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            pageIdentity = getCharacterIdentifier(1),
            originId = 2,
        ),
    )
}
