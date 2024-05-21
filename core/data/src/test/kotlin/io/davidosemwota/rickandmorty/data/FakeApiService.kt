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

import io.davidosemwota.rickandmorty.network.NetworkCharacter
import io.davidosemwota.rickandmorty.network.NetworkCharacterListResponse
import io.davidosemwota.rickandmorty.network.NetworkError
import io.davidosemwota.rickandmorty.network.NetworkErrorResponse
import io.davidosemwota.rickandmorty.network.NetworkInfo
import io.davidosemwota.rickandmorty.network.NetworkLocation
import io.davidosemwota.rickandmorty.network.NetworkOrigin
import io.davidosemwota.rickandmorty.network.NetworkResident
import io.davidosemwota.rickandmorty.network.RickAndMortyApiService
import java.io.IOException

object FakeApiService {

    private val names: List<String> = listOf(
        "Rick",
        "Andrea",
        "Shola",
        "Bukky",
        "Elo",
        "Oma",
        "Nedu",
        "Akpan",
        "Morty",
    )

    private val lastNames: List<String> = listOf(
        "Davenport",
        "Smith",
        "Osemwota",
        "Juniper",
        "Cisco",
    )

    private val ids: List<String> = (1..100).map { it.toString() }

    private val info = NetworkInfo(
        count = 826,
        pages = 40,
        next = 2,
        prev = null,
    )
    private val rickSanchez = NetworkCharacter(
        id = "1",
        image = "https:LINK_TO_IMAGE",
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = NetworkOrigin(
            id = "1",
            name = "Earth (c-137)",
            dimension = "Dimension c-137",
        ),
        location = NetworkLocation(
            id = "3",
            name = "Citadel of ricks",
            dimension = "unknown",
            residents = listOf(
                NetworkResident(
                    id = "2",
                    name = "Morty Smith",
                    image = "http IMAGE URL",
                ),
            ),
        ),
    )

    private fun generateRandomCharacter(
        id: String,
        name: String = "${names.random()} ${lastNames.random()}",
    ): NetworkCharacter {
        if (id == "1") {
            return rickSanchez
        }
        if (id == "2") {
            return morty
        }

        return NetworkCharacter(
            id = "1",
            image = "https:LINK_TO_IMAGE",
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = NetworkOrigin(
                id = "1",
                name = "Earth (c-137)",
                dimension = "Dimension c-137",
            ),
            location = NetworkLocation(
                id = "3",
                name = "Citadel of ricks",
                dimension = "unknown",
                residents = listOf(
                    NetworkResident(
                        id = "2",
                        name = "Morty Smith",
                        image = "http IMAGE URL",
                    ),
                ),
            ),
        )
    }

    private val morty = rickSanchez.copy(
        id = "2",
        name = "Morty Smith",
    )

    val successWithMoreData = object : RickAndMortyApiService {
        override suspend fun getCharacters(
            page: Int?,
            name: String?,
            status: String?,
            species: String?,
            type: String?,
            gender: String?,
        ): NetworkCharacterListResponse {
            return NetworkCharacterListResponse(
                errorResponse = null,
                info = info,
                results = listOf(rickSanchez, morty),
            )
        }
    }

    val successWithNoMoreData = object : RickAndMortyApiService {
        override suspend fun getCharacters(
            page: Int?,
            name: String?,
            status: String?,
            species: String?,
            type: String?,
            gender: String?,
        ): NetworkCharacterListResponse {
            val characters = mutableListOf<NetworkCharacter>()
            for (num in 1..50) {
                characters.add(generateRandomCharacter(num.toString()))
            }
            return NetworkCharacterListResponse(
                errorResponse = null,
                info = info.copy(next = null, prev = 5),
                results = listOf(rickSanchez, morty),
            )
        }
    }

    fun getNetworkResponseSuccess(
        numberOfCharacters: Int = 10,
    ): RickAndMortyApiService {
        return object : RickAndMortyApiService {

            override suspend fun getCharacters(
                page: Int?,
                name: String?,
                status: String?,
                species: String?,
                type: String?,
                gender: String?,
            ): NetworkCharacterListResponse {
                val characters = mutableListOf<NetworkCharacter>()
                for (num in 1..numberOfCharacters) {
                    System.out.println("Character No: $num")
                    characters.add(generateRandomCharacter(num.toString()))
                }
                return NetworkCharacterListResponse(
                    errorResponse = null,
                    info = info,
                    results = characters,
                )
            }
        }
    }

    val errorResponse = object : RickAndMortyApiService {
        override suspend fun getCharacters(
            page: Int?,
            name: String?,
            status: String?,
            species: String?,
            type: String?,
            gender: String?,
        ): NetworkCharacterListResponse {
            return NetworkCharacterListResponse(
                errorResponse = NetworkErrorResponse(
                    exception = IOException(),
                    errors = listOf(
                        NetworkError(
                            serverErrorMessage = "",
                            code = "504",
                        ),
                    ),
                ),
                info = null,
                results = null,
            )
        }
    }
}
