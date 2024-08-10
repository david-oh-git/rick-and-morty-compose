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
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.api.Error
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.network.graphql.CharacterListQuery
import io.davidosemwota.rickandmorty.network.graphql.EpisodesQuery
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 *  Test for [RickAndMortyApiService]
 */

@OptIn(ApolloExperimental::class)
class RickAndMortyApiServiceTest {

    private lateinit var rickAndMortyApiService: RickAndMortyApiService
    private lateinit var apolloClient: ApolloClient

    @Before
    fun setUp() {
        apolloClient = ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .build()

        rickAndMortyApiService = RickAndMortyServiceImpl(apolloClient)
    }

    // Character tests
    @Test
    fun getCharacters_NoQuery_NoFilter_Success() = runTest {
        val testQuery = CharacterListQuery()
        val info = NetworkInfo(
            count = 826,
            pages = 40,
            next = 2,
            prev = null,
        )
        val characters = mutableListOf<NetworkCharacter>()
        val rickSanchez = NetworkCharacter(
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
        characters.add(rickSanchez)
        apolloClient.enqueueTestResponse(
            testQuery,
            FakeApiResponseData.generateApiResponseData(info, characters),
        )

        val response = rickAndMortyApiService.getCharacters()
        assertThat(response.info).isNotNull()
        assertThat(response.info?.count).isEqualTo(info.count)
        assertThat(response.info?.pages).isEqualTo(info.pages)
        assertThat(response.info?.next).isEqualTo(info.next)
        assertThat(response.results?.size).isEqualTo(characters.size)

        val character = response.results?.get(0)

        val location = character?.location
        assertThat(location?.dimension).isEqualTo(rickSanchez.location?.dimension)
        assertThat(response.results?.get(0)).isEqualTo(rickSanchez)
    }

    @Test
    fun getCharacters_NoQuery_NoFilter_Error_Response() = runTest {
        val testQuery = CharacterListQuery()
        val errorMessage = "Expected type Int, found 99999999999999999999999999999999999999999999."

        apolloClient.enqueueTestResponse(
            operation = testQuery,
            errors = listOf(
                com.apollographql.apollo3.api.Error.Builder(errorMessage)
                    .locations(listOf(Error.Location(1, 34)))
                    .putExtension("code", "GRAPHQL_VALIDATION_FAILED")
                    .build(),
            ),
        )

        val response = rickAndMortyApiService.getCharacters()
        assertThat(response.errorResponse?.errors).isNotNull()
        val responseErrorMessage = response.errorResponse?.errors?.get(0)?.serverErrorMessage
        assertThat(responseErrorMessage).isNotNull()
        assertThat(responseErrorMessage).isNotEmpty()
        assertThat(responseErrorMessage).isEqualTo(errorMessage)
    }

    // Episode
    @Test
    fun getEpisodes_NoQuery_NoFilter_Success() = runTest {
        val testQuery = EpisodesQuery()
        val info = NetworkInfo(
            count = 826,
            pages = 40,
            next = 2,
            prev = null,
        )
        val episodes = mutableListOf<NetworkEpisode>()
        val s01e01 = NetworkEpisode(
            id = "1",
            episode = "S01E01",
            name = "the bla bla",
        )
        val s01e02 = NetworkEpisode(
            id = "2",
            episode = "S01E02",
            name = "the bla bla II",
        )
        val s01e03 = NetworkEpisode(
            id = "3",
            episode = "S01E03",
            name = "The rise of balablu",
        )
        episodes.addAll(listOf(s01e01, s01e02, s01e03))
        apolloClient.enqueueTestResponse(
            testQuery,
            FakeApiResponseData.generateApiResponseData(info, episodes),
        )

        val response = rickAndMortyApiService.getEpisodes()

        assertThat(response.info).isNotNull()
        assertThat(response.info?.count).isEqualTo(info.count)
        assertThat(response.info?.pages).isEqualTo(info.pages)
        assertThat(response.info?.next).isEqualTo(info.next)

        assertThat(response.results?.size).isEqualTo(episodes.size)
        assertThat(response.results).contains(s01e01)
        assertThat(response.results).contains(s01e02)
        assertThat(response.results).contains(s01e03)
    }

    @Test
    fun getEpisodes_NoQuery_NoFilter_Error_Response() = runTest {
        // Given
        val testQuery = EpisodesQuery()
        val errorMessage = "Expected type Int, found 99999999999999999999999999999999999999999999."

        // When
        apolloClient.enqueueTestResponse(
            operation = testQuery,
            errors = listOf(
                Error.Builder(errorMessage)
                    .locations(listOf(Error.Location(1, 34)))
                    .putExtension("code", "GRAPHQL_VALIDATION_FAILED")
                    .build(),
            ),
        )
        val response = rickAndMortyApiService.getEpisodes()

        // Then
        assertThat(response.errorResponse?.errors).isNotNull()
        val responseErrorMessage = response.errorResponse?.errors?.get(0)?.serverErrorMessage
        assertThat(responseErrorMessage).isNotNull()
        assertThat(responseErrorMessage).isNotEmpty()
        assertThat(responseErrorMessage).isEqualTo(errorMessage)
    }
}
