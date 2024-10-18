package com.example.petproject.data.storage.mappers.note

import com.example.petproject.data.storage.relations.NoteWithTagsDb
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.tag.Tag
import javax.inject.Inject

class NotesWithTagsDbToDomainMapper @Inject constructor() : (List<NoteWithTagsDb>) -> List<NoteWithTags> {
    override fun invoke(notesWithTags: List<NoteWithTagsDb>): List<NoteWithTags> {
        return notesWithTags.map { noteWithTags ->
            NoteWithTags(
                title = noteWithTags.noteDb.title,
                content = noteWithTags.noteDb.content,
                pinned = noteWithTags.noteDb.pinned,
                tags = noteWithTags.tagDbs.map { tagDb ->
                    Tag(name = tagDb.name)
                }
            )
        }
    }
}