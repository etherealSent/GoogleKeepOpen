package com.example.petproject.domain.mappers

import com.example.petproject.data.storage.entities.NoteDb
import com.example.petproject.data.storage.entities.TagDb
import com.example.petproject.domain.entities.tag.Tag
import javax.inject.Inject

class TagToDbMapper @Inject constructor() : (Tag) -> TagDb  {
    override fun invoke(tag: Tag): TagDb {
        return TagDb(
            name = tag.name
        )
    }
}