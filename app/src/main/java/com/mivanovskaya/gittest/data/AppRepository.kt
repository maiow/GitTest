package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.UserInfo
import com.mivanovskaya.gittest.domain.toListRepo
import com.mivanovskaya.gittest.domain.toUserInfo
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val api: Api,
    private val keyValueStorage: KeyValueStorage
) {
    suspend fun getRepositories(): List<Repo> =
        api.getRepositories(keyValueStorage.login ?: "", REPOS_QUANTITY, PAGES).toListRepo()

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

        return api.getUserInfo().toUserInfo()
    }

    fun getToken() = keyValueStorage.authToken

    fun resetToken() {
        keyValueStorage.authToken = null
    }

    fun saveLogin(login: String) {
        keyValueStorage.login = login
    }

    companion object {
        private const val REPOS_QUANTITY = 10
        private const val PAGES = 1
    }
}