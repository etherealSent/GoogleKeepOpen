package com.example.petproject.di

import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.repository.note.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindNotesRepository(
        impl: NoteRepositoryImpl
    ) : NoteRepository

}