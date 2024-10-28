package com.example.petproject.domain.usecases.tag

import com.example.petproject.domain.entities.tag.Tag
import com.example.petproject.domain.mappers.NoteToDbMapper
import com.example.petproject.domain.mappers.TagToDbMapper
import com.example.petproject.domain.repository.note.NoteRepository
import com.example.petproject.domain.repository.note.TagRepository
import javax.inject.Inject

interface SaveTagUseCase {
    suspend fun saveTag(tag: Tag)
}

class SaveTagUseCaseImpl @Inject constructor(
    private val tagRepository: TagRepository,
    private val tagToDbMapper: TagToDbMapper = TagToDbMapper()
) : SaveTagUseCase {
    override suspend fun saveTag(tag: Tag) {
        tagRepository.saveTag(tagToDbMapper(tag))
    }

}