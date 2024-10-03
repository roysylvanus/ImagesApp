package com.techadive.pixabay

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techadive.pixabay.di.AppModule
import com.techadive.pixabay.di.NetworkModule
import com.techadive.pixabay.repository.MockPixabayRepository
import com.techadive.pixabay.ui.CircularLoadingBar
import com.techadive.pixabay.ui.ListItemImage
import com.techadive.pixabay.ui.ListItemView
import com.techadive.pixabay.ui.MainActivity
import com.techadive.pixabay.ui.MainImageList
import com.techadive.pixabay.ui.MoreDetailsDialog
import com.techadive.pixabay.ui.SearchImageField
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(AppModule::class, NetworkModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalTestApi::class)
class MainListViewTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testMainListView_displaysSearchFieldAndListItems() {
        // Verify search text field exists
        composeTestRule.onNodeWithTag(SearchImageField).assertExists()

        // Perform a search query
        composeTestRule.onNodeWithTag(SearchImageField).apply {
            performTextClearance()
            performTextInput("fruits")
        }

        // Simulate loading and verify the list is displayed after loading completes
        composeTestRule.onNodeWithTag(CircularLoadingBar).assertExists()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CircularLoadingBar), timeoutMillis = 5000)
        composeTestRule.onNodeWithTag(MainImageList).assertExists()

        // Verify the first and last items in the list
        composeTestRule.onAllNodesWithTag(ListItemView).apply {
            onFirst()
                .assert(hasText("user1"))
                .assert(hasContentDescription(ListItemImage))
                .assert(hasText("tag1"))
                .assert(hasText("tag2"))
                .assert(hasText("tag3"))

            onLast()
                .assert(hasText("user2"))
                .assert(hasContentDescription(ListItemImage))
                .assert(hasText("tag4"))
                .assert(hasText("tag5"))
                .assert(hasText("tag6"))
        }
    }

    @Test
    fun testMainListView_clickItemDialogIsShown() {
        // Verify search field and list view are loaded
        composeTestRule.onNodeWithTag(SearchImageField).assertExists()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CircularLoadingBar), timeoutMillis = 5000)
        composeTestRule.onNodeWithTag(MainImageList).assertExists()

        // Click the first item and verify the dialog is shown
        composeTestRule.onAllNodesWithTag(ListItemView)
            .onFirst()
            .assertExists()
            .performClick()

        // Verify dialog opens and handle Cancel button
        composeTestRule.onNodeWithTag(MoreDetailsDialog).assertExists()
        composeTestRule.onNodeWithText("Yes", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("Cancel", useUnmergedTree = true).assertExists()
            .performClick()
    }

    @Test
    fun testMainListView_whenApiReturnsError_ErrorTextIsShown() {
        MockPixabayRepository.errorState = true

        composeTestRule.onNodeWithTag(SearchImageField).assertExists()
        composeTestRule.waitUntilDoesNotExist(hasTestTag(CircularLoadingBar), timeoutMillis = 5000)
        composeTestRule.onNodeWithText("Something went wrong. Please check your internet connection.").assertExists()
    }
}
