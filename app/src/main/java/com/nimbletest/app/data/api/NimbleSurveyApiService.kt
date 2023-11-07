package com.nimbletest.app.data.api

import com.nimbletest.app.data.models.ApiResponse
import com.nimbletest.app.data.models.ErrorResponse
import com.nimbletest.app.data.models.ForgotPasswordRequest
import com.nimbletest.app.data.models.ForgotPasswordResponse
import com.nimbletest.app.data.models.LoginRequest
import com.nimbletest.app.data.models.LoginResponse
import com.nimbletest.app.data.models.LogoutRequest
import com.nimbletest.app.data.models.ProfileResponse
import com.nimbletest.app.data.models.RefreshTokenRequest
import com.nimbletest.app.data.models.Survey
import com.nimbletest.app.util.HTTPRoutes
import com.nimbletest.app.util.NotRequiredAuthorization
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NimbleSurveyApiService {
    // auth endpoints
    @POST(HTTPRoutes.AUTH)
    @NotRequiredAuthorization
    suspend fun login(
        @Body body: LoginRequest
    ): NetworkResponse<ApiResponse<LoginResponse>, ErrorResponse>

    @POST(HTTPRoutes.AUTH)
    @NotRequiredAuthorization
    suspend fun refreshToken(
        @Body body: RefreshTokenRequest
    ): NetworkResponse<ApiResponse<LoginResponse>, ErrorResponse>

    @POST(HTTPRoutes.LOGOUT)
    @NotRequiredAuthorization
    suspend fun logout(
        @Body body: LogoutRequest
    ): NetworkResponse<Unit, ErrorResponse>

    @GET(HTTPRoutes.PROFILE)
    suspend fun getProfile(): NetworkResponse<ApiResponse<ProfileResponse>, ApiResponse<ProfileResponse>>

    @POST(HTTPRoutes.FORGOT_PASSWORD)
    @NotRequiredAuthorization
    suspend fun forgotPassword(
        @Body body: ForgotPasswordRequest
    ): NetworkResponse<ApiResponse<ForgotPasswordResponse>, ErrorResponse>

    // survey endpoints
    @GET(HTTPRoutes.SURVEY_LIST)
    suspend fun getSurveyList(
        @Query("page[number]") page: Int? = null,
        @Query("page[size]") pageSize: Int? = null,
    ): NetworkResponse<ApiResponse<List<Survey>>, ApiResponse<List<Survey>>>
}