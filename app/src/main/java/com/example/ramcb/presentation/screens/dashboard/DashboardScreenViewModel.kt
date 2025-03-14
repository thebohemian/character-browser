package com.example.ramcb.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ramcb.data.repository.character.CharacterRepository
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.data.repository.model.Paginated
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject internal constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    sealed interface UiState {
        data object Initial : UiState
        data object Error : UiState
        data class LoadingInitially(val progress: Int) : UiState
        data class ContentAvailable(
            val characterPreviews: List<CharacterPreview>,
            val isLoadingMore: Boolean = false,
        ) : UiState
    }

    private var lastResponse: LoadResult<Paginated<List<CharacterPreview>>> = LoadResult.None

    private data class Data(
        val page: Int = 1,
        val accumulatedData: Map<Int, List<CharacterPreview>> = emptyMap(),
        val lastResponse: LoadResult<Paginated<List<CharacterPreview>>> = LoadResult.None
    )

    private val page = MutableStateFlow(1)

    private val response = page.flatMapLatest { characterRepository.getCharacterPreviews(it) }

    private val data = response.scan(Data(), ::mapData)

    private fun mapData(
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
    }.also {
        // unhappy: side-effect
        lastResponse = response
    }

    private fun flattenData(data: Data) = data.accumulatedData.toSortedMap().values.flatten()

    private fun mapState(data: Data) = when (data.lastResponse) {
        is LoadResult.None -> UiState.LoadingInitially(0)
        is LoadResult.Loading -> {
            if (data.accumulatedData.isEmpty()) {
                UiState.LoadingInitially(50)
            } else {
                UiState.ContentAvailable(flattenData(data), isLoadingMore = true)
            }
        }

        is LoadResult.Error -> UiState.Error
        is LoadResult.Success -> UiState.ContentAvailable(flattenData(data))
    }

    val uiState: StateFlow<UiState> = data.mapLatest(::mapState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Initial
    )

    fun onDashboardClick() {
        // do not yet
    }

    fun onLoadNextPage() {
        when (val response = lastResponse) {
            is LoadResult.Loading -> Unit

            is LoadResult.None, is LoadResult.Error -> page.value = 1

            is LoadResult.Success -> {
                if (response.model.pageCount > response.model.page) {
                    page.value += 1
                }
            }
        }
    }

}

