package com.mivanovskaya.gittest.domain.model

data class Repo(
    val description: String?,
    val id: Int,
    val language: String?,
    val name: String
)

data class RepoDetails(
    val description: String?,
    val forks_count: Int,
    val html_url: String,
    val id: Int,
    val language: String?,
    val license: License?,
    val name: String,
    val stargazers_count: Int,
    val watchers_count: Int
)

class License(val name: String)

class Readme(val download_url: String?)
