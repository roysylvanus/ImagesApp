package com.techadive.pixabay.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techadive.pixabay.R
import com.techadive.pixabay.viewmodel.MainViewModel

const val MainListView = "MainListView"
const val SearchImageField = "SearchImageField"
const val CircularLoadingBar = "CircularLoadingBar"
const val MainImageList = "MainImageList"
const val NoAvailableItemsInfo = "NoAvailableItemsInfo"

@Composable
fun MainListView(
    mainViewUIState: MainViewModel.MainViewUIState,
    paddingValues: PaddingValues,
    onEvent: (MainViewModel.PixabayEvent) -> Unit
) {

    if (mainViewUIState.isDialogVisible) {
        MoreDetailsDialog(onEvent)
    }

    val searchImageResponse = mainViewUIState.searchImageResponse

    Column (modifier = Modifier
        .testTag(MainListView)
        .fillMaxSize()
        .padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding(),
            start = 16.dp,
            end = 16.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        TextField(
            modifier = Modifier
                .testTag(SearchImageField)
                .fillMaxWidth(),
            value = mainViewUIState.searchQuery,
            onValueChange = {
                onEvent(MainViewModel.PixabayEvent.SearchImage(it))
            },
            label = {
                Text(
                    text = stringResource(id = R.string.search_image),
                    color = Color.DarkGray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.background,
                errorLabelColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.background,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.onBackground,
            ),
            trailingIcon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.White.copy(0.25F)
                )
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (mainViewUIState.isLoading) {
                CircularProgressIndicator(modifier = Modifier
                    .testTag(CircularLoadingBar)
                    .align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .testTag(MainImageList)
                        .align(Alignment.TopStart)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (searchImageResponse != null) {
                        if (searchImageResponse.hits.isEmpty()) {
                            item {
                                Text(
                                    modifier = Modifier.testTag(NoAvailableItemsInfo),
                                    text = stringResource(R.string.no_available_items),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        } else {
                            itemsIndexed(searchImageResponse.hits) { index, imageResult ->
                                ListItemView(
                                    indexAndListSize = Pair(index, searchImageResponse.hits.size),
                                    imageResult = imageResult,
                                    onEvent = onEvent
                                )
                            }
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun MainListView_Preview() {
    MainListView(
        MainViewModel.MainViewUIState(),
        PaddingValues(),
    ) { }
}