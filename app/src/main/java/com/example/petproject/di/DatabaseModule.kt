package com.example.petproject.di

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.example.petproject.data.storage.AppDatabase
import com.example.petproject.data.storage.AppDatabase.Companion.DATABASE_NAME
import com.example.petproject.data.storage.converters.DataConverters
import com.example.petproject.data.storage.dao.NoteDao
import com.example.petproject.data.storage.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provide(@ApplicationContext context: Context) =
        databaseBuilder(
            context,
            AppDatabase::class.java, DATABASE_NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .addTypeConverter(DataConverters())
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(db: AppDatabase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideTagDao(db: AppDatabase): TagDao = db.tagDao()
}