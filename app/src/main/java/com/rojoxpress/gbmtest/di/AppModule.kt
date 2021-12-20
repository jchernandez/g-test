package com.rojoxpress.gbmtest.di

import com.rojoxpress.gbmtest.repository.DataRepoImpl
import com.rojoxpress.gbmtest.repository.DataRepository
import com.rojoxpress.gbmtest.repository.RemoteDao
import com.rojoxpress.gbmtest.utils.Retrofit
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class AppModule {

    @Binds
    abstract fun getRepository(repo: DataRepoImpl): DataRepository
}