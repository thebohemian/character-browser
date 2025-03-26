package com.example.ramcb.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.data.repository.model.Paginated
import com.example.ramcb.presentation.screens.dashboard.usecase.LoadPaginatedCharacterPreviewListUseCase
import com.example.ramcb.presentation.screens.dashboard.usecase.LoadPaginatedCharacterPreviewListUseCase.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject internal constructor(
    loadPaginatedCharacterPreviewListUseCase: LoadPaginatedCharacterPreviewListUseCase
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

    private val page = MutableStateFlow(1)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState> =
        loadPaginatedCharacterPreviewListUseCase(page)
            .onEach {
                // allows onLoadNextPage() to decide immediately how to modify the page
                // not a nice approach in the sense, that this is a side-effect in flow
                // handling
                lastResponse = it.lastResponse
            }
            .mapLatest(::mapState).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Initial
            )

    private fun mapState(data: Data) = when (data.lastResponse) {
        is LoadResult.None -> UiState.LoadingInitially(0)
        is LoadResult.Loading -> {
            if (data.isEmpty()) {
                UiState.LoadingInitially(50)
            } else {
                UiState.ContentAvailable(data.flatten(), isLoadingMore = true)
            }
        }

        is LoadResult.Error -> UiState.Error
        is LoadResult.Success -> UiState.ContentAvailable(data.flatten())
    }

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

