package com.example.petproject.presentation.mappers

import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.presentation.model.TagUi
import javax.inject.Inject

class TagToDomainMapper @Inject constructor() : (TagUi) -> Tag {
    override fun invoke(tag: TagUi): Tag {
        return Tag(
            id = tag.id,
            name = tag.name
        )
    }
}