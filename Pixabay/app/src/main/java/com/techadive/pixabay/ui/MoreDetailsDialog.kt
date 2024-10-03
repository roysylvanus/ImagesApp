package com.techadive.pixabay.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.techadive.pixabay.R
import com.techadive.pixabay.viewmodel.MainViewModel

const val MoreDetailsDialog = "MoreDetailsDialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreDetailsDialog(
    onEvent: (MainViewModel.PixabayEvent) -> Unit,
) {
    BasicAlertDialog(onDismissRequest = {
        onEvent(MainViewModel.PixabayEvent.UpdateDialogStatus())
    }) {
        Column(
            modifier = Modifier
                .testTag(MoreDetailsDialog)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.background)
                .wrapContentSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.would_you_like_more_details),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                TextButton(modifier = Modifier
                    .weight(1f),
                    onClick = {
                        onEvent(MainViewModel.PixabayEvent.UpdateDialogStatus())
                    }) {
                    Text(
                        text = stringResource(id = R.string.cancel),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onEvent(MainViewModel.PixabayEvent.ShowItemDetails)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.yes),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}