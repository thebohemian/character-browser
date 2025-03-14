package com.example.ramcb.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.CharacterStatus
import com.example.ramcb.presentation.theme.CharacterBrowserTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DashboardScreenSnapshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
    )

    private fun snapshotState(state: DashboardScreenViewModel.UiState) {
        val viewModel = mockk<DashboardScreenViewModel>()

        every { viewModel.uiState } returns MutableStateFlow(state)

        paparazzi.snapshot {
            CharacterBrowserTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colorScheme.onPrimary
                    ) {

                        DashboardScreen({}, viewModel)
                    }
                }
            }
        }
    }

    @Test
    fun `with state Ready should display items`() {
        val paginatedData = listOf(
            CharacterPreview("#0", "Foobaza", "http://zilch", CharacterStatus.ALIVE),
            CharacterPreview("#1", "Zawasa", "http://zilch", CharacterStatus.DEAD),
            CharacterPreview("#2", "Ciaozinho", "http://zilch", CharacterStatus.UNKNOWN),
        )
        snapshotState(DashboardScreenViewModel.UiState.ContentAvailable(paginatedData))
    }

    @Test
    fun `with state Loading should display loading text with progress`() {
        snapshotState(DashboardScreenViewModel.UiState.LoadingInitially(55))
    }

    @Test
    fun `with state Error should display error text`() {
        snapshotState(DashboardScreenViewModel.UiState.Error)
    }
}