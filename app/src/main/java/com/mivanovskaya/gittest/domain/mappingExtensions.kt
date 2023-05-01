package com.mivanovskaya.gittest.domain

import com.mivanovskaya.gittest.data.dto.LicenseDto
import com.mivanovskaya.gittest.data.dto.RepoDto
import com.mivanovskaya.gittest.data.dto.UserInfoDto
import com.mivanovskaya.gittest.domain.model.License
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.RepoDetails
import com.mivanovskaya.gittest.domain.model.UserInfo

fun UserInfoDto.toUserInfo() = UserInfo(login)

fun RepoDto.toRepo() = Repo(description, id, language, name)

fun List<RepoDto>.toListRepo(): List<Repo> =
    this.map { item -> item.toRepo() }

fun RepoDto.toRepoDetails() = RepoDetails(
    description, forks_count, html_url, id, language, license?.toLicense(), name, stargazers_count,
    watchers_count
)

fun LicenseDto.toLicense() = License(name)
