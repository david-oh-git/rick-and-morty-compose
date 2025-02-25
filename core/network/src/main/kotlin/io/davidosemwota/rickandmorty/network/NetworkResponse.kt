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

import com.apollographql.apollo3.api.Error
import io.davidosemwota.rickandmorty.network.graphql.CharacterListQuery
import io.davidosemwota.rickandmorty.network.graphql.EpisodesQuery

data class NetworkCharacterListResponse(
    val info: NetworkInfo? = null,
    val results: List<NetworkCharacter>? = null,
    val errorResponse: NetworkErrorResponse? = null,
)

data class NetworkEpisodeListResponse(
    val info: NetworkInfo? = null,
    val results: List<NetworkEpisode>? = null,
    val errorResponse: NetworkErrorResponse? = null,
)

data class NetworkErrorResponse(
    val exception: Exception? = null,
    val errors: List<NetworkError>? = null,
)

data class NetworkError(
    val serverErrorMessage: String,
    val code: String,
)

data class NetworkInfo(
    val count: Int,
    val pages: Int,
    val next: Int?,
    val prev: Int?,
)

data class NetworkCharacter(
    val id: String?,
    val name: String? = null,
    val status: String? = null,
    val image: String? = null,
    val species: String? = null,
    val type: String? = null,
    val gender: String? = null,
    val origin: NetworkLocation? = null,
    val location: NetworkLocation? = null,
    val episode: List<NetworkEpisode> = emptyList(),
)

data class NetworkLocation(
    val id: String?,
    val name: String?,
    val dimension: String?,
    val residents: List<NetworkCharacter> = emptyList(),
)

data class NetworkEpisode(
    val id: String?,
    val airDate: String? = null,
    val episode: String?,
    val name: String?,
    val characters: List<NetworkCharacter> = emptyList(),
)

fun CharacterListQuery.Info.networkInfo(): NetworkInfo = NetworkInfo(
    count = this.count ?: 0,
    pages = this.pages ?: 0,
    next = this.next,
    prev = this.prev,
)

fun EpisodesQuery.Info.networkInfo(): NetworkInfo = NetworkInfo(
    count = this.count ?: 0,
    pages = this.pages ?: 0,
    next = this.next,
    prev = this.prev,
)

fun List<Error>.toListOfNetworkError(): List<NetworkError> {
    return this.map {
        NetworkError(
            serverErrorMessage = it.message,
            code = it.extensions?.get("code").toString(),
        )
    }
}

fun CharacterListQuery.Resident.toNetworkCharacter(): NetworkCharacter = NetworkCharacter(
    id = this.id,
    name = this.name,
    image = this.image,
)

fun CharacterListQuery.Location.toNetworkLocation(): NetworkLocation = NetworkLocation(
    id = this.id,
    name = this.name.orEmpty(),
    dimension = this.dimension.orEmpty(),
    residents = this.residents.filterNotNull().map { it.toNetworkCharacter() },
)

fun CharacterListQuery.Origin.toNetworkOrigin(): NetworkLocation = NetworkLocation(
    id = this.id,
    name = this.name,
    dimension = this.dimension,
)

fun CharacterListQuery.Episode.toNetworkEpisode(): NetworkEpisode = NetworkEpisode(
    id = this.id,
    name = this.name.orEmpty(),
    episode = this.episode.orEmpty(),
    characters = this.characters.filterNotNull().map {
        NetworkCharacter(
            id = it.id.toString(),
            image = it.image.toString(),
            gender = "null",
            origin = null,
            location = null,
            name = "",
            species = "",
            status = "",
            type = "",
        )
    },
    airDate = TODO(),
)

fun EpisodesQuery.Episodes?.toNetworkEpisodeListResponse(): NetworkEpisodeListResponse {
    val info = this?.info?.networkInfo()
    val episodes = this?.results?.filterNotNull()?.map {
        it.let {
            NetworkEpisode(
                id = it.id.orEmpty(),
                airDate = it.air_date.orEmpty(),
                name = it.name.orEmpty(),
                episode = it.episode.orEmpty(),
                characters = it.characters.map { queryCharacter ->
                    NetworkCharacter(
                        id = queryCharacter?.id.orEmpty(),
                        image = queryCharacter?.image.orEmpty(),
                        name = queryCharacter?.name.orEmpty(),
                    )
                },
            )
        }
    }

    return NetworkEpisodeListResponse(
        info = info,
        results = episodes,
        errorResponse = null,
    )
}

fun CharacterListQuery.Characters?.toNetworkCharacterListResponse(): NetworkCharacterListResponse {
    val info = this?.info?.networkInfo()
    val characters = this?.results?.filterNotNull()?.filter { it.id != null }?.map { characterResponse ->
        characterResponse.let {
            NetworkCharacter(
                id = it.id!!, // filter removes characters with null id already
                gender = it.gender.orEmpty(),
                image = it.image.orEmpty(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                status = it.status.orEmpty(),
                species = it.species.orEmpty(),
                location = it.location?.takeIf { it.id != null }?.toNetworkLocation(),
                origin = it.origin?.takeIf { it.id != null }?.toNetworkOrigin(),
                episode = it.episode.filterNotNull().filter { it.id != null }.map { queryEpisode ->
                    NetworkEpisode(
                        id = queryEpisode.id.orEmpty(),
                        episode = queryEpisode.episode.orEmpty(),
                        name = queryEpisode.name.orEmpty(),
                        characters = queryEpisode.characters.map { queryCharacter ->
                            NetworkCharacter(
                                id = queryCharacter?.id.orEmpty(),
                                image = queryCharacter?.image.orEmpty(),
                            )
                        },
                    )
                },
            )
        }
    }

    return NetworkCharacterListResponse(info, characters, null)
}
