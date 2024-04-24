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
package io.davidosemwota.rickandmorty.data.model.entities

import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.data.db.entities.PageInfoEntity
import io.davidosemwota.rickandmorty.data.db.entities.toModel
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PageInfoEntityTest {

    @Test
    fun pageInfoEntityToModelExtention_verifyResult() = runTest {
        // Given/Arrange
        val id = 3
        val pages: Int = 30
        val next: Int = 4
        val prev: Int = 3
        val pageInfoEntity = PageInfoEntity(
            id = id,
            pages = pages,
            next = next,
            prev = prev,
            count = 20,
        )

        // When/Act
        val result = pageInfoEntity.toModel()

        // Then/Assert
        assertThat(result).isNotNull()
        assertThat(result.id).isEqualTo(pageInfoEntity.id.toInt())
        assertThat(result.pages).isEqualTo(pageInfoEntity.pages)
        assertThat(result.next).isEqualTo(pageInfoEntity.next)
        assertThat(result.prev).isEqualTo(pageInfoEntity.prev)
    }
}
