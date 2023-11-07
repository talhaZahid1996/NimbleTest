package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class Survey(
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("attributes")
    val attributes: Attributes,
    @SerializedName("relationships")
    val relationships: Relationships
)