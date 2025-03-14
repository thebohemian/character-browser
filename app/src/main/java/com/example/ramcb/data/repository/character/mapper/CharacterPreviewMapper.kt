package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.graphql.GetCharactersQuery
import com.example.ramcb.data.repository.model.CharacterPreview

object CharacterPreviewMapper {
    operator fun invoke(result: GetCharactersQuery.Result): CharacterPreview =
        CharacterPreview(
            id = result.id!!,
            name = result.name!!,
            imageUrl = result.imageUrl!!,
            status = CharacterStatusMapper(result.status!!)
        )

}