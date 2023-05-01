package com.mivanovskaya.gittest.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RepoDto(
    val description: String?,
    val forks_count: Int,
    val html_url: String,
    val id: Int,
    val language: String?,
    val license: LicenseDto?,
    val name: String,
    val stargazers_count: Int,
    val watchers_count: Int
)

@Serializable
data class LicenseDto(val name: String)

@Serializable
data class Readme(val download_url: String?)
