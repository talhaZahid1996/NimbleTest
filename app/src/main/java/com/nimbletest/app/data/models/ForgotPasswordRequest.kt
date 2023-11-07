package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName
import com.nimbletest.app.BuildConfig

data class ForgotPasswordRequest(
    @SerializedName("user")
    val forgotPasswordInfo: ForgotPasswordInfo,
    @SerializedName("client_id")
    val clientId: String = BuildConfig.NIMBLE_KEY,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.NIMBLE_SECRET,
)