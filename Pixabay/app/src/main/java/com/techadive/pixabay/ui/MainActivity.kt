package com.techadive.pixabay.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techadive.pixabay.R
import com.techadive.pixabay.common.NetworkChangeReceiver
import com.techadive.pixabay.common.ui.ToolbarView
import com.techadive.pixabay.data.model.ImageResult
import com.techadive.pixabay.ui.theme.PixabayTheme
import com.techadive.pixabay.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavHostController
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            navController = rememberNavController()
            PixabayTheme {
                MainActivityView(
                    navController = navController,
                    mainViewUIState = viewModel.mainViewUIState.value,
                    itemDetailViewUIState = viewModel.itemDetailViewUIState.value,
                    onEvent = viewModel::onEvent
                )
            }
        }

        if (savedInstanceState == null) {
            viewModel.onEvent(MainViewModel.PixabayEvent.SearchImage(STARTER_QUERY))
        }

        networkChangeReceiver = NetworkChangeReceiver { isConnected ->
            viewModel.onEvent(MainViewModel.PixabayEvent.OnNetworkStatusChanged(isConnected))
        }

        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        initObservers()
    }

    private fun initObservers() {
        lifecycle.addObserver(viewModel)

        viewModel.pixabayEvent.observe(this) { event ->
            when (event) {
                is MainViewModel.PixabayEvent.ShowItemDetails -> {
                    navController.navigate(MainHostDestinations.ITEM_DETAIL_SCREEN.route)
                }

                MainViewModel.PixabayEvent.BackClicked -> {
                    navController.navigateUp()
                }

                else -> Unit
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    private companion object {
        const val STARTER_QUERY = "fruits"
    }
}

@Composable
fun MainActivityView(
    navController: NavHostController,
    mainViewUIState: MainViewModel.MainViewUIState,
    itemDetailViewUIState: ImageResult?,
    onEvent: (MainViewModel.PixabayEvent) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                ToolbarView(
                    isBackVisible = mainViewUIState.isBackVisible,
                    onBackClicked = { onEvent(MainViewModel.PixabayEvent.BackClicked) }
                )
            }
        },
        bottomBar = {
            if (mainViewUIState.hasError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(5.dp),
                        text = stringResource(R.string.internet_error),
                        fontSize = TextUnit(14f, TextUnitType.Sp),
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            paddingValues = innerPadding,
            mainViewUIState = mainViewUIState,
            itemDetailViewUIState = itemDetailViewUIState,
            onEvent = onEvent
        )
    }
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    mainViewUIState: MainViewModel.MainViewUIState,
    itemDetailViewUIState: ImageResult?,
    onEvent: (MainViewModel.PixabayEvent) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = MainHostDestinations.LIST_SCREEN.route
    ) {
        composable(MainHostDestinations.LIST_SCREEN.route) {
            MainListView(
                mainViewUIState = mainViewUIState,
                paddingValues = paddingValues,
                onEvent = onEvent
            )
        }
        composable(MainHostDestinations.ITEM_DETAIL_SCREEN.route) {
            ItemDetailView(
                paddingValues = paddingValues,
                imageResult = itemDetailViewUIState
            )
        }
    }
}

enum class MainHostDestinations(val route: String) {
    LIST_SCREEN("listScreen"),
    ITEM_DETAIL_SCREEN("itemDetailScreen")
}