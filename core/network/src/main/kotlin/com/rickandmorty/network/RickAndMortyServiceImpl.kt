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
package com.rickandmorty.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.davidosemwota.rickandmorty.network.graphql.CharacterListQuery
import io.davidosemwota.rickandmorty.network.graphql.type.FilterCharacter

class RickAndMortyServiceImpl constructor(
    private val apolloClient: ApolloClient = ApolloFactory.provideApolloClient(),
) : RickAndMortyApiService {

    override suspend fun getCharacters(
        page: Int?,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?,
    ): CharacterListResponse {
        val filterCharacter = FilterCharacter(
            Optional.presentIfNotNull(name),
            Optional.presentIfNotNull(status),
            Optional.presentIfNotNull(species),
            Optional.presentIfNotNull(type),
            Optional.presentIfNotNull(gender),
        )
        try {
            val response = apolloClient.query(
                CharacterListQuery(
                    Optional.presentIfNotNull(page),
                    Optional.presentIfNotNull(filterCharacter),
                ),
            ).execute()

            response.errors
            if (response.hasErrors()) {
                return response.data?.characters.toCharactersResponse().copy(
                    errorResponse = response.exception?.let {
                        ErrorResponse(it, response.errors?.toListOfResponseError())
                    },
                )
            }

            return response.data?.characters.toCharactersResponse().copy(
                errorResponse = response.exception?.let { ErrorResponse(it) },
            )
        } catch (ex: Exception) {
            return CharacterListResponse(
                errorResponse = ErrorResponse(ex),
            )
        }
    }
}
