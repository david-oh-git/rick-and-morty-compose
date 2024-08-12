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
import io.davidosemwota.rickandmorty.data.db.entities.PageInfoEntity
import io.davidosemwota.rickandmorty.data.db.entities.getCharacterIdentifier
import io.davidosemwota.rickandmorty.data.db.entities.getEpisodeIdentifier
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PageInfoDaoTest {

    private lateinit var rickAndMortyDatabase: RickAndMortyDatabase
    private lateinit var pageInfoDao: PageInfoDao

    @Before
    fun setUp() {
        val applicationContext = ApplicationProvider.getApplicationContext<Context>()

        rickAndMortyDatabase = Room.inMemoryDatabaseBuilder(
            applicationContext,
            RickAndMortyDatabase::class.java,
        ).allowMainThreadQueries().build()

        pageInfoDao = rickAndMortyDatabase.pageInfoDao()
    }

    @After
    fun close() {
        rickAndMortyDatabase.close()
    }

    @Test
    fun getIndividualItems_VerifyResultViaIdentifier() = runTest {
        // Given
        val characterInfoPage = 4
        val episodeInfoPage = 4
        val expectedCharacterIdentifier = getCharacterIdentifier(characterInfoPage)
        val expectedEpisodeIdentifier = getEpisodeIdentifier(episodeInfoPage)
        val expectedCharacterPageInfo = PageInfoEntity(
            id = 0,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = expectedCharacterIdentifier,
        )
        val expectedEpisodePageInfo = PageInfoEntity(
            id = 0,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = expectedEpisodeIdentifier,
        )
        val items: List<PageInfoEntity> = listOf(
            expectedCharacterPageInfo,
            expectedEpisodePageInfo,
        )

        pageInfoDao.insertAll(items)

        // When
        val result = pageInfoDao.getAllItems()
        val characterPageInfo = pageInfoDao.getPageInfo(expectedCharacterIdentifier)
        val episodePageInfo = pageInfoDao.getPageInfo(expectedEpisodeIdentifier)

        // Then

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(items.size)

        assertThat(characterPageInfo).isNotNull()
        assertThat(expectedCharacterPageInfo.next).isEqualTo(characterPageInfo?.next)
        assertThat(expectedCharacterPageInfo.pages).isEqualTo(characterPageInfo?.pages)
        assertThat(expectedCharacterPageInfo.prev).isEqualTo(characterPageInfo?.prev)
        assertThat(expectedCharacterPageInfo.count).isEqualTo(characterPageInfo?.count)
        assertThat(expectedCharacterPageInfo.identifier).isEqualTo(characterPageInfo?.identifier)

        assertThat(episodePageInfo).isNotNull()
        assertThat(expectedEpisodePageInfo.next).isEqualTo(episodePageInfo?.next)
        assertThat(expectedEpisodePageInfo.pages).isEqualTo(episodePageInfo?.pages)
        assertThat(expectedEpisodePageInfo.prev).isEqualTo(episodePageInfo?.prev)
        assertThat(expectedEpisodePageInfo.count).isEqualTo(episodePageInfo?.count)
        assertThat(expectedEpisodePageInfo.identifier).isEqualTo(episodePageInfo?.identifier)
    }

    @Test
    fun getIndividualItems_VerifyResult() = runTest {
        val item1 = PageInfoEntity(
            id = 2,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(2),
        )
        val item2 = PageInfoEntity(
            id = 3,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getEpisodeIdentifier(1),
        )
        val items: List<PageInfoEntity> = listOf(
            item1,
            item2,
        )

        pageInfoDao.insertAll(items)

        val result = pageInfoDao.getAllItems()
        val itemOneResult = pageInfoDao.getPageInfo(item1.id)

        assertThat(itemOneResult).isNotNull()
        assertThat(item1.id).isEqualTo(itemOneResult?.id)
        assertThat(item1.next).isEqualTo(itemOneResult?.next)
        assertThat(item1.pages).isEqualTo(itemOneResult?.pages)

        assertThat(result).contains(item2)
    }

    @Test
    fun insertItem_VerifyResult() = runTest {
        val item = PageInfoEntity(
            id = 2,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(1),
        )

        pageInfoDao.insert(item)

        val result = pageInfoDao.getAllItems()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).contains(item)
    }

    @Test
    fun insertAllItems_VerifyResult() = runTest {
        val item1 = PageInfoEntity(
            id = 2,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(1),
        )
        val item2 = PageInfoEntity(
            id = 3,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(1),
        )
        val items: List<PageInfoEntity> = listOf(
            item1,
            item2,
        )

        pageInfoDao.insertAll(items)

        val result = pageInfoDao.getAllItems()

        assertThat(result).isNotNull()
        assertThat(result).isNotEmpty()
        assertThat(result).contains(item1)
        assertThat(result).contains(item2)
    }

    @Test
    fun deleteAll_VerifyResult() = runTest {
        val item1 = PageInfoEntity(
            id = 2,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(1),
        )
        val item2 = PageInfoEntity(
            id = 3,
            prev = null,
            next = 2,
            pages = 50,
            count = 20,
            identifier = getCharacterIdentifier(1),
        )
        val items: List<PageInfoEntity> = listOf(
            item1,
            item2,
        )
        pageInfoDao.insertAll(items)

        pageInfoDao.clearAllPageInfo()

        val result = pageInfoDao.getAllItems()

        assertThat(result).isEmpty()
        assertThat(result).doesNotContain(item1)
        assertThat(result).doesNotContain(item2)
    }
}
