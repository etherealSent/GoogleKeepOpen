package com.example.petproject.data.storage.mappers.tag

import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.relations.TagWithNotesDb
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.note.NotesWithTags
import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.entities.tag.TagWithNotes
import javax.inject.Inject

//class TagWithNotesToDomainMapper @Inject constructor() : (TagWithNotesDb) -> TagWithNotes {
//    override fun invoke(tagWithNotesDb: TagWithNotesDb): TagWithNotes {
//        return TagWithNotes(
//            tag = Tag(name = tagWithNotesDb.tagDb.name),
//            notes = NotesWithTags(tagWithNotesDb.noteWithTagDbs.map { noteWithTagsDb ->
//                NoteWithTags(
//                    title = noteWithTagsDb.noteDb.title,
//                    content = noteWithTagsDb.noteDb.content,
//                    pinned = noteWithTagsDb.noteDb.pinned,
//                    tags = noteWithTagsDb.tagDbs.map { tagDb ->
//                        Tag(name = tagDb.name)
//                    }
//                )
//            })
//        )
//    }
//}