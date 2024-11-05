package com.example.petproject.di

import android.content.Context
import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.usecases.note.ObserveNotesUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesUseCaseImpl
import com.example.petproject.utils.files.FileHelper
import com.example.petproject.utils.files.MediaContentHelper
import com.example.petproject.utils.files.ProviderFileManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FileModule {

    @Singleton
    @Provides
    fun provideFileManager(
        @ApplicationContext applicationContext: Context
    ): ProviderFileManager {
        return ProviderFileManager(
            context = applicationContext,
            fileHelper = FileHelper(
                applicationContext
            ),
            contentResolver = applicationContext.contentResolver,
            executor = Executors.newSingleThreadExecutor(),
            mediaContentHelper = MediaContentHelper()
        )
    }

}