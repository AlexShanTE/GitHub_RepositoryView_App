package com.shante.githubrepositoryviewapp.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserInfo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val name: String
) : Parcelable