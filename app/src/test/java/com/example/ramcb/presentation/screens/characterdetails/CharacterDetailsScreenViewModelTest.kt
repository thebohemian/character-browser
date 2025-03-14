package com.example.ramcb.presentation.screens.characterdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.turbineScope
import com.example.ramcb.data.repository.model.CharacterDetails
import com.example.ramcb.data.repository.model.LoadResult
import com.example.ramcb.presentation.screens.characterdetails.usecase.LoadCharacterDetailsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharacterDetailsScreenViewModelTest {

    private val savedStateHandle = mockk<SavedStateHandle>()

    private val loadCharacterDetailsUseCase = mockk<LoadCharacterDetailsUseCase> {
        every { this@mockk.invoke(any()) } returns emptyFlow()
    }

    private val subject by lazy {
        // subject is supposed to be created on first access in test
        CharacterDetailsScreenViewModel(
            savedStateHandle = savedStateHandle,
            loadCharacterDetailsUseCase = loadCharacterDetailsUseCase,
        )
    }

    @Test
    fun `uiState should initially be Initial`() = runTest {
        turbineScope {
            // when:
            val result = subject.uiState.testIn(backgroundScope)

            // then:
            result.awaitItem() shouldBe CharacterDetailsScreenViewModel.UiState.Initial
        }
    }

    @Test
    fun `uiState should be LoadingInitially with 0 progress when LoadResult is None`() = runTest {
        turbineScope {
            // given:
            every { loadCharacterDetailsUseCase.invoke(any()) } returns flowOf(LoadResult.None)

            // when:
            val result = subject.uiState.testIn(backgroundScope)

            // then:
            result.skipItems(1)
            result.awaitItem() shouldBe CharacterDetailsScreenViewModel.UiState.LoadingInitially(0)
        }
    }

    @Test
    fun `uiState should be LoadingInitially with 50 progress when LoadResult is Loading`() =
        runTest {
            turbineScope {
                // given:
                every { loadCharacterDetailsUseCase.invoke(any()) } returns flowOf(LoadResult.Loading)

                // when:
                val result = subject.uiState.testIn(backgroundScope)

                // then:
                result.skipItems(1)
                result.awaitItem() shouldBe CharacterDetailsScreenViewModel.UiState.LoadingInitially(
                    50
                )
            }
        }

    @Test
    fun `uiState should be Error when LoadResult is Error`() = runTest {
        turbineScope {
            // given:
            every { loadCharacterDetailsUseCase.invoke(any()) } returns flowOf(
                LoadResult.Error(
                    mockk()
                )
            )

            // when:
            val result = subject.uiState.testIn(backgroundScope)

            // then:
            result.skipItems(1)
            result.awaitItem() shouldBe CharacterDetailsScreenViewModel.UiState.Error
        }
    }

    @Test
    fun `uiState should be ContentAvailable when LoadResult is Success`() = runTest {
        turbineScope {
            // given:
            val givenModel = mockk<CharacterDetails>()
            every { loadCharacterDetailsUseCase.invoke(any()) } returns flowOf(
                LoadResult.Success(givenModel)
            )

            // when:
            val result = subject.uiState.testIn(backgroundScope)

            // then:
            result.skipItems(1)
            result.awaitItem() shouldBe CharacterDetailsScreenViewModel.UiState.ContentAvailable(
                givenModel
            )
        }
    }


}