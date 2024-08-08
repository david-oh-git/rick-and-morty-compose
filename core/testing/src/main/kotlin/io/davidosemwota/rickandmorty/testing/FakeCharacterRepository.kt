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
package io.davidosemwota.rickandmorty.testing

import androidx.paging.PagingData
import io.davidosemwota.rickandmorty.data.repository.CharacterRepository
import io.davidosemwota.rickandmorty.models.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCharacterRepository(
    private val items: List<Character> = generateCharacters(),
) : CharacterRepository {

    override fun getPagedCharacters(): Flow<PagingData<Character>> {
        return flowOf(PagingData.from(items))
    }

    companion object {
        fun generateCharacters(): List<Character> =
            listOf(
                Character(
                    id = 1,
                    name = "Rick Smith",
                    gender = "Male",
                    imageUrl = "https:samada.com/kenny_chan",
                ),
                Character(
                    id = 2,
                    name = "Morty Smith",
                    gender = "Male",
                    imageUrl = "https:samada.com/kenny_chan",
                ),
                Character(
                    id = 3,
                    name = "Rick Smith",
                    gender = "Male",
                    imageUrl = "https:samada.com/kenny_chan",
                ),
                Character(
                    id = 4,
                    name = "Rick Smith",
                    gender = "Male",
                    imageUrl = "https:samada.com/kenny_chan",
                ),
                Character(
                    id = 5,
                    name = "Rick Smith",
                    gender = "Male",
                    imageUrl = "https:samada.com/kenny_chan",
                ),
            )
    }
}
