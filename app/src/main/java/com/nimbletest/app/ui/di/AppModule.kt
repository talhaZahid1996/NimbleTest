package com.nimbletest.app.ui.di

import com.nimbletest.app.ui.notification.NotificationService
import com.nimbletest.app.ui.notification.NotificationServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindNotificationService(
        notificationService: NotificationServiceImpl
    ): NotificationService
}