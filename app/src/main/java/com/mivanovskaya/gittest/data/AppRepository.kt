package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.data.api.RepositoriesApi
import com.mivanovskaya.gittest.data.api.UserContentApi
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.RepoDetails
import com.mivanovskaya.gittest.domain.model.UserInfo
import com.mivanovskaya.gittest.domain.toListRepo
import com.mivanovskaya.gittest.domain.toRepoDetails
import com.mivanovskaya.gittest.domain.toUserInfo
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val repositoriesApi: RepositoriesApi,
    private val userApi: UserContentApi,
    private val keyValueStorage: KeyValueStorage
) {
    suspend fun getRepositories(): List<Repo> =
        repositoriesApi.getRepositories(keyValueStorage.login ?: "", REPOS_QUANTITY, PAGES)
            .toListRepo()

    suspend fun getRepository(repoId: String): RepoDetails =
        repositoriesApi.getRepository(keyValueStorage.login ?: "", repoId).toRepoDetails()

    suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String =
        userApi.getRepositoryReadme(ownerName, repositoryName, branchName)


    suspend fun signIn(token: String): UserInfo {
        keyValueStorage.authToken = token
        return repositoriesApi.getUserInfo().toUserInfo()
    }

    fun getToken() = keyValueStorage.authToken

    fun resetToken() {
        keyValueStorage.authToken = null
    }

    fun saveLogin(login: String) {
        keyValueStorage.login = login
    }

    fun logout() {
        keyValueStorage.login = null
        keyValueStorage.authToken = null
    }

    companion object {
        private const val REPOS_QUANTITY = 10
        private const val PAGES = 1
    }
}