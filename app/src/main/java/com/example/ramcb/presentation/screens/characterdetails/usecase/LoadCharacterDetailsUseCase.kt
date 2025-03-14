package com.example.ramcb.presentation.screens.characterdetails.usecase

import androidx.lifecycle.SavedStateHandle
import com.example.ramcb.data.repository.character.CharacterRepository
import com.example.ramcb.presentation.screens.characterdetails.CharacterDetailsScreen
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class LoadCharacterDetailsUseCase @Inject internal constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(saveStateHandle: SavedStateHandle) =
        saveStateHandle.getStateFlow<String?>(CharacterDetailsScreen.CHARACTER_ID_BUNDLE_KEY, null)
            .filterNotNull()
            .flatMapLatest { repository.getCharacterDetails(it) }
}