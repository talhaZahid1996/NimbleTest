package com.nimbletest.app.data.models

data class ForgotPasswordResponse(
    val meta: ForgotPasswordMeta
)

data class ForgotPasswordMeta(
    val message: String
)
