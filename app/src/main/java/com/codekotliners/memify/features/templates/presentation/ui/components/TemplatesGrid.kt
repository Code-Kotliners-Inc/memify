package com.codekotliners.memify.features.templates.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.templates.presentation.state.TabState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TemplatesGrid(
    currentState: TabState.Content,
    onTemplateSelected: (String) -> Unit,
    onLoadMore: () -> Unit,
    onLikeToggle: (id: String) -> Unit,
) {
    val listState = rememberLazyStaggeredGridState()
    val preloadWhenLeft = 20
    val coroutineScope = rememberCoroutineScope()
    var bumpLoading by remember { mutableStateOf(false) }
    val isAtBottom by remember {
        derivedStateOf {
            val layout = listState.layoutInfo
            val lastVisible = layout.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= layout.totalItemsCount - preloadWhenLeft
        }
    }
    val nestedScroll =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (available.y < 0 && isAtBottom && !bumpLoading) {
                        bumpLoading = true
                        coroutineScope.launch {
                            delay(400)
                            bumpLoading = false
                            onLoadMore()
                        }
                    }
                    return Offset.Zero
                }
            }
        }

    LazyVerticalStaggeredGrid(
        state = listState,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier =
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
                .nestedScroll(nestedScroll),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(currentState.templates) { template ->
                TemplateItem(
                    template = template,
                    onTemplateSelected = onTemplateSelected,
                    onLikeToggle,
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                if (currentState.isLoadingMore || bumpLoading) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }
            }
        },
    )
}
