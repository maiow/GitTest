package com.mivanovskaya.gittest.domain

import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.RepoDetails
import com.mivanovskaya.gittest.domain.model.UserInfo

interface AppRepository {

    suspend fun getRepositories(
        limit: Int,
        page: Int
    ): List<Repo>

    suspend fun getRepository(repoName: String): RepoDetails

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String?

    suspend fun signIn(token: String): UserInfo

    fun getToken(): String?
    fun resetToken()
    fun saveCredentials(login: String, token: String)
    fun logout()
}