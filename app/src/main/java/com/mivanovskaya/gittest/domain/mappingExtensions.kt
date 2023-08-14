package com.mivanovskaya.gittest.domain

import com.mivanovskaya.gittest.data.dto.LicenseDto
import com.mivanovskaya.gittest.data.dto.RepoDto
import com.mivanovskaya.gittest.data.dto.UserInfoDto
import com.mivanovskaya.gittest.domain.model.License
import com.mivanovskaya.gittest.domain.model.Repo
import com.mivanovskaya.gittest.domain.model.RepoDetails
import com.mivanovskaya.gittest.domain.model.UserInfo

fun UserInfoDto.toUserInfo() = UserInfo(login)

fun RepoDto.toRepo() = Repo(
    description = description,
    id = id,
    language = language,
    name = name
)

fun RepoDto.toRepoDetails() = RepoDetails(
    description = description,
    forksCount = forksCount,
    htmlUrl = htmlUrl,
    id = id,
    language = language,
    license = license?.toLicense(),
    name = name,
    stargazersCount = stargazersCount,
    watchersCount = watchersCount,
    owner = owner.login,
    defaultBranch = defaultBranch
)

fun LicenseDto.toLicense() = License(name)
