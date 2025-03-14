package com.example.ramcb.presentation.screens.characterdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

object CharacterDetailsScreen {
    const val CHARACTER_ID_BUNDLE_KEY = "characterId"
}

@Composable
fun CharacterDetailScreen(
    onBackPressed: () -> Unit,
    viewModel: CharacterDetailsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is CharacterDetailsScreenViewModel.UiState.Initial -> Unit
        is CharacterDetailsScreenViewModel.UiState.LoadingInitially -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }


        is CharacterDetailsScreenViewModel.UiState.Error -> Box(
            contentAlignment = Alignment.Center
        ) {
            Text("Errör!")
        }

        is CharacterDetailsScreenViewModel.UiState.ContentAvailable -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ) {
            Row {
                val character = s.characterDetails
                Column {
                    AsyncImage(
                        model = character.imageUrl,
                        contentDescription = character.name
                    )
                }

                Column {
                    Text(character.name)
                    Text(character.status.name)
                    Text(character.species)
                    Text(character.type)
                    Text(character.gender.name)
                }
            }
        }
    }

    BackHandler { onBackPressed() }

}