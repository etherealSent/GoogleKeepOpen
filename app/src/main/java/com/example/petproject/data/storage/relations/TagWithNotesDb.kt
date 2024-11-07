package com.example.petproject.data.storage.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.entities.TagNoteCrossRef

data class TagWithNotesDb(
    @Embedded
    val tagDb: TagDb = TagDb(),
    @Relation(
        parentColumn = "tagId",
        entityColumn = "noteId",
        associateBy = Junction(TagNoteCrossRef::class)
    )
    val noteWithTagDbs: List<NoteDb> = emptyList()
)
