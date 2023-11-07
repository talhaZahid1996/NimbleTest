package com.nimbletest.app.data.api

import java.io.IOException

sealed class NetworkResponse<out T : Any, out U : Any> {
    /**
     * Success response with body
     */
    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * separate object for HTTP 204 responses so that we can make Success's body non-null.
     */
    object Empty : NetworkResponse<Nothing, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<U : Any>(val body: U?, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
}