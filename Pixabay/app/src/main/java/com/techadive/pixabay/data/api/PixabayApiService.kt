package com.techadive.pixabay.data.api

import com.techadive.pixabay.data.model.SearchImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("/api/")
    suspend fun searchImages(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("lang") lang: String,
        @Query("image_type") imageType: String = "photo"
    ): Response<SearchImageResponse>
}