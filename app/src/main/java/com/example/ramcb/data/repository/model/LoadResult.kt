package com.example.ramcb.data.repository.model

sealed class LoadResult<out M> {
    data object None : LoadResult<Nothing>()
    data object Loading : LoadResult<Nothing>()
    data class Error<M>(val exception: Exception) : LoadResult<M>()
    data class Success<M>(val model: M) : LoadResult<M>()
}