package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName
import com.nimbletest.app.BuildConfig
import com.nimbletest.app.util.GrantTypes

data class RefreshTokenRequest(
    @SerializedName("grant_type")
    val grantType: String = GrantTypes.REFRESH.value,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("client_id")
    val clientId: String = BuildConfig.NIMBLE_KEY,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.NIMBLE_SECRET
)