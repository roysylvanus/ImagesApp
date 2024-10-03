package com.techadive.pixabay.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.techadive.pixabay.R

const val BackTag = "Back"

@Composable
fun ToolbarView(
    isBackVisible: Boolean,
    onBackClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .height(60.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isBackVisible) {
            IconButton(
                modifier = Modifier
                    .testTag(BackTag)
                    .align(Alignment.CenterStart)
                    .padding(start = 10.dp),
                onClick = {
                    onBackClicked()
                }) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}