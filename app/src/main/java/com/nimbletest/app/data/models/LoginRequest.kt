package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName
import com.nimbletest.app.BuildConfig
import com.nimbletest.app.util.GrantTypes

data class LoginRequest(
    @SerializedName("grant_type")
    val grantType: String = GrantTypes.PASSWORD.value,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("client_id")
    val clientId: String = BuildConfig.NIMBLE_KEY,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.NIMBLE_SECRET
)