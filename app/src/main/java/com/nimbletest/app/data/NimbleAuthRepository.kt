package com.nimbletest.app.data

import com.nimbletest.app.data.api.NetworkResponse
import com.nimbletest.app.data.api.NimbleSurveyApiService
import com.nimbletest.app.data.datastore.DataStorePreferencesRepository
import com.nimbletest.app.data.models.ErrorResponse
import com.nimbletest.app.data.models.ForgotPasswordInfo
import com.nimbletest.app.data.models.ForgotPasswordRequest
import com.nimbletest.app.data.models.ForgotPasswordResponse
import com.nimbletest.app.data.models.LoginRequest
import com.nimbletest.app.data.models.LoginResponse
import com.nimbletest.app.data.models.LogoutRequest
import com.nimbletest.app.data.models.ProfileResponse
import com.nimbletest.app.data.models.UserInfo
import com.nimbletest.app.data.result.Result
import com.nimbletest.app.util.mapApiResponseToResult
import com.nimbletest.app.util.mapResponseToResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface NimbleAuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse, ErrorResponse>
    suspend fun logout(): Result<Unit, ErrorResponse>
    suspend fun forgotPassword(email: String): Result<ForgotPasswordResponse, ErrorResponse>
    suspend fun getProfile(): Result<ProfileResponse, *>
}

class NimbleAuthRepositoryImpl @Inject constructor(
    private val nimbleApiService: NimbleSurveyApiService,
    private val dataStorePreferencesRepository: DataStorePreferencesRepository
) : NimbleAuthRepository {
    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse, ErrorResponse> = mapApiResponseToResult {
        val result = nimbleApiService.login(
            LoginRequest(
                email = email,
                password = password
            )
        )
        // store token
        when (result) {
            is NetworkResponse.Success -> {
                dataStorePreferencesRepository.saveUserCredentials(
                    UserInfo(
                        accessToken = result.body.data?.attributes?.accessToken,
                        refreshToken = result.body.data?.attributes?.refreshToken,
                        expiresIn = result.body.data?.attributes?.expiresIn,
                        createdAt = result.body.data?.attributes?.createdAt
                    )
                )
            }

            else -> Unit
        }
        result
    }

    override suspend fun logout(): Result<Unit, ErrorResponse> = mapResponseToResult {
        val token = dataStorePreferencesRepository.userCredentials.first()?.accessToken
        val result = nimbleApiService.logout(
            LogoutRequest(token = token)
        )
        // clear token
        when (result) {
            is NetworkResponse.Success -> {
                dataStorePreferencesRepository.saveUserCredentials(UserInfo())
            }

            else -> Unit
        }
        result
    }

    override suspend fun forgotPassword(
        email: String
    ): Result<ForgotPasswordResponse, ErrorResponse> = mapApiResponseToResult {
        nimbleApiService.forgotPassword(
            ForgotPasswordRequest(
                forgotPasswordInfo = ForgotPasswordInfo(email = email)
            )
        )
    }

    override suspend fun getProfile(): Result<ProfileResponse, *> = mapApiResponseToResult {
        nimbleApiService.getProfile()
    }
}