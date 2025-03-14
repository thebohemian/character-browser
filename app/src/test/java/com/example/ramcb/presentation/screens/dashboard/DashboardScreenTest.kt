package com.example.ramcb.presentation.screens.dashboard

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.ramcb.data.repository.model.CharacterPreview
import com.example.ramcb.data.repository.model.CharacterStatus
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel = mockk<DashboardScreenViewModel> {
        every { onLoadNextPage() } just runs
    }

    @Test
    fun `click on dashboard should invoke onDashboardClick()`() {
        // given
        every { viewModel.onDashboardClick() } just runs
        every { viewModel.uiState } returns MutableStateFlow(
            DashboardScreenViewModel.UiState.ContentAvailable(
                listOf(
                    CharacterPreview("#0", "Foobaza", "http://zilch", CharacterStatus.ALIVE),
                )
            )
        )

        // when:
        composeTestRule.setContent {
            DashboardScreen(mockk(relaxed = true), viewModel)
        }

        composeTestRule.onRoot().performClick()

        // then:
        verify { viewModel.onDashboardClick() }
    }

}