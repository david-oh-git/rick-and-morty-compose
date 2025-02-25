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
import io.davidosemwota.rickandmorty.data.db.entities.CHARACTER_ENTITY_PAGE_PREFIX
import io.davidosemwota.rickandmorty.data.db.entities.EPISODE_ENTITY_PAGE_PREFIX
import io.davidosemwota.rickandmorty.data.db.entities.PageInfoEntity

/**
 * DAO for [PageInfoEntity]
 */
@Dao
interface PageInfoDao {

    /**
     * Adds a list of items to the DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pageInfoList: List<PageInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pageInfo: PageInfoEntity)

    /**
     * Searches for an item from the DB, returns null
     *
     * @param id Unique id for the item
     */
    @Query("SELECT * FROM page_info WHERE id = :id")
    suspend fun getPageInfo(id: Int): PageInfoEntity?

    @Query("SELECT * FROM page_info WHERE identifier = :pageIdentity")
    suspend fun getPageInfo(pageIdentity: String): PageInfoEntity?

    /**
     * Delete table/all items from DB
     */
    @Query("DELETE FROM page_info")
    suspend fun clearAllPageInfo()

    @Query("DELETE FROM page_info WHERE identifier LIKE :prefix|| '%'")
    suspend fun clearAllCharacterPageInfo(prefix: String = CHARACTER_ENTITY_PAGE_PREFIX)

    @Query("DELETE FROM page_info WHERE identifier LIKE :prefix|| '%'")
    suspend fun clearAllEpisodePageInfo(prefix: String = EPISODE_ENTITY_PAGE_PREFIX)

    @Query("SELECT * FROM page_info ORDER by id")
    fun getAllItems(): List<PageInfoEntity>
}
