package com.techadive.pixabay.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.techadive.pixabay.R
import com.techadive.pixabay.common.convertToList
import com.techadive.pixabay.data.model.ImageResult

const val ItemDetailView = "ItemDetailViewTag"
const val ItemDetailViewImage = "ItemDetailViewImage"
const val ItemDetailViewUsername = "ItemDetailViewUsername"
const val ItemDetailViewLikesIcon = "ItemDetailViewLikesIcon"
const val ItemDetailViewLikes = "ItemDetailViewLikes"
const val ItemDetailViewDownloads = "ItemDetailViewDownloads"
const val ItemDetailViewDownloadsIcon = "ItemDetailViewDownloadsIcon"
const val ItemDetailViewComments = "ItemDetailViewComments"
const val ItemDetailViewCommentsIcon = "ItemDetailViewCommentsIcon"
const val ItemDetailViewTagsRow = "ItemDetailViewTagsRow"

@Composable
fun ItemDetailView(
    paddingValues: PaddingValues,
    imageResult: ImageResult?,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Display layout in landscape mode
        Row(
            modifier = Modifier
                .testTag(ItemDetailView)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Image on the left
            AsyncImage(
                modifier = Modifier
                    .testTag(ItemDetailViewImage)
                    .size(300.dp)
                    .clip(RoundedCornerShape(10)),
                model = imageResult?.largeImageURL,
                contentScale = ContentScale.Crop,
                contentDescription = ItemDetailViewImage,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_error_loading_image)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Details on the right
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ImageResultDetailsView(imageResult)
            }
        }
    } else {
        // Display layout in portrait mode
        Column(
            modifier = Modifier
                .testTag(ItemDetailView)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .testTag(ItemDetailViewImage)
                    .size(300.dp)
                    .clip(RoundedCornerShape(10)),
                model = imageResult?.largeImageURL,
                contentScale = ContentScale.Crop,
                contentDescription = ItemDetailViewImage,
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(R.drawable.ic_error_loading_image)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ImageResultDetailsView(imageResult)
        }
    }
}

@Composable
private fun ImageResultDetailsView(imageResult: ImageResult?) {
    Text(
        modifier = Modifier.testTag(ItemDetailViewUsername),
        text = imageResult?.user.orEmpty(),
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            modifier = Modifier.testTag(ItemDetailViewLikesIcon),
            contentDescription = stringResource(R.string.likes),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            modifier = Modifier.testTag(ItemDetailViewLikes),
            text = imageResult?.likes.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            painterResource(id = R.drawable.ic_download),
            modifier = Modifier.testTag(ItemDetailViewDownloadsIcon),
            contentDescription = stringResource(R.string.downloads),
            tint = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            modifier = Modifier.testTag(ItemDetailViewDownloads),
            text = imageResult?.downloads.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Icon(
            painterResource(id = R.drawable.ic_comments),
            modifier = Modifier.testTag(ItemDetailViewCommentsIcon),
            contentDescription = stringResource(R.string.comments),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            modifier = Modifier.testTag(ItemDetailViewComments),
            text = imageResult?.comments.toString(),
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    Spacer(modifier = Modifier.height(30.dp))

    if (imageResult?.tags != null) {
        LazyRow(
            modifier = Modifier.testTag(ItemDetailViewTagsRow),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(imageResult.tags.convertToList()) { tag ->
                TagView(tag)
            }
        }
    }
}