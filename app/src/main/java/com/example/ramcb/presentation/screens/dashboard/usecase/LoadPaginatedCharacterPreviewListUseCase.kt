package com.example.ramcb.presentation.screens.dashboard.usecase

import com.example.ramcb.data.repository.character.CharacterRepository
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.data.repository.model.Paginated
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

class LoadPaginatedCharacterPreviewListUseCase @Inject internal constructor(
    private val repository: CharacterRepository
) {
    data class Data(
        val page: Int = 1,
        internal val accumulatedData: Map<Int, List<CharacterPreview>> = emptyMap(),
        val lastResponse: LoadResult<Paginated<List<CharacterPreview>>> = LoadResult.None
    ) {
        fun isEmpty() = accumulatedData.isEmpty()
        fun flatten() = accumulatedData.toSortedMap().values.flatten()
    }

    private fun mapLoadResult(
        currentData: Data,
        response: LoadResult<Paginated<List<CharacterPreview>>>,
    ) = when (response) {
        is LoadResult.None -> currentData
        is LoadResult.Loading -> currentData.copy(
            lastResponse = response
        )

        is LoadResult.Error -> Data()
        is LoadResult.Success -> {
            val page = response.model.page
            currentData.copy(
                accumulatedData = currentData.accumulatedData + (page to response.model.data),
                lastResponse = response
            )
        }
    }

    operator fun invoke(page: Flow<Int>): Flow<Data> =
        page.flatMapLatest { repository.getCharacterPreviews(it) }.scan(Data(), ::mapLoadResult)
}