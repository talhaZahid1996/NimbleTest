package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class Relationships(
    @SerializedName("questions")
    val questions: Questions
)