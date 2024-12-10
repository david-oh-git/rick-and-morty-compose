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

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.davidosemwota.rickandmorty.data.db.entities.EpisodeEntity

@Dao
interface EpisodeDao {

    /**
     * Adds all items to local Db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodes: List<EpisodeEntity>)

    /**
     * Add item to local Db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg episode: EpisodeEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(vararg episode: EpisodeEntity)

    /**
     *  Returns all items in DB via paging
     */
    @Query("SELECT * FROM episode_entity_table ORDER by episodeId")
    fun getPagedEpisodes(): PagingSource<Int, EpisodeEntity>

    /**
     *  Returns all DB items as a list.
     */
    @Query("SELECT * FROM episode_entity_table ORDER by episodeId")
    suspend fun getEpisodes(): List<EpisodeEntity>

    /**
     * Searches for an item via the id , returns null if item is not
     * found
     *
     * @param query id for item searched for
     */
    @Query("SELECT * FROM episode_entity_table WHERE episodeId = :query")
    suspend fun getEpisode(query: Int): EpisodeEntity?

    /**
     * Deletes the entire table from the DB.
     */
    @Query("DELETE FROM episode_entity_table")
    suspend fun deleteAllItems()
}
