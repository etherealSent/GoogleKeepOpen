package com.example.petproject.di

import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.usecases.ObserveNotesUseCase
import com.example.petproject.domain.usecases.ObserveNotesUseCaseImpl
import com.example.petproject.domain.usecases.SaveNoteUseCase
import com.example.petproject.domain.usecases.SaveNoteUseCaseImpl
//import com.example.petproject.domain.repository.note.NoteWithTagsRepository
//import com.example.petproject.domain.usecases.SubscribeNotesWithTagsUseCase
//import com.example.petproject.domain.usecases.SubscribeNotesWithTagsUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideObserveNotesUseCase(
        repository: NoteRepository,
    ): ObserveNotesUseCase  {
        return ObserveNotesUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun provideSaveNoteUseCase(
        repository: NoteRepository,
    ): SaveNoteUseCase  {
        return SaveNoteUseCaseImpl(repository)
    }

}