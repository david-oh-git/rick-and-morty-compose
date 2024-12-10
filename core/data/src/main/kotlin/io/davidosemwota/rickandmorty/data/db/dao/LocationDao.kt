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
import io.davidosemwota.rickandmorty.data.db.entities.LOCATION_ENTITY_TABLE_NAME
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg location: LocationEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(vararg location: LocationEntity)

    @Query("SELECT * FROM $LOCATION_ENTITY_TABLE_NAME ORDER by locationId")
    fun getPagedLocations(): PagingSource<Int, LocationEntity>

    @Query("SELECT * FROM $LOCATION_ENTITY_TABLE_NAME ORDER by locationId")
    suspend fun getLocations(): List<LocationEntity>

    @Query("SELECT * FROM $LOCATION_ENTITY_TABLE_NAME WHERE locationId = :query")
    suspend fun getLocation(query: Int): LocationEntity?

    @Query("DELETE FROM $LOCATION_ENTITY_TABLE_NAME")
    suspend fun deleteAllItems()
}
