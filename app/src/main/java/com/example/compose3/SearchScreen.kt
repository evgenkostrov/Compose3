package com.narcissus.marketplace.ui.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val state = rememberSearchState(
            searchHistory = viewModel.searchHistoryList,

            ) { query: TextFieldValue ->
            viewModel.getResultList(query.text)
        }

        SearchBar(
            query = state.query,
            onQueryChange = { state.query = it },
            onSearchFocusChange = { state.focused = it },
            onClearQuery = { state.query = TextFieldValue("") },
            onBack = { state.query = TextFieldValue("") },
            searching = state.searching,
            focused = state.focused,
            modifier = modifier,
        )

        when (state.searchContainer) {

            SearchContainer.NoResults -> {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("❌ No Results!", fontSize = 24.sp, color = Color(0xffDD2C00))
                }
            }

            SearchContainer.History -> {
                SuggestionGridLayout(suggestions = state.searchHistory) {
                    var text = state.query.text
                    if (text.isEmpty()) text = it else text += " $it"
                    text.trim()
                    // Set text and cursor position to end of text
                    state.query = TextFieldValue(text, TextRange(text.length))
                }
            }

            SearchContainer.Results -> {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun SuggestionGridLayout(
    modifier: Modifier = Modifier,
    suggestions: List<SearchHistoryModel>,
    onSuggestionClick: (String) -> Unit,
) {

   Row(
        modifier = modifier.padding(4.dp),
    ) {
        suggestions.forEach { suggestionModel ->
            ItemSearchHistory(
                modifier = Modifier.padding(4.dp),
                suggestion = suggestionModel,
                onClick = {
                    onSuggestionClick(it.suggestion)
                },
                onCancel = {

                },
            )
        }
    }
}

@Composable
fun ItemSearchHistory(
    modifier: Modifier = Modifier,
    suggestion: SearchHistoryModel,
    onClick: ((SearchHistoryModel) -> Unit)? = null,
    onCancel: ((SearchHistoryModel) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable {
                onClick?.run {
                    invoke(suggestion)
                }
            }
            .padding(vertical = 8.dp, horizontal = 10.dp),
    ) {

        Text(
            text = suggestion.suggestion,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(end = 8.dp),
        )
        IconButton(
            onClick = {
                onCancel?.run {
                    invoke(suggestion)
                }
            },
            modifier = Modifier
                .size(16.dp)
                .padding(1.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                tint = Color(0xFFE0E0E0),
                contentDescription = null,
            )
        }
    }
}


