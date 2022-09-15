package com.shante.githubrepositoryviewapp.domain.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RepoDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("html_url")
    val html_url: String,
    @SerializedName("license")
    val license : String?,
    @SerializedName("forks_count")
    val forks: Int,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("watchers_count")
    val watchers: Int,
    @SerializedName("readme")
    val readme: String
)