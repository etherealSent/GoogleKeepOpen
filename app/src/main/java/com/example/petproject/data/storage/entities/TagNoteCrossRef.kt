package com.example.petproject.data.storage.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["noteId", "tagId"])
data class TagNoteCrossRef(
    val noteId: String,
    val tagId: String
)
