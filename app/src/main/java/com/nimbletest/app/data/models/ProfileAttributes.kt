package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class ProfileAttributes(
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)