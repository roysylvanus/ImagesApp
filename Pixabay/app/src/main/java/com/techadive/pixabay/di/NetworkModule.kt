package com.techadive.pixabay.di

import android.content.Context
import com.techadive.pixabay.common.DefaultNetworkManager
import com.techadive.pixabay.common.NetworkManager
import com.techadive.pixabay.data.api.PixabayApiService
import com.techadive.pixabay.data.api.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        RetrofitInstance().getClient()

    @Provides
    @Singleton
    fun providePixabayApiService(retrofit: Retrofit): PixabayApiService =
        retrofit.create(PixabayApiService::class.java)

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager =
        DefaultNetworkManager(context)
}