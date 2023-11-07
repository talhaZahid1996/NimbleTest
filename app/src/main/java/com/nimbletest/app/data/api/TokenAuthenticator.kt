package com.nimbletest.app.data.api

import com.nimbletest.app.data.datastore.DataStorePreferencesRepository
import com.nimbletest.app.data.models.RefreshTokenRequest
import com.nimbletest.app.data.models.UserInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val dataStorePreferences: DataStorePreferencesRepository,
    private val apiService: Provider<NimbleSurveyApiService>,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val refreshToken = dataStorePreferences.userCredentials.first()?.refreshToken
            when (
                val result =
                    apiService.get()
                        .refreshToken(RefreshTokenRequest(refreshToken = refreshToken.orEmpty()))
            ) {
                is NetworkResponse.Success -> {
                    dataStorePreferences.saveUserCredentials(
                        UserInfo(
                            accessToken = result.body.data?.attributes?.accessToken,
                            refreshToken = result.body.data?.attributes?.refreshToken,
                            expiresIn = result.body.data?.attributes?.expiresIn,
                            createdAt = result.body.data?.attributes?.createdAt
                        )
                    )
                    newRequestWithAccessToken(
                        response.request,
                        result.body.data?.attributes?.accessToken.orEmpty()
                    )
                }

                else -> {
                    dataStorePreferences.saveUserCredentials(UserInfo())
                    null
                }
            }
        }
    }

    private fun newRequestWithAccessToken(request: Request, accessToken: String): Request {
        return request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
    }
}