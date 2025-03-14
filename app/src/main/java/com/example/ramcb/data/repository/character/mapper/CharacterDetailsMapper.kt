package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.graphql.GetCharacterDetailsQuery
import com.example.ramcb.data.repository.model.CharacterDetails

object CharacterDetailsMapper {
    operator fun invoke(data: GetCharacterDetailsQuery.Data): CharacterDetails =
        with(data.character!!) {
            CharacterDetails(
                id = id!!,
                name = name!!,
                status = CharacterStatusMapper(status!!),
                species = species!!,
                type = type!!,
                gender = GenderMapper(gender!!),
                imageUrl = imageUrl!!,
            )
        }

}