package com.mivanovskaya.gittest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
//    val avatar_url: String,
//    val bio: Any?,
//    val blog: String,
//    val collaborators: Int,
//    val company: Any?,
//    val created_at: String,
//    val disk_usage: Int,
//    val email: Any?,
//    val events_url: String,
//    val followers: Int,
//    val followers_url: String,
//    val following: Int,
//    val following_url: String,
//    val gists_url: String,
//    val gravatar_id: String,
//    val hireable: Any?,
//    val html_url: String,
//    val id: Int,
//    val location: String,
    val login: String, //есть в пути запроса репо лист, пример: https://api.github.com/users/{login}/repos",
//    val name: String,
//    val node_id: String,
//    val organizations_url: String,
//    val owned_private_repos: Int,
//    val plan: Plan,
//    val private_gists: Int,
//    val public_gists: Int,
//    val public_repos: Int,
//    val received_events_url: String,
    val repos_url: String, //для запроса репо лист, пример: https://api.github.com/users/maiow/repos",
//    val site_admin: Boolean,
//    val starred_url: String,
//    val subscriptions_url: String,
//    val total_private_repos: Int,
//    val twitter_username: Any?,
//    val two_factor_authentication: Boolean,
//    val type: String,
//    val updated_at: String,
//    val url: String
) /*{
    data class Plan(
        val collaborators: Int,
        val name: String,
        val private_repos: Int,
        val space: Int
    )
}*/