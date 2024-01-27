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
package com.rickandmorty.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rickandmorty.data.model.PageInfo
import com.rickandmorty.network.NetworkInfo

@Entity(tableName = "page_info")
data class PageInfoEntity(
    @PrimaryKey
    val id: Int,
    val pages: Int,
    val count: Int,
    val next: Int?,
    val prev: Int?,
)

fun PageInfoEntity.toModel(): PageInfo = PageInfo(
    id = this.id.toInt(),
    pages = this.pages,
    next = this.next,
    prev = this.prev,
)

internal fun NetworkInfo.toEntity(id: String): PageInfoEntity = PageInfoEntity(
    id = id.toInt(),
    pages = this.pages,
    next = this.next,
    prev = this.prev,
    count = this.count,
)
