package com.techadive.pixabay.di

import android.app.Application
import android.content.Context
import com.techadive.pixabay.common.MarketManager
import com.techadive.pixabay.data.repository.PixabayRepository
import com.techadive.pixabay.repository.MockPixabayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    fun provideMarketManager(@ApplicationContext context: Context): MarketManager =
        MarketManager(context)

    @Provides
    fun providePixabayRepository(): PixabayRepository =
        MockPixabayRepository()
}