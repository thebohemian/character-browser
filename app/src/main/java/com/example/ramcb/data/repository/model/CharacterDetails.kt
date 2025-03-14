package com.example.ramcb.data.repository.model

data class CharacterDetails(
    val id: String,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val type: String,
    val gender: Gender,
    val imageUrl: String,
)



