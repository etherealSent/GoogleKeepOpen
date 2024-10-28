package com.example.petproject.domain.usecases.tag

import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.mappers.TagToDbMapper
import com.example.petproject.domain.repository.note.TagRepository
import javax.inject.Inject

interface UpdateTagUseCase {
    suspend fun getTagById(id: String) : Tag?
    suspend fun updateTag(tag: Tag)
}

class UpdateTagUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository,
    private val tagToDbMapper: TagToDbMapper = TagToDbMapper()
) : UpdateTagUseCase {
    override suspend fun updateTag(tag: Tag) {
        val fetchedTag = getTagById(tag.id)?.copy(
            name = tag.name
        ) ?: throw Exception("Task (id ${tag.id}) not found")

        tagRepository.updateTag(
            tagToDbMapper(fetchedTag)
        )
    }

    override suspend fun getTagById(id: String): Tag? {
        return tagRepository.getTagById(id)
    }
}