package com.mivanovskaya.gittest.di

import com.mivanovskaya.gittest.data.AppRepositoryImpl
import com.mivanovskaya.gittest.domain.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface Binds {

    @Binds
    fun bindsAppRepository(repository: AppRepositoryImpl): AppRepository
}