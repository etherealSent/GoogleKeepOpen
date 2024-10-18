package com.example.petproject.data.storage.entities

import androidx.room.Entity

@Entity(primaryKeys = ["noteId", "tagId"])
data class TagNoteCrossRef(
    val noteId: Int,
    val tagId: Int
)