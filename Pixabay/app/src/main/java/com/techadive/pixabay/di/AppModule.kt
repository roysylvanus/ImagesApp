package com.techadive.pixabay.di

import android.app.Application
import android.content.Context
import com.techadive.pixabay.common.MarketManager
import com.techadive.pixabay.data.api.local.DefaultLocalDataSource
import com.techadive.pixabay.data.api.remote.DefaultRemoteDataSource
import com.techadive.pixabay.data.repository.DefaultPixabayRepository
import com.techadive.pixabay.data.repository.PixabayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    fun provideMarketManager(@ApplicationContext context: Context): MarketManager =
        MarketManager(context)

    @Provides
    fun providePixabayRepository(defaultRemoteDataSource: DefaultRemoteDataSource, defaultLocalDataSource: DefaultLocalDataSource): PixabayRepository =
        DefaultPixabayRepository(defaultRemoteDataSource, defaultLocalDataSource)
}