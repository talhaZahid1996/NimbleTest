package com.nimbletest.app.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val accessToken: String? = null,
    val expiresIn: Long? = null,
    val refreshToken: String? = null,
    val createdAt: Long? = null,
)
