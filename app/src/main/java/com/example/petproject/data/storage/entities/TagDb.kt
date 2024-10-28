package com.example.petproject.data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.petproject.data.storage.entities.TagDb.Companion.TAG_TABLE_NAME

@Entity(tableName = TAG_TABLE_NAME)
data class TagDb(
    @PrimaryKey val tagId: String = "",
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        const val TAG_TABLE_NAME = "tag"
    }
}
