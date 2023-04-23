package com.mivanovskaya.gittest.data

import com.mivanovskaya.gittest.data.model.Repo
import com.mivanovskaya.gittest.data.model.UserInfo
import javax.inject.Inject

class AppRepository @Inject constructor(private val api: Api){
//    suspend fun getRepositories(): List<Repo> {
//        return api.getRepositories(user = UserInfo.login)
//    }

//    suspend fun getRepository(repoId: String): RepoDetails {
//        // TODO:
//    }
//
//    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
//        // TODO:
//    }
//
    suspend fun signIn(token: String): UserInfo {
        return api.getUserInfo(token)
    }

    // TODO:
}