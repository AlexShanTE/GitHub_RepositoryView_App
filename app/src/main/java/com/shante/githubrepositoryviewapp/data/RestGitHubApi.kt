package com.shante.githubrepositoryviewapp.data

import com.shante.githubrepositoryviewapp.domain.models.ReadMe
import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.models.RepoDetails
import com.shante.githubrepositoryviewapp.domain.models.UserInfo
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RestGitHubApi {

    @GET("user/repos")
    suspend fun getRepositories(@Header("Authorization") token: String): List<Repo>

    @GET("repositories/{repoId}")
    suspend fun getRepository(@Path("repoId") repoId: String): RepoDetails

    // ref - The name of the commit/branch/tag. Default: the repository’s default branch (usually master)
    @GET("repos/{user}/{repository}/readme")
    suspend fun getRepositoryReadme(
        @Path("user") ownerName: String,
        @Path("repository") repositoryName: String,
        @Query("ref") branchName: String
    ): ReadMe

    @GET("user")
    suspend fun signIn(@Header("Authorization") token: String): UserInfo

}