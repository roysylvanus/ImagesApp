package com.techadive.pixabay.common

sealed class PixabayResult<out T> {
    data class Success<out T>(val data: T) : PixabayResult<T>()
    data class Error(val exception: Exception) : PixabayResult<Nothing>()
}