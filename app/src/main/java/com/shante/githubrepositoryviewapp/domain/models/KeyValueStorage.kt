package com.shante.githubrepositoryviewapp.domain.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class KeyValueStorage {
    @SerializedName("authToken")
    var authToken: String? = null
}