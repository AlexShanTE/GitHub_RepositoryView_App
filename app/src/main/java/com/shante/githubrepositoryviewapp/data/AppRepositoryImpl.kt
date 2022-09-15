package com.shante.githubrepositoryviewapp.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.shante.githubrepositoryviewapp.domain.models.KeyValueStorage
import com.shante.githubrepositoryviewapp.domain.models.Repo
import com.shante.githubrepositoryviewapp.domain.models.RepoDetails
import com.shante.githubrepositoryviewapp.domain.models.UserInfo
import com.shante.githubrepositoryviewapp.domain.repository.AppRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val gitApi: RestGitHubApi,
    application: Application
) : AppRepository {

    private val prefs = application.getSharedPreferences("token", Context.MODE_PRIVATE)

    override suspend fun getRepositories(): List<Repo> {
        val token: String? = getTokenFromSharedPreferences()
        Log.d("TAG", "Token from app repository is ${token.toString()}")
        return if (token.isNullOrBlank()) gitApi.getRepositories("")
        else gitApi.getRepositories(TOKEN_TYPE + token)
    }

    override suspend fun getRepository(repoId: String): RepoDetails {
        return gitApi.getRepository(repoId)
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String,
        branchName: String
    ): String {
        return gitApi.getRepositoryReadme(ownerName, repositoryName, branchName)
    }

    override suspend fun signIn(token: String): UserInfo {
        return gitApi.signIn(TOKEN_TYPE + token)
    }

    override fun saveTokenInSharedPreferences(token: String) {
        Log.d("TAG", "Incoming token in save in shared is $token")
        prefs.edit {
            val keyValueStorage = KeyValueStorage()
            keyValueStorage.authToken = token
            val serializedKeyValueStorage = Json.encodeToString(keyValueStorage)
            putString(TOKEN_PREFS_KEY, serializedKeyValueStorage)
        }
    }

    override fun getTokenFromSharedPreferences(): String? {
        val serializedKeyValueStorage = prefs.getString(TOKEN_PREFS_KEY, null)
        return if (serializedKeyValueStorage != null) {
            val keyValueStorage: KeyValueStorage = Json.decodeFromString(serializedKeyValueStorage)
            keyValueStorage.authToken.toString()
        } else null
    }

    override fun removeTokenFromSharedPreferences() {
        prefs.edit {
            remove(TOKEN_PREFS_KEY)
        }
    }

    companion object {
        const val TOKEN_TYPE = "Bearer "
        const val TOKEN_PREFS_KEY = "token prefs key"
    }
}