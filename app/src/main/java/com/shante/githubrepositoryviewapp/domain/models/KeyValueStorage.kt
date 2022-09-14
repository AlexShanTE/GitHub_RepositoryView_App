package com.shante.githubrepositoryviewapp.domain.models

import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
class KeyValueStorage {
    @SerializedName("authToken")
    var authToken: String? = null
}