package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val profileAttributes: ProfileAttributes
)