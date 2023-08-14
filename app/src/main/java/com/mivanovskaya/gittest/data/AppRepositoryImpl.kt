package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.data.api.RepositoriesApi
import com.mivanovskaya.gittest.data.api.UserContentApi
import com.mivanovskaya.gittest.di.IoDispatcher
import com.mivanovskaya.gittest.domain.AppRepository
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.RepoDetails
import com.mivanovskaya.gittest.domain.model.UserInfo
import com.mivanovskaya.gittest.domain.toRepo
import com.mivanovskaya.gittest.domain.toRepoDetails
import com.mivanovskaya.gittest.domain.toUserInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val repositoriesApi: RepositoriesApi,
    private val userApi: UserContentApi,
    private val keyValueStorage: KeyValueStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AppRepository {

    override suspend fun getRepositories(limit: Int, page: Int): List<Repo> =
        withContext(ioDispatcher) {
            repositoriesApi.getRepositories(
                user = requireNotNull(keyValueStorage.login) {
                    "Error: authorized username not found in storage"
                },
                limit = limit,
                page = page
            ).map { it.toRepo() }
        }

    override suspend fun getRepository(repoName: String): RepoDetails = withContext(ioDispatcher) {
        repositoriesApi.getRepository(
            user = requireNotNull(keyValueStorage.login) {
                "Error: authorized username not found in storage"
            },
            repoName = repoName
        ).toRepoDetails()
    }

    override suspend fun getRepositoryReadme(
        ownerName: String, repositoryName: String, branchName: String
    ): String = withContext(ioDispatcher) {
        userApi.getRepositoryReadme(
            ownerName = ownerName, repositoryName = repositoryName, branchName = branchName
        )
    }

    override suspend fun signIn(token: String): UserInfo = withContext(ioDispatcher) {
        repositoriesApi.getUserInfo("Bearer $token").toUserInfo()
    }

    override fun getToken(): String? = keyValueStorage.authToken

    override fun resetToken() {
        keyValueStorage.authToken = null
    }

    override fun saveCredentials(login: String, token: String) {
        keyValueStorage.login = login
        keyValueStorage.authToken = token
    }

    override fun logout() {
        keyValueStorage.login = null
        keyValueStorage.authToken = null
    }

}