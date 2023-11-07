package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thank_email_above_threshold")
    val thankEmailAboveThreshold: String,
    @SerializedName("thank_email_below_threshold")
    val thankEmailBelowThreshold: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("cover_image_url")
    val coverImageUrl: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("active_at")
    val activeAt: String,
    @SerializedName("inactive_at")
    val inactiveAt: Any?, // data type unknown at the moment
    @SerializedName("survey_type")
    val surveyType: String
)