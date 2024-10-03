package com.techadive.pixabay.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.techadive.pixabay.R
import com.techadive.pixabay.common.convertToList
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.viewmodel.MainViewModel

const val ListItemView = "ListItemView"
const val ListItemImage = "ListItemImage"
const val ListItemUsername = "ListItemUsername"
const val ListItemTagsRow = "ListItemTagsRow"


@Composable
fun ListItemView(
    indexAndListSize: Pair<Int, Int>,
    imageResult: ImageResult,
    onEvent: (MainViewModel.PixabayEvent) -> Unit
) {
    Column(modifier = Modifier
        .testTag(ListItemView)
        .clickable {
        onEvent(MainViewModel.PixabayEvent.UpdateDialogStatus(true, imageResult))
    }) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .testTag(ListItemImage)
                    .size(80.dp)
                    .clip(RoundedCornerShape(10)),
                model = imageResult.largeImageURL,
                contentScale = ContentScale.Crop,
                contentDescription = ListItemImage,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_error_loading_image)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    modifier = Modifier.testTag(ListItemUsername),
                    text = imageResult.user,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier.testTag(ListItemTagsRow),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(imageResult.tags.convertToList()) { tag ->
                        TagView(tag)
                    }
                }
            }
        }

        if (indexAndListSize.first < indexAndListSize.second - 1) {
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}