package com.shante.githubrepositoryviewapp.data

import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.models.RepoDetails
import com.shante.githubrepositoryviewapp.domain.models.UserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface RestGitHubApi {

    @GET("user/repos")
    suspend fun getRepositories(): List<Repo>

    @GET("repositories/{repoId}")
    suspend fun getRepository(@Path("repoId") repoId: String): RepoDetails

    @GET("repos/{user}/{repository}/readme")
    suspend fun getRepositoryReadme(
        @Path("user") ownerName: String,
        @Path("repository") repositoryName: String,
        branchName: String
    ): String

    @GET("user")
    suspend fun signIn(@Header("Authorization") token: String): UserInfo

}