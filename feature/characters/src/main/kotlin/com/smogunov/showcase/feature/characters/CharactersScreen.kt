package com.smogunov.showcase.feature.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.smogunov.showcase.core.designsystem.component.LoadingState
import com.smogunov.showcase.core.designsystem.component.MessageState
import com.smogunov.showcase.core.model.Character
import com.smogunov.showcase.core.ui.CharacterListItem

@Composable
internal fun CharactersScreen(
    onCharacterClick: (Int) -> Unit,
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    CharactersContent(
        characters = viewModel.characters.collectAsLazyPagingItems(),
        onCharacterClick = onCharacterClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharactersContent(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.feature_characters_title)) },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { padding ->
        val refreshState = characters.loadState.refresh
        PullToRefreshBox(
            isRefreshing = refreshState is LoadState.Loading && characters.itemCount > 0,
            onRefresh = characters::refresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                characters.itemCount > 0 -> CharactersList(characters, onCharacterClick)
                refreshState is LoadState.Loading -> LoadingState()
                refreshState is LoadState.Error -> MessageState(
                    title = stringResource(R.string.feature_characters_error),
                    actionLabel = stringResource(R.string.feature_characters_retry),
                    onAction = characters::retry,
                )
                else -> MessageState(title = stringResource(R.string.feature_characters_empty))
            }
        }
    }
}

@Composable
private fun CharactersList(
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            count = characters.itemCount,
            key = characters.itemKey { it.id },
            contentType = characters.itemContentType { "character" },
        ) { index ->
            characters[index]?.let { character ->
                CharacterListItem(character = character, onClick = { onCharacterClick(it.id) })
            }
        }

        when (characters.loadState.append) {
            is LoadState.Loading -> item(contentType = "loading") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is LoadState.Error -> item(contentType = "error") {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(onClick = characters::retry) {
                        Text(stringResource(R.string.feature_characters_retry))
                    }
                }
            }
            is LoadState.NotLoading -> Unit
        }
    }
}
