package com.smogunov.showcase.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.smogunov.showcase.core.designsystem.component.LoadingState
import com.smogunov.showcase.core.designsystem.component.MessageState
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.ui.StatusLine

@Composable
internal fun CharacterDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CharacterDetailsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRetry = viewModel::onRetry,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailsContent(
    uiState: CharacterDetailsUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is CharacterDetailsUiState.Success) {
                        Text(uiState.details.character.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.feature_details_back),
                        )
                    }
                },
                actions = {
                    if (uiState is CharacterDetailsUiState.Success) {
                        FavoriteButton(isFavorite = uiState.details.isFavorite, onClick = onFavoriteClick)
                    }
                },
            )
        },
    ) { padding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(padding)
        when (uiState) {
            CharacterDetailsUiState.Loading -> LoadingState(contentModifier)
            CharacterDetailsUiState.Error -> MessageState(
                title = stringResource(R.string.feature_details_error),
                actionLabel = stringResource(R.string.feature_details_retry),
                onAction = onRetry,
                modifier = contentModifier,
            )
            is CharacterDetailsUiState.Success -> CharacterDetailsBody(
                character = uiState.details.character,
                modifier = contentModifier,
            )
        }
    }
}

@Composable
private fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        if (isFavorite) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = stringResource(R.string.feature_details_remove_favorite),
                tint = MaterialTheme.colorScheme.primary,
            )
        } else {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = stringResource(R.string.feature_details_add_favorite),
            )
        }
    }
}

@Composable
private fun CharacterDetailsBody(character: Character, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp)),
        )
        Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
        StatusLine(character)
        HorizontalDivider()
        InfoRow(label = stringResource(R.string.feature_details_gender), value = character.gender)
        InfoRow(label = stringResource(R.string.feature_details_origin), value = character.origin)
        InfoRow(label = stringResource(R.string.feature_details_location), value = character.location)
        InfoRow(label = stringResource(R.string.feature_details_episodes), value = character.episodeCount.toString())
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    ListItem(
        overlineContent = { Text(label) },
        headlineContent = { Text(value) },
    )
}
