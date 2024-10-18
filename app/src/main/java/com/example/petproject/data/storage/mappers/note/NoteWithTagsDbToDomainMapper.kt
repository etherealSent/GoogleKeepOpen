package com.example.petproject.data.storage.mappers.note

import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.relations.NoteWithTagsDb
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.tag.Tag
import javax.inject.Inject

class NoteWithTagsDbToDomainMapper @Inject constructor() : (NoteWithTagsDb) -> NoteWithTags {
    override fun invoke(noteWithTagsDb: NoteWithTagsDb): NoteWithTags {
        return NoteWithTags(
            title = noteWithTagsDb.noteDb.title,
            content = noteWithTagsDb.noteDb.content,
            pinned = noteWithTagsDb.noteDb.pinned,
            tags = noteWithTagsDb.tagDbs.map { tagDb ->
                Tag(tagDb.name)
            }
        )
    }
}