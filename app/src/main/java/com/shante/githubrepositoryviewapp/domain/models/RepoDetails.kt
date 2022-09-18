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
    val license : License?,
    @SerializedName("forks_count")
    val forks: Int,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("watchers_count")
    val watchers: Int,
)

@Serializable
data class ReadMe(
    @SerializedName("content")
    val content:String
)

@Serializable
data class License(
    val key : String,
    val name: String,
    val spdx_id: String,
    val url: String,
    val node_id : String
)