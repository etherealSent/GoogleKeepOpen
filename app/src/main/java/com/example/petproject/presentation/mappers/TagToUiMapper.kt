package com.example.petproject.presentation.mappers

import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.presentation.model.TagUi
import javax.inject.Inject

class TagToUiMapper @Inject constructor() : (Tag) -> TagUi  {
    override fun invoke(tag: Tag): TagUi =
        TagUi(
            id = tag.id,
            name = tag.name
        )
}