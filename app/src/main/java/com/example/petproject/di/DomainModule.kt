package com.example.petproject.di

import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.repository.note.TagRepository
import com.example.petproject.domain.usecases.note.DeleteNoteUseCase
import com.example.petproject.domain.usecases.note.DeleteNoteUseCaseImpl
import com.example.petproject.domain.usecases.note.GetNoteByIdUseCase
import com.example.petproject.domain.usecases.note.GetNoteByIdUseCaseImpl
import com.example.petproject.domain.usecases.note.GetNoteWithTagsByIdUseCase
import com.example.petproject.domain.usecases.note.GetNoteWithTagsByIdUseCaseImpl
import com.example.petproject.domain.usecases.note.ObserveNotesUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesUseCaseImpl
import com.example.petproject.domain.usecases.note.ObserveNotesWithTagsUseCase
import com.example.petproject.domain.usecases.note.ObserveNotesWithTagsUseCaseImpl
import com.example.petproject.domain.usecases.tag.ObserveTagsUseCase
import com.example.petproject.domain.usecases.tag.ObserveTagsUseCaseImpl
import com.example.petproject.domain.usecases.note.SaveNoteUseCase
import com.example.petproject.domain.usecases.note.SaveNoteUseCaseImpl
import com.example.petproject.domain.usecases.note.UpdateNoteUseCase
import com.example.petproject.domain.usecases.note.UpdateNoteUseCaseImpl
import com.example.petproject.domain.usecases.tag.SaveTagUseCase
import com.example.petproject.domain.usecases.tag.SaveTagUseCaseImpl
import com.example.petproject.domain.usecases.tag.UpdateTagUseCase
import com.example.petproject.domain.usecases.tag.UpdateTagUseCaseImpl
//import com.example.petproject.domain.repository.note.NoteWithTagsRepository
//import com.example.petproject.domain.usecases.SubscribeNotesWithTagsUseCase
//import com.example.petproject.domain.usecases.SubscribeNotesWithTagsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Singleton
    @Provides
    fun provideObserveNotesUseCase(
        repository: NoteRepository,
    ): ObserveNotesUseCase {
        return ObserveNotesUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun provideSaveNoteUseCase(
        repository: NoteRepository,
    ): SaveNoteUseCase {
        return SaveNoteUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesUpdateNoteUseCase(
        repository: NoteRepository
    ) : UpdateNoteUseCase {
        return UpdateNoteUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesGetNoteByIdUseCase(
        repository: NoteRepository
    ) : GetNoteByIdUseCase {
        return GetNoteByIdUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesUpdateTagUseCase(
        repository: TagRepository
    ) : UpdateTagUseCase {
        return UpdateTagUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesObserveTagsUseCase(
        repository: TagRepository
    ) : ObserveTagsUseCase {
        return ObserveTagsUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesSaveTagUseCase(
        repository: TagRepository
    ) : SaveTagUseCase {
        return SaveTagUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesObserveNotesWithTagsUseCase(
        repository: NoteRepository
    ) : ObserveNotesWithTagsUseCase {
        return ObserveNotesWithTagsUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesGetNoteWithTagsByIdUseCase(
        repository: NoteRepository
    ) : GetNoteWithTagsByIdUseCase {
        return GetNoteWithTagsByIdUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun providesDeleteNoteUseCase(
        repository: NoteRepository
    ) : DeleteNoteUseCase {
        return DeleteNoteUseCaseImpl(repository)
    }
}