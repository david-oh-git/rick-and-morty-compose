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

import io.davidosemwota.rickandmorty.network.graphql.CharacterListQuery

data class CharacterListResponse(
    val info: Info? = null,
    val results: List<Character>? = null,
    val errorResponse: ErrorResponse? = null,
)

data class ErrorResponse(
    val exception: Exception? = null,
    val errors: List<Error>? = null,
)

data class Error(
    val serverErrorMessage: String,
    val code: String,
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: Int?,
    val prev: Int?,
)

data class Character(
    val id: String,
    val name: String = "",
    val status: String = "",
    val image: String,
    val species: String = "",
    val type: String = "",
    val gender: String = "",
    val origin: Origin? = null,
    val location: Location? = null,
    val episode: List<Episode> = emptyList(),
)

data class Origin(
    val id: String,
    val name: String,
    val dimension: String,
)

data class Resident(
    val id: String,
    val name: String,
    val image: String,
)

data class Location(
    val id: String,
    val name: String,
    val dimension: String,
    val residents: List<Resident>,
)

data class Episode(
    val id: String = "",
    val episode: String,
    val name: String,
    val characters: List<Character> = emptyList(),
)

fun CharacterListQuery.Info.toInfoResponse(): Info = Info(
    count = this.count ?: 0,
    pages = this.pages ?: 0,
    next = this.next,
    prev = this.prev,
)

fun List<com.apollographql.apollo3.api.Error>.toListOfResponseError(): List<Error> {
    return this.map {
        Error(
            serverErrorMessage = it.message,
            code = it.extensions?.get("code").toString(),
        )
    }
}

fun CharacterListQuery.Resident.toResidentResponse(): Resident = Resident(
    id = this.id.orEmpty(),
    name = this.name.orEmpty(),
    image = this.image.orEmpty(),
)

fun CharacterListQuery.Location.toLocationResponse(): Location = Location(
    id = this.id.orEmpty(),
    name = this.name.orEmpty(),
    dimension = this.dimension.orEmpty(),
    residents = this.residents.filterNotNull().map { it.toResidentResponse() },
)

fun CharacterListQuery.Origin.toOriginResponse(): Origin = Origin(
    id = this.id.orEmpty(),
    name = this.name.orEmpty(),
    dimension = this.dimension.orEmpty(),
)

fun CharacterListQuery.Episode.toEpisodeResponse(): Episode = Episode(
    id = this.id.orEmpty(),
    name = this.name.orEmpty(),
    episode = this.episode.orEmpty(),
    characters = this.characters.filterNotNull().map {
        Character(
            id = it.id.orEmpty(),
            image = it.image.orEmpty(),
            gender = "null",
            origin = null,
            location = null,
            name = "",
            species = "",
            status = "",
            type = "",
        )
    },
)

fun CharacterListQuery.Characters?.toCharactersResponse(): CharacterListResponse {
    val info = this?.info?.toInfoResponse()
    val characters = this?.results?.filterNotNull()?.map {
        it.let {
            Character(
                id = it.id.orEmpty(),
                gender = it.gender.orEmpty(),
                image = it.image.orEmpty(),
                name = it.name.orEmpty(),
                type = it.type.orEmpty(),
                status = it.status.orEmpty(),
                species = it.species.orEmpty(),
                location = it.location?.toLocationResponse(),
                origin = it.origin?.toOriginResponse(),
                episode = it.episode.map { queryEpisode ->
                    Episode(
                        id = queryEpisode?.id.orEmpty(),
                        episode = queryEpisode?.episode.orEmpty(),
                        name = queryEpisode?.name.orEmpty(),
                        characters = queryEpisode?.characters?.map { queryCharacter ->
                            Character(
                                id = queryCharacter?.id.orEmpty(),
                                image = queryCharacter?.image.orEmpty(),
                            )
                        } ?: emptyList(),
                    )
                },
            )
        }
    }

    return CharacterListResponse(info, characters, null)
}
