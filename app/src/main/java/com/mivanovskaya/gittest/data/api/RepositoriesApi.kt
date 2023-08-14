package com.mivanovskaya.gittest.data.api

import com.mivanovskaya.gittest.data.dto.RepoDto
import com.mivanovskaya.gittest.data.dto.UserInfoDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RepositoriesApi {

    @GET("user")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserInfoDto

    @GET("users/{user}/repos")
    suspend fun getRepositories(
        @Path("user") user: String,
        @Query("per_page") limit: Int,
        @Query("page") page: Int
    ): List<RepoDto>

    @GET("repos/{user}/{repo}")
    suspend fun getRepository(
        @Path("user") user: String,
        @Path("repo") repoName: String
    ): RepoDto
}