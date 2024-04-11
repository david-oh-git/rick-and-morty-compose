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
package io.davidosemwota.network

import io.davidosemwota.network.graphql.CharacterListQuery
import io.davidosemwota.network.graphql.type.buildCharacter
import io.davidosemwota.network.graphql.type.buildCharacters
import io.davidosemwota.network.graphql.type.buildEpisode
import io.davidosemwota.network.graphql.type.buildInfo
import io.davidosemwota.network.graphql.type.buildLocation

object FakeApiResponseData {

    fun generateApiResponseData(
        testInfo: NetworkInfo,
        testResults: List<NetworkCharacter>,
    ): CharacterListQuery.Data {
        return CharacterListQuery.Data {
            characters = buildCharacters {
                info = buildInfo {
                    count = testInfo.count
                    pages = testInfo.pages
                    next = testInfo.next
                    prev = testInfo.prev
                }
                results = testResults.map { character ->
                    buildCharacter {
                        id = character.id
                        name = character.name
                        status = character.status
                        image = character.image
                        species = character.species
                        type = character.type
                        gender = character.gender
                        origin = buildLocation {
                            id = character.origin?.id
                            name = character.origin?.name
                            dimension = character.origin?.dimension
                        }
                        location = buildLocation {
                            id = character.location?.id
                            name = character.location?.name
                            dimension = character.location?.dimension
                            residents = character.location?.residents?.map { resident ->
                                buildCharacter {
                                    id = resident.id
                                    name = resident.name
                                    image = resident.image
                                }
                            } ?: emptyList()
                        }
                        episode = character.episode.map {
                            buildEpisode {
                                id = it.id
                                episode = it.episode
                                name = it.name
                                characters = it.characters.map { episodeCharacters ->
                                    buildCharacter {
                                        id = episodeCharacters.id
                                        image = episodeCharacters.image
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    val responseData = CharacterListQuery.Data {
        characters = buildCharacters {
            info = buildInfo {
                count = 826
                pages = 42
                next = 2
                prev = null
            }
            results = listOf(
                buildCharacter {
                    id = "1"
                    name = "Rick Sanchez"
                    status = "Alive"
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    species = "Human"
                    type = ""
                    gender = "Male"
                    origin = buildLocation {
                        id = "1"
                        name = "Earth (c-137)"
                        dimension = "Dimension C-137"
                    }
                    location = buildLocation {
                        id = "3"
                        name = "Citadel of Ricks"
                        dimension = "unknown"
                        residents = listOf(
                            buildCharacter {
                                id = "1"
                                name = "Rick Sanchez"
                                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                            },
                            buildCharacter {
                                id = "2"
                                name = "Morty Smith"
                                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
                            },
                        )
                    }

                    episode = listOf(
                        buildEpisode {
                            id = "1"
                            episode = "S01E01"
                            name = "Pilot"
                        },
                    )
                },
            )
        }
    }
}
