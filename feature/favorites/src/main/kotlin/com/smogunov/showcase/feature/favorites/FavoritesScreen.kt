package com.smogunov.showcase.feature.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smogunov.showcase.core.designsystem.component.LoadingState
import com.smogunov.showcase.core.designsystem.component.MessageState
import com.smogunov.showcase.core.ui.CharacterListItem

@Composable
internal fun FavoritesScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesContent(uiState = uiState, onCharacterClick = onCharacterClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesContent(
    uiState: FavoritesUiState,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.feature_favorites_title)) }) },
    ) { padding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (uiState) {
            FavoritesUiState.Loading -> LoadingState(contentModifier)
            FavoritesUiState.Empty -> MessageState(
                title = stringResource(R.string.feature_favorites_empty),
                modifier = contentModifier,
            )
            is FavoritesUiState.Success -> LazyColumn(
                modifier = contentModifier.testTag("favorites:list"),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.favorites, key = { it.id }) { character ->
                    CharacterListItem(
                        character = character,
                        onClick = { onCharacterClick(it.id) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}
