package com.example.ramcb.data.repository.character.mapper

import com.example.ramcb.data.repository.model.CharacterStatus

object CharacterStatusMapper {
    operator fun invoke(statusString: String) =
        when (statusString.lowercase()) {
            "dead" -> CharacterStatus.DEAD
            "alive" -> CharacterStatus.ALIVE
            else -> CharacterStatus.UNKNOWN
        }
}
