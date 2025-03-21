package com.example.ramcb.presentation.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.ramcb.data.repository.model.CharacterPreview

@Composable
fun LazyListState.OnBottomReached(
    loadMore : () -> Unit
){
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore){
        snapshotFlow { shouldLoadMore.value }
            .collect {
                // if should load more, then invoke loadMore
                if (it) loadMore()
            }
    }
}

@Composable
fun CharacterPreviewList(
    onGoToCharacterDetails: (String) -> Unit,
    loadMoreItems: () -> Unit,
    characterPreviews: List<CharacterPreview>,
    state: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = state,
        modifier = modifier.testTag("characterPreviewList").fillMaxSize()
    ) {
        if (characterPreviews.isNotEmpty()) {
            items(
                count = characterPreviews.size,
                key = { index: Int -> characterPreviews[index].id }) { index ->
                CharacterPreviewItem(onGoToCharacterDetails, characterPreviews[index])
            }

        }

    }

    state.OnBottomReached { loadMoreItems() }
}