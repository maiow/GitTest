package com.mivanovskaya.gittest.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mivanovskaya.gittest.data.Api
import com.mivanovskaya.gittest.data.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        keyValueStorage: KeyValueStorage
    ): OkHttpClient =
        OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${keyValueStorage.authToken}")
                    .addHeader("X-GitHub-Api-Version", "2022-11-28")
                    .addHeader("Accept", "application/vnd.github+json")
                    .build()
            chain.proceed(request)
        }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}