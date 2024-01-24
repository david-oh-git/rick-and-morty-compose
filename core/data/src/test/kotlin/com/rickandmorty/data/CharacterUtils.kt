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
package com.rickandmorty.data

import com.rickandmorty.data.db.CharacterEntity
import com.rickandmorty.data.db.CharacterEpisodeEntity
import com.rickandmorty.data.db.CharacterLocationEntity
import com.rickandmorty.data.db.CharacterOriginEntity
import com.rickandmorty.data.db.CharacterResidentEntity

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

internal fun generateCharacters(): List<CharacterEntity> {
    return listOf(
        CharacterEntity(
            id = "1",
            name = "Rick Sanchez",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            origin = CharacterOriginEntity(
                id = "40",
                name = "C -137 Earth",
                dimension = "C-137",
            ),
            location = CharacterLocationEntity(
                id = "22",
                name = "londoninion",
                dimension = "C-137",
                residents = listOf(
                    CharacterResidentEntity(
                        id = "34",
                        name = "Evil morty",
                        image = "image url",
                    ),
                ),
            ),
            episodes = listOf(
                CharacterEpisodeEntity(
                    id = "56",
                    name = "S03E09",
                ),
            ),
        ),
        CharacterEntity(
            id = "2",
            name = "Morty Smith",
            status = "Alive",
            image = "http://image_url",
            species = "Human",
            type = "whatever type",
            gender = "male",
            origin = CharacterOriginEntity(
                id = "40",
                name = "C -137 Earth",
                dimension = "C-137",
            ),
            location = CharacterLocationEntity(
                id = "22",
                name = "londoninion",
                dimension = "C-137",
                residents = listOf(
                    CharacterResidentEntity(
                        id = "34",
                        name = "Evil morty",
                        image = "image url",
                    ),
                ),
            ),
            episodes = listOf(
                CharacterEpisodeEntity(
                    id = "56",
                    name = "S03E09",
                ),
                CharacterEpisodeEntity(
                    id = "16",
                    name = "S01E09",
                ),
            ),
        ),
    )
}
