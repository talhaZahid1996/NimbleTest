package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName
import com.nimbletest.app.BuildConfig


data class LogoutRequest(
    @SerializedName("client_id")
    val clientId: String = BuildConfig.NIMBLE_KEY,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.NIMBLE_SECRET,
    @SerializedName("token")
    val token: String? = null
)