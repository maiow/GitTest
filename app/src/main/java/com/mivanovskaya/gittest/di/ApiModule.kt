package com.mivanovskaya.gittest.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mivanovskaya.gittest.data.Api
import com.mivanovskaya.gittest.data.KeyValueStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

//    @Provides
//    fun provideAuthTokenInterceptor(tokenProvider: AuthTokenProvider): Interceptor =
//        AuthTokenInterceptor(tokenProvider)

//    @Provides
//    @Singleton
//    fun providekeyValueStorage() = KeyValueStorage(@ApplicationContext context: Context)

    @Provides
    @Singleton
    fun provideOkHttpClient(
      //  authTokenInterceptor: Interceptor
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
//        OkHttpClient.Builder()
//        .addInterceptor(authTokenInterceptor)
//        .build()

    @Provides
    @Singleton
    fun provideSerializer(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, serializer: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create(serializer))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)
}