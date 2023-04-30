package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.data.model.Repo
import com.mivanovskaya.gittest.data.model.UserInfo
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val api: Api,
    private val keyValueStorage: KeyValueStorage
) {
    suspend fun getRepositories(): List<Repo> = api.getRepositories(keyValueStorage.login ?: "")

    //    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }
//
    suspend fun signIn(token: String): UserInfo {
        keyValueStorage.authToken = token

        return api.getUserInfo()
    }

    fun getToken() = keyValueStorage.authToken

    fun resetToken() {
        keyValueStorage.authToken = null
    }

    fun saveLogin(login: String) {
        keyValueStorage.login = login
    }
}