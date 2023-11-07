package com.nimbletest.app.util

import com.nimbletest.app.data.api.NetworkResponse
import com.nimbletest.app.data.models.ApiResponse
import com.nimbletest.app.data.result.Result

suspend fun <R : Any, E : Any> mapApiResponseToResult(
    request: suspend () -> NetworkResponse<ApiResponse<R>, E>
): Result<R, E> {
    return when (val result = request()) {
        is NetworkResponse.Empty -> Result.Success(null)
        is NetworkResponse.Success -> {
            Result.Success(result.body.data)
        }

        is NetworkResponse.ApiError<E> -> {
            Result.Error(
                data = result.body,
                code = result.code,
            )
        }

        is NetworkResponse.NetworkError -> {
            Result.Error(
                message = result.error.message,
                exception = result.error
            )
        }

        is NetworkResponse.UnknownError -> {
            Result.Error(
                message = result.error?.message,
                exception = result.error
            )
        }
    }
}

suspend fun <R : Any, E : Any> mapResponseToResult(
    request: suspend () -> NetworkResponse<R, E>
): Result<R, E> {
    return when (val result = request()) {
        is NetworkResponse.Empty -> Result.Success(null)
        is NetworkResponse.Success -> {
            Result.Success(result.body)
        }

        is NetworkResponse.ApiError<E> -> {
            Result.Error(
                data = result.body,
                code = result.code,
            )
        }

        is NetworkResponse.NetworkError -> {
            Result.Error(
                message = result.error.message,
                exception = result.error
            )
        }

        is NetworkResponse.UnknownError -> {
            Result.Error(
                message = result.error?.message,
                exception = result.error
            )
        }
    }
}