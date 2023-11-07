package com.nimbletest.app.util

import com.nimbletest.app.BuildConfig

object HTTPRoutes {
    private const val BASE_URL = BuildConfig.NIMBLE_BASE_URL

    // auth endpoints
    const val AUTH = "${BASE_URL}/oauth/token"
    const val LOGOUT = "${BASE_URL}/oauth/revoke"
    const val PROFILE = "${BASE_URL}/me"
    const val FORGOT_PASSWORD = "${BASE_URL}/passwords"

    // survey endpoints
    const val SURVEY_LIST = "${BASE_URL}/surveys"
}