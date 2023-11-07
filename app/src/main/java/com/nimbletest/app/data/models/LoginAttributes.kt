package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class LoginAttributes(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("created_at")
    val createdAt: Long
)