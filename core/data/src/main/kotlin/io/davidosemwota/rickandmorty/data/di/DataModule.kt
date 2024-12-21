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
package io.davidosemwota.rickandmorty.data.di

import android.content.Context
import androidx.paging.PagingConfig
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.davidosemwota.rickandmorty.data.db.RickAndMortyDatabase
import io.davidosemwota.rickandmorty.data.repository.CharacterRepository
import io.davidosemwota.rickandmorty.data.repository.CharacterRepositoryImpl
import io.davidosemwota.rickandmorty.data.repository.EpisodeRepository
import io.davidosemwota.rickandmorty.data.repository.EpisodeRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCharactersRepository(
        characterRepository: CharacterRepositoryImpl,
    ): CharacterRepository

    @Binds
    abstract fun bindEpisodesRepository(
        episodeRepository: EpisodeRepositoryImpl,
    ): EpisodeRepository

    companion object {

        @Singleton
        @Provides
        fun provideRoomDatabase(
            @ApplicationContext context: Context,
        ): RickAndMortyDatabase =
            Room.databaseBuilder(
                context,
                RickAndMortyDatabase::class.java,
                "rick-and-morty-database",
            ).build()

        @Provides
        fun providePagingConfig(): PagingConfig =
            PagingConfig(
                pageSize = 15,
                prefetchDistance = 1,
                enablePlaceholders = false,
            )
    }
}
