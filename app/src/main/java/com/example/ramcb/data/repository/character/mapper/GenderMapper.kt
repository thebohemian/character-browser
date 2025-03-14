package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.model.Gender

object GenderMapper {
    operator fun invoke(genderString: String) =
        when (genderString.lowercase()) {
            "male" -> Gender.MALE
            "female" -> Gender.FEMALE
            "genderless" -> Gender.GENDERLESS
            else -> Gender.UNKNOWN
        }
}