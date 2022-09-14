package com.shante.githubrepositoryviewapp.domain.repository

import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.models.RepoDetails
import com.shante.githubrepositoryviewapp.domain.models.UserInfo

interface AppRepository {

    suspend fun getRepositories(): List<Repo>

    suspend fun getRepository(repoId: String): RepoDetails

    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String

    suspend fun signIn(token: String): UserInfo

    fun saveTokenInSharedPreferences(token: String)

    fun getTokenFromSharedPreferences() : String?

    fun removeTokenFromSharedPreferences()

}