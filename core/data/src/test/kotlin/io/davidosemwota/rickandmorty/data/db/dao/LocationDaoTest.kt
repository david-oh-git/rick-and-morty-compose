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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.db.entities.LocationEntity
import io.davidosemwota.rickandmorty.data.db.entities.getLocationIdentifier
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class LocationDaoTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var locationDao: LocationDao

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()

        locationDao = rickAndMortyDatabase.locationDao()
    }

    @After
    fun close() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun saveItems_verifySavedToDb() = runTest {
        // Given
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // When
        val result = locationDao.getLocations()

        // Then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun saveItem_verifySaved() = runTest {
        // Given
        val expectedId = 2
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = expectedId,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // When
        val result = locationDao.getLocation(expectedId)

        // Then
        assertThat(result).isNotNull()
        assertThat(result?.locationId).isEqualTo(expectedId)
        assertThat(result?.name).isEqualTo(citadelOfRicks.name)
        assertThat(result?.dimension).isEqualTo(citadelOfRicks.dimension)
    }

    @Test
    fun saveItems_verifyDeleted() = runTest {
        // Given
        val expectedIdentifier = getLocationIdentifier(1)
        val citadelOfRicks = LocationEntity(
            locationId = 25,
            name = "Citadel of Ricks",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        val earthReplacement = LocationEntity(
            locationId = 26,
            name = "Earth (Replacement dimension)",
            dimension = "Replacement dimension",
            pageIdentity = expectedIdentifier,
        )
        val abadango = LocationEntity(
            locationId = 27,
            name = "Abadango",
            dimension = "Unknown",
            pageIdentity = expectedIdentifier,
        )
        locationDao.insert(citadelOfRicks, earthReplacement, abadango)

        // When
        locationDao.deleteAllItems()

        // Then
        val result = locationDao.getLocations()

        assertThat(result).isEmpty()
    }
}
