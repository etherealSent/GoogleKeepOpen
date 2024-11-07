package com.example.petproject.data.storage.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.entities.TagNoteCrossRef

data class NoteWithTagsDb(
    @Embedded
    val noteDb: NoteDb = NoteDb(),
    @Relation(
        parentColumn = "noteId",
        entityColumn = "tagId",
        associateBy = Junction(TagNoteCrossRef::class)
    )
    val tagDbs: List<TagDb> = emptyList()
)