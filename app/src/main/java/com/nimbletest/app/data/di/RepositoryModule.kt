package com.nimbletest.app.data.di

import com.nimbletest.app.data.NimbleAuthRepository
import com.nimbletest.app.data.NimbleAuthRepositoryImpl
import com.nimbletest.app.data.NimbleSurveyRepository
import com.nimbletest.app.data.NimbleSurveyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNimbleAuthRepository(
        nimbleAuthRepository: NimbleAuthRepositoryImpl
    ): NimbleAuthRepository

    @Binds
    @Singleton
    abstract fun bindNimbleSurveyRepository(
        nimbleSurveyRepository: NimbleSurveyRepositoryImpl
    ): NimbleSurveyRepository
}