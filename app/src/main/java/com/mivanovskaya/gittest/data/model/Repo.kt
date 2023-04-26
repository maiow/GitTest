package com.mivanovskaya.gittest.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val description: String?,
    val forks_count: Int, //?
    val html_url: String, //?
    val id: Int, //?
    val language: String?,
    val license: License?, //?
    val name: String,
    val stargazers_count: Int, //?
    val watchers_count: Int //?
)

@Serializable
data class License(val name: String)
