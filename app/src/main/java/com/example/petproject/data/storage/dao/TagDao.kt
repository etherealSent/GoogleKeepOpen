package com.example.petproject.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.relations.TagWithNotesDb
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tagDb: TagDb)

    @Update
    fun updateTag(tagDb: TagDb)

    @Delete
    fun deleteTag(tagDb: TagDb)

    @Query("SELECT * FROM tag")
    fun loadAllTags(): List<TagDb>

    @Query("SELECT * FROM tag")
    fun observeTags(): Flow<List<TagDb>>

    @Query("SELECT * FROM tag WHERE tagId=:id")
    fun getTagById(id: String): TagDb?
}