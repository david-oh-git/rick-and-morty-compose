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
package com.rickandmorty.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rickandmorty.data.db.entities.CharacterEntity

/**
 *  Data access object for [CharacterEntity]
 */
@Dao
interface CharacterDao {

    /**
     * Adds all items to local Db
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    /**
     *  Returns all items in DB via paging
     */
    @Query("SELECT * FROM character_entity_table ORDER by id")
    fun getPagedCharacters(): PagingSource<Int, CharacterEntity>

    /**
     *  Returns all DB items as a list.
     */
    @Query("SELECT * FROM character_entity_table ORDER by id")
    suspend fun getCharacters(): List<CharacterEntity>

    /**
     * Searches for an item via the id , returns null if item is not
     * found
     *
     * @param query id for item searched for
     */
    @Query("SELECT * FROM character_entity_table WHERE id = :query")
    suspend fun getCharacter(query: Int): CharacterEntity?

    /**
     * Deletes the entire table from the DB.
     */
    @Query("DELETE FROM character_entity_table")
    suspend fun deleteAllItems()
}
