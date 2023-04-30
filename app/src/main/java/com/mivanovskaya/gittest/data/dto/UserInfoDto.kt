package com.mivanovskaya.gittest.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    val login: String,
    val repos_url: String
)