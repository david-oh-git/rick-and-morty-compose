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
package io.davidosemwota.rickandmorty.characters

import com.google.common.truth.Truth.assertThat
import io.davidosemwota.rickandmorty.characters.list.CharactersViewModel
import io.davidosemwota.rickandmorty.domain.GetCharactersUseCase
import io.davidosemwota.rickandmorty.models.PagingState
import io.mockk.MockKAnnotations
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class CharactersViewModelTest {

    private lateinit var viewModel: CharactersViewModel
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        getCharactersUseCase = mockk(relaxed = true)
        viewModel = CharactersViewModel(getCharactersUseCase)
    }

    @Test
    fun verify_DefaultScreenState() = runTest {
        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isTrue()
        assertThat(result.fullScreenState).isEqualTo(PagingState.NotLoading)
        assertThat(result.appendState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    @Test
    fun verify_emptyDatabase_FullScreenLoading() {
        val expectedFullScreenState = PagingState.Loading

        viewModel.hasEmptyCharacterDatabase(true)
        viewModel.updateFullScreenState(expectedFullScreenState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isTrue()
        assertThat(result.fullScreenState).isEqualTo(expectedFullScreenState)
        assertThat(result.appendState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isTrue()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    @Test
    fun verify_EmptyDatabase_FullScreenError() {
        val expectedFullScreenState = PagingState.Error

        viewModel.hasEmptyCharacterDatabase(true)
        viewModel.updateFullScreenState(expectedFullScreenState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isTrue()
        assertThat(result.fullScreenState).isEqualTo(expectedFullScreenState)
        assertThat(result.appendState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isTrue()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    @Test
    fun verify_nonEmptyDatabase_FullScreenLoading() {
        val expectedFullScreenState = PagingState.Loading

        viewModel.hasEmptyCharacterDatabase(false)
        viewModel.updateFullScreenState(expectedFullScreenState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isFalse()
        assertThat(result.fullScreenState).isEqualTo(expectedFullScreenState)
        assertThat(result.appendState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    // Append

    @Test
    fun verify_emptyDatabase_AppendLoading() {
        val expectedAppendState = PagingState.Loading

        viewModel.hasEmptyCharacterDatabase(true)
        viewModel.updateAppendScreenState(expectedAppendState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isTrue()
        assertThat(result.appendState).isEqualTo(expectedAppendState)
        assertThat(result.fullScreenState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    @Test
    fun verify_emptyDatabase_AppendError() {
        val expectedAppendState = PagingState.Error

        viewModel.hasEmptyCharacterDatabase(true)
        viewModel.updateAppendScreenState(expectedAppendState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isTrue()
        assertThat(result.appendState).isEqualTo(expectedAppendState)
        assertThat(result.fullScreenState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }

    @Test
    fun verify_NonEmptyDatabase_AppendLoading() {
        val expectedAppendState = PagingState.Loading

        viewModel.hasEmptyCharacterDatabase(false)
        viewModel.updateAppendScreenState(expectedAppendState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isFalse()
        assertThat(result.appendState).isEqualTo(expectedAppendState)
        assertThat(result.fullScreenState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isFalse()
        assertThat(result.hasAppendScreenLoading).isTrue()
    }

    @Test
    fun verify_NonEmptyDatabase_AppendError() {
        val expectedAppendState = PagingState.Error

        viewModel.hasEmptyCharacterDatabase(false)
        viewModel.updateAppendScreenState(expectedAppendState)

        val result = viewModel.screenState.value

        assertThat(result.isCharacterDatabaseEmpty).isFalse()
        assertThat(result.appendState).isEqualTo(expectedAppendState)
        assertThat(result.fullScreenState).isEqualTo(PagingState.NotLoading)

        assertThat(result.hasFullScreenError).isFalse()
        assertThat(result.hasFullScreenLoading).isFalse()

        assertThat(result.hasAppendScreenError).isTrue()
        assertThat(result.hasAppendScreenLoading).isFalse()
    }
}
