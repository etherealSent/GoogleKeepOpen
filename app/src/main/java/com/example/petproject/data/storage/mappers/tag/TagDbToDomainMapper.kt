package com.example.petproject.data.storage.mappers.tag

import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.data.storage.relations.NoteWithTagsDb
import com.example.petproject.domain.entities.note.NoteWithTags
import com.example.petproject.domain.entities.tag.Tag
import javax.inject.Inject

class TagDbToDomainMapper @Inject constructor() : (TagDb) -> Tag {
    override fun invoke(tagDb: TagDb): Tag {
        return Tag(tagDb.name)
    }
}