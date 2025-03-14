package com.example.ramcb.presentation.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import com.example.ramcb.presentation.theme.CharacterBrowserTheme
import com.example.ramcb.presentation.views.CharacterPreviewList

@Composable
fun DashboardScreen(
    onGoToCharacterDetails: (String) -> Unit,
    viewModel: DashboardScreenViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.clickable(onClick = viewModel::onDashboardClick),
        contentAlignment = Alignment.Center
    ) {
        when (val s = uiState) {
            DashboardScreenViewModel.UiState.Initial -> {
                // do not render
            }

            is DashboardScreenViewModel.UiState.ContentAvailable -> {
                CharacterPreviewList(
                    onGoToCharacterDetails,
                    viewModel::onLoadNextPage,
                    s.characterPreviews,
                    listState,
                )

                if (s.isLoadingMore) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is DashboardScreenViewModel.UiState.LoadingInitially -> Box(
                modifier = Modifier.fillMaxSize()
            ) { Loading(s) }

            is DashboardScreenViewModel.UiState.Error -> Box(
                modifier = Modifier.fillMaxSize()
            ) { Text("Error") }
        }

    }
}

@Composable
fun Loading(loading: DashboardScreenViewModel.UiState.LoadingInitially) {
    Text("Loading ${loading.progress}")
}

@Preview
@Composable
fun LoadingPreview() {
    CharacterBrowserTheme {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onPrimary
        ) {
            Loading(loading = DashboardScreenViewModel.UiState.LoadingInitially(85))
        }
    }
}