package com.shante.githubrepositoryviewapp.domain.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String
)