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
package io.davidosemwota.rickandmorty.data.db.mappers

import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity
import io.davidosemwota.rickandmorty.models.Episode
import io.davidosemwota.rickandmorty.network.NetworkEpisode

internal fun EpisodeEntity.toEpisodeUiModel(): Episode = Episode(
    id = this.episodeId,
    name = this.name,
    airDate = this.airDate,
    episode = this.episode,
    pageIdentity = this.pageIdentity,
    characters = emptyList(),
)

internal fun NetworkEpisode.toEpisodeEntity(): EpisodeEntity = EpisodeEntity(
    episodeId = this.id.toInt(),
    airDate = this.airDate,
    episode = this.episode,
    name = this.name,
    pageIdentity = "",
)

internal fun List<NetworkEpisode>.toListOfEntity(): List<EpisodeEntity> =
    this.map { it.toEpisodeEntity() }
