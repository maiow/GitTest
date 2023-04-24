package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.data.model.Repo
import com.mivanovskaya.gittest.data.model.UserInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("user")
    suspend fun getUserInfo(): UserInfo

    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String,
        @Query("per_page") limit: Int = 10,
        @Query("page") page: Int = 1,
    ): List<Repo>
}