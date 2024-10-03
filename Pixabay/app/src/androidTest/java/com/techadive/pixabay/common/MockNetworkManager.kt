package com.techadive.pixabay.common

class MockNetworkManager : NetworkManager {
    override fun isNetworkAvailable(): Boolean {
        // Simulate network availability
        return true
    }
}