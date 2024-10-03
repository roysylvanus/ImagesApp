package com.techadive.pixabay.data.api.remote

import android.util.Log
import com.techadive.pixabay.common.ApiConstants
import com.techadive.pixabay.common.MarketManager
import com.techadive.pixabay.common.PixabayResult
import com.techadive.pixabay.data.api.PixabayApiService
import com.techadive.pixabay.data.model.SearchImageResponse
import java.lang.Exception
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun searchImages(
        query: String
    ): PixabayResult<SearchImageResponse?>
}

class DefaultRemoteDataSource @Inject constructor(
    private val pixabayApiService: PixabayApiService,
    private val marketManager: MarketManager,
): RemoteDataSource {
    override suspend fun searchImages(
        query: String
    ): PixabayResult<SearchImageResponse?> {
        val response = pixabayApiService
            .searchImages(
                apiKey = ApiConstants.API_KEY,
                query = query,
                lang = marketManager.language
            )

        return if (response.isSuccessful) {
            Log.i("searchImages",  response.body().toString())
            PixabayResult.Success(response.body())
        } else {
            PixabayResult.Error(Exception(response.code().toString()))
        }
    }
}