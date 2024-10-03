package com.techadive.pixabay

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.techadive.pixabay.common.ui.BackTag
import com.techadive.pixabay.di.AppModule
import com.techadive.pixabay.di.NetworkModule
import com.techadive.pixabay.ui.CircularLoadingBar
import com.techadive.pixabay.ui.ItemDetailView
import com.techadive.pixabay.ui.ItemDetailViewComments
import com.techadive.pixabay.ui.ItemDetailViewCommentsIcon
import com.techadive.pixabay.ui.ItemDetailViewDownloads
import com.techadive.pixabay.ui.ItemDetailViewDownloadsIcon
import com.techadive.pixabay.ui.ItemDetailViewImage
import com.techadive.pixabay.ui.ItemDetailViewLikes
import com.techadive.pixabay.ui.ItemDetailViewLikesIcon
import com.techadive.pixabay.ui.ItemDetailViewUsername
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
class ItemDetailViewTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun testItemDetailView_displaysImageAndDetails() {
        navigateToItemDetailScreen()

        // Verify image and details are displayed
        composeTestRule.onNodeWithContentDescription(ItemDetailViewImage).assertExists()

        assertUserDetails()
    }

    @Test
    fun testItemDetailView_backClickedNavigateToMainListView() {
        navigateToItemDetailScreen()

        // Simulate back button click and verify navigation to main list view
        composeTestRule.onNodeWithTag(BackTag)
            .assertExists()
            .performClick()

        // Verify that we are back in the main list view
        composeTestRule.onNodeWithTag(SearchImageField).assertExists()
        composeTestRule.waitForElementToDisappear(CircularLoadingBar)
        composeTestRule.onNodeWithTag(MainImageList).assertExists()
    }

    private fun navigateToItemDetailScreen() {
        // Verify main list view is loaded
        composeTestRule.onNodeWithTag(SearchImageField).assertExists()
        composeTestRule.waitForElementToDisappear(CircularLoadingBar)
        composeTestRule.onNodeWithTag(MainImageList).assertExists()

        // Simulate navigation to the item detail screen
        composeTestRule.onAllNodesWithTag(ListItemView)
            .onFirst()
            .assertExists()
            .performClick()

        // Handle confirmation dialog
        composeTestRule.onNodeWithTag(MoreDetailsDialog).assertExists()
        composeTestRule.onNodeWithText("Yes", useUnmergedTree = true)
            .assertExists()
            .performClick()

        // Wait for loading to complete and verify the detail view is displayed
        composeTestRule.waitForElementToDisappear(CircularLoadingBar)
        composeTestRule.onNodeWithTag(ItemDetailView).assertExists()
    }

    private fun assertUserDetails() {
        // Verify the username, likes, downloads, and comments
        composeTestRule.onNodeWithTag(ItemDetailViewUsername).assertTextContains("user1")
        composeTestRule.onNodeWithTag(ItemDetailViewLikes).assertTextContains("1")
        composeTestRule.onNodeWithTag(ItemDetailViewLikesIcon).assertExists()
        composeTestRule.onNodeWithTag(ItemDetailViewDownloads).assertTextContains("1")
        composeTestRule.onNodeWithTag(ItemDetailViewDownloadsIcon).assertExists()
        composeTestRule.onNodeWithTag(ItemDetailViewComments).assertTextContains("1")
        composeTestRule.onNodeWithTag(ItemDetailViewCommentsIcon).assertExists()
        composeTestRule.onNodeWithText("tag1").assertExists()
        composeTestRule.onNodeWithText("tag2").assertExists()
        composeTestRule.onNodeWithText("tag3").assertExists()
    }

    private fun ComposeTestRule.waitForElementToDisappear(tag: String, timeoutMillis: Long = 5000) {
        this.waitUntilDoesNotExist(hasTestTag(tag), timeoutMillis)
    }
}
