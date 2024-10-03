package com.techadive.pixabay.di

import com.techadive.pixabay.common.MockNetworkManager
import com.techadive.pixabay.common.NetworkManager
import com.techadive.pixabay.data.api.PixabayApiService
import com.techadive.pixabay.data.api.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object TestNetworkModule {
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
    fun provideNetworkManager(): NetworkManager =
        MockNetworkManager()
}