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
package io.davidosemwota.rickandmorty.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import io.davidosemwota.rickandmorty.network.graphql.CharacterListQuery
import io.davidosemwota.rickandmorty.network.graphql.EpisodesQuery
import io.davidosemwota.rickandmorty.network.graphql.type.FilterCharacter
import io.davidosemwota.rickandmorty.network.graphql.type.FilterEpisode
import javax.inject.Inject

class RickAndMortyServiceImpl @Inject constructor(
    private val apolloClient: ApolloClient,
) : RickAndMortyApiService {

    override suspend fun getCharacters(
        page: Int?,
        name: String?,
        status: String?,
        species: String?,
        type: String?,
        gender: String?,
    ): NetworkCharacterListResponse {
        try {
            val filterCharacter = FilterCharacter(
                Optional.presentIfNotNull(name),
                Optional.presentIfNotNull(status),
                Optional.presentIfNotNull(species),
                Optional.presentIfNotNull(type),
                Optional.presentIfNotNull(gender),
            )
            val response = apolloClient.query(
                CharacterListQuery(
                    Optional.presentIfNotNull(page),
                    Optional.presentIfNotNull(filterCharacter),
                ),
            ).execute()

            response.errors
            if (response.hasErrors()) {
                return response.data?.characters.toNetworkCharacterListResponse().copy(
                    errorResponse = response.exception?.let {
                        NetworkErrorResponse(it, response.errors?.toListOfNetworkError())
                    },
                )
            }

            return response.data?.characters.toNetworkCharacterListResponse().copy(
                errorResponse = response.exception?.let { NetworkErrorResponse(it) },
            )
        } catch (ex: Exception) {
            return NetworkCharacterListResponse(
                errorResponse = NetworkErrorResponse(ex),
            )
        }
    }

    override suspend fun getEpisodes(
        page: Int?,
        name: String?,
        episode: String?,
    ): NetworkEpisodeListResponse {
        try {
            val filterEpisode = FilterEpisode(
                Optional.presentIfNotNull(name),
                Optional.presentIfNotNull(episode),
            )

            val response = apolloClient.query(
                EpisodesQuery(
                    Optional.presentIfNotNull(page),
                    Optional.presentIfNotNull(filterEpisode),
                ),
            ).execute()
            if (response.hasErrors()) {
                return NetworkEpisodeListResponse(
                    errorResponse = response.exception?.let {
                        NetworkErrorResponse(it, response.errors?.toListOfNetworkError())
                    },
                )
            }

            return response.data?.episodes.toNetworkEpisodeListResponse().copy(
                errorResponse = response.exception?.let { NetworkErrorResponse(it) },
            )
        } catch (ex: Exception) {
            return NetworkEpisodeListResponse(
                errorResponse = NetworkErrorResponse(ex),
            )
        }
    }
}
