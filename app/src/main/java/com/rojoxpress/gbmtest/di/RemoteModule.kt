package com.rojoxpress.gbmtest.di

import com.rojoxpress.gbmtest.repository.RemoteDao
import com.rojoxpress.gbmtest.utils.Retrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RemoteModule {
    @Singleton
    @Provides
    fun provideRemoteDAO(): RemoteDao = Retrofit.getClient().create(RemoteDao::class.java)
}