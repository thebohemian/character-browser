package com.example.ramcb.presentation.screens.characterdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ramcb.data.repository.model.CharacterDetails
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.presentation.screens.characterdetails.usecase.LoadCharacterDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsScreenViewModel @Inject internal constructor(
    private val saveStateHandle: SavedStateHandle,
    private val loadCharacterDetailsUseCase: LoadCharacterDetailsUseCase
) : ViewModel() {

    sealed interface UiState {
        data object Initial : UiState
        data object Error : UiState
        data class LoadingInitially(val progress: Int) : UiState
        data class ContentAvailable(
            val characterDetails: CharacterDetails
        ) : UiState
    }

    private fun mapState(loadResult: LoadResult<CharacterDetails>) =
        when (loadResult) {
            is LoadResult.None -> UiState.LoadingInitially(0)
            is LoadResult.Loading -> UiState.LoadingInitially(50)
            is LoadResult.Error -> UiState.Error
            is LoadResult.Success -> UiState.ContentAvailable(loadResult.model)
        }

    val uiState: StateFlow<UiState> =
        loadCharacterDetailsUseCase(saveStateHandle).mapLatest(::mapState).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Initial
        )

}

