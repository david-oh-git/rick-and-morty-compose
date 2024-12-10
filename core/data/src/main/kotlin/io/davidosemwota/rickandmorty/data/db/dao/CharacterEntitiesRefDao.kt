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
package io.davidosemwota.rickandmorty.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.davidosemwota.rickandmorty.data.db.CharacterWithEpisodesAndLocations
import io.davidosemwota.rickandmorty.data.db.EpisodeWithCharacters
import io.davidosemwota.rickandmorty.data.db.entities.CHARACTER_ENTITY_TABLE_NAME
import io.davidosemwota.rickandmorty.data.db.entities.CHARACTER_EPISODE_REF_TABLE
import io.davidosemwota.rickandmorty.data.db.entities.CHARACTER_LOCATION_REF_TABLE
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndEpisodeEntityRef
import io.davidosemwota.rickandmorty.data.db.entities.CharacterAndLocationEntityRef
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterEntitiesRefDao {

    /**
     * Adds all character location ref item/items to local Db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg characterWithLocation: CharacterAndLocationEntityRef)

    /**
     * Adds all character episode ref item/items to local Db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg characterWithEpisode: CharacterAndEpisodeEntityRef)

    @Transaction
    @Query("SELECT * FROM $CHARACTER_ENTITY_TABLE_NAME ORDER by characterId")
    fun getCharacterWithEpisodesAndLocations(): Flow<List<CharacterWithEpisodesAndLocations>>

    @Transaction
    @Query("SELECT * FROM $CHARACTER_ENTITY_TABLE_NAME WHERE characterId = :query")
    fun getCharacterWithEpisodesAndLocations(query: Int): CharacterWithEpisodesAndLocations?

    @Query("DELETE FROM $CHARACTER_EPISODE_REF_TABLE")
    suspend fun deleteCharacterEpisodeRefDb()

    @Query("DELETE FROM $CHARACTER_LOCATION_REF_TABLE")
    suspend fun deleteCharacterLocationRefDb()

    // TODO move to separate dao.

    @Transaction
    @Query("SELECT * FROM episode_entity_table ORDER by episodeId")
    fun getEpisodeWithCharacters(): List<EpisodeWithCharacters>

    @Transaction
    @Query("SELECT * FROM episode_entity_table WHERE episodeId = :query")
    fun getEpisodeWithCharacters(query: Int): List<EpisodeWithCharacters>
}
