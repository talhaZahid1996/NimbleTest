package com.nimbletest.app.data.models

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("page")
    val page: Int,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("page_size")
    val pageSize: Int,
    @SerializedName("records")
    val records: Int
)