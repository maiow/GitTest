package com.mivanovskaya.gittest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val login: String, //есть в пути запроса репо лист, пример: https://api.github.com/users/{login}/repos",
    val repos_url: String, //для запроса репо лист, пример: https://api.github.com/users/maiow/repos",
)