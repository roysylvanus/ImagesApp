package com.techadive.pixabay.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DefaultNetworkManagerTest {

    private lateinit var defaultNetworkManager: DefaultNetworkManager

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Mock
    private lateinit var mockNetworkInfo: NetworkInfo

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            mockConnectivityManager
        )
        defaultNetworkManager = DefaultNetworkManager(mockContext)
    }

    @Test
    fun `Given isNetworkAvailable is called, when network is available, then return true`() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(true)

        val result = defaultNetworkManager.isNetworkAvailable()

        assertThat(result, equalTo(true))
    }

    @Test
    fun `Given isNetworkAvailable is called, when network is not connected, then return true`() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnected).thenReturn(false)

        val result = defaultNetworkManager.isNetworkAvailable()

        assertThat(result, equalTo(false))
    }

    @Test
    fun `Given isNetworkAvailable is called, when network info is not available, then return false`() {
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(null)

        val result = defaultNetworkManager.isNetworkAvailable()

        assertThat(result, equalTo(false))
    }
}