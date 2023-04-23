package com.mivanovskaya.gittest.data

import com.google.gson.GsonBuilder
import com.mivanovskaya.gittest.data.model.Repo
import com.mivanovskaya.gittest.data.model.UserInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.github.com/"

object RetrofitService {
    private val xserializer = GsonBuilder().setLenient().create()

    private var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(xserializer))
        .build()

    //создание ретрофит-сервиса
    val api: Api = retrofit.create(
        Api::class.java
    )
}

interface Api {
    @Headers(
        "Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28"
    )

    @GET("user")
    suspend fun getUserInfo(
        //@Header("Accept: application/vnd.github+json")
        @Header("Authorization: Bearer ")
        @Path("token") token: String?
    ): UserInfo

    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String,
        @Query("per_page") limit: Int = 10,
        @Query("page") page: Int = 1,
    ): List<Repo>
}