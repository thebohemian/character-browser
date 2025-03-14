package com.example.ramcb.data.repository.model

data class Paginated<D>(
    val page: Int,
    val pageCount: Int,
    val data: D
)

