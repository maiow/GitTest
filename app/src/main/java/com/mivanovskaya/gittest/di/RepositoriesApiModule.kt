package com.mivanovskaya.gittest.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mivanovskaya.gittest.data.KeyValueStorage
import com.mivanovskaya.gittest.data.api.RepositoriesApi
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
class RepositoriesApiModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        keyValueStorage: KeyValueStorage
    ): OkHttpClient =
        OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                .run {
                    if (keyValueStorage.authToken != null && originalRequest.headers["Authorization"] == null) {
                        this.addHeader("Authorization", "Bearer ${keyValueStorage.authToken}")
                    } else {
                        this
                    }
                }
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .addHeader("Accept", "application/vnd.github+json")
                .build()
            chain.proceed(request)
        }.build()

    @Provides
    @Singleton
    fun provideRetrofitForRepositoriesApi(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideRepositoriesApi(retrofit: Retrofit): RepositoriesApi =
        retrofit.create(RepositoriesApi::class.java)
}