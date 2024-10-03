package com.techadive.pixabay.common

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject

interface NetworkManager {
    fun isNetworkAvailable(): Boolean
}

class DefaultNetworkManager @Inject constructor(
    private val context: Context
): NetworkManager {
    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}