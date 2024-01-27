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

import com.rickandmorty.network.NetworkCharacter
import com.rickandmorty.network.NetworkCharacterListResponse
import com.rickandmorty.network.NetworkError
import com.rickandmorty.network.NetworkErrorResponse
import com.rickandmorty.network.NetworkInfo
import com.rickandmorty.network.NetworkLocation
import com.rickandmorty.network.NetworkOrigin
import com.rickandmorty.network.NetworkResident
import com.rickandmorty.network.RickAndMortyApiService
import java.io.IOException

object FakeApiService {

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
            return NetworkCharacterListResponse(
                errorResponse = null,
                info = info.copy(next = null, prev = 5),
                results = listOf(rickSanchez, morty),
            )
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
