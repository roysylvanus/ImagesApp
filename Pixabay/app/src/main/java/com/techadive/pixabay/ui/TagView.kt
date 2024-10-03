package com.techadive.pixabay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

const val TagView = "TagView"

@Composable
fun TagView(tag: String) {
    Box(
        modifier = Modifier
            .testTag(TagView)
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.onBackground)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(5.dp),
            text = tag,
            color = MaterialTheme.colorScheme.background,
        )
    }
}