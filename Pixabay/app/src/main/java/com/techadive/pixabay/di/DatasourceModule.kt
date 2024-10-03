package com.techadive.pixabay.di

import android.content.Context
import androidx.room.Room
import com.techadive.pixabay.common.MarketManager
import com.techadive.pixabay.data.api.PixabayApiService
import com.techadive.pixabay.data.api.local.AppDatabase
import com.techadive.pixabay.data.api.local.ImageResultDao
import com.techadive.pixabay.data.api.local.DefaultLocalDataSource
import com.techadive.pixabay.data.api.local.LocalDataSource
import com.techadive.pixabay.data.api.remote.DefaultRemoteDataSource
import com.techadive.pixabay.data.api.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: PixabayApiService,
        marketManager: MarketManager
    ): RemoteDataSource =
        DefaultRemoteDataSource(apiService, marketManager)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pixabay_images.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideImageResultDao(appDatabase: AppDatabase): ImageResultDao =
        appDatabase.imageResultDao()


    @Provides
    @Singleton
    fun provideLocalDataSource(imageResultDao: ImageResultDao): LocalDataSource =
        DefaultLocalDataSource(imageResultDao)
}