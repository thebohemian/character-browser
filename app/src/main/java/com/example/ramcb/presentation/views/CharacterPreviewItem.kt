package com.example.ramcb.presentation.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.example.ramcb.data.repository.model.CharacterPreview

@Composable
fun CharacterPreviewItem(
    onGoToCharacterDetails: (String) -> Unit,
    characterPreview: CharacterPreview,
    modifier: Modifier = Modifier
) = Row(modifier = Modifier.clickable(onClick = {
    onGoToCharacterDetails(characterPreview.id)
})) {
    AsyncImage(
        model = characterPreview.imageUrl,
        contentDescription = characterPreview.name
    )
    Text(characterPreview.name)
}