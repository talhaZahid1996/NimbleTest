package com.nimbletest.app.data.di

import android.app.Application
import com.google.gson.GsonBuilder
import com.nimbletest.app.BuildConfig
import com.nimbletest.app.data.api.NetworkResponseAdapterFactory
import com.nimbletest.app.data.api.NimbleSurveyApiService
import com.nimbletest.app.data.api.TokenAuthenticator
import com.nimbletest.app.data.datastore.DataStorePreferencesRepository
import com.nimbletest.app.util.NotRequiredAuthorization
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    internal fun provideCache(application: Application): Cache {
        val cacheSize = (25 * 1024 * 1024).toLong() // 25 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: Interceptor,
        cache: Cache,
        preferences: DataStorePreferencesRepository,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.apply {
            authenticator(tokenAuthenticator)
            cache(cache)
            addInterceptor(Interceptor { chain ->
                val originalRequest = chain.request()
                val method = originalRequest.tag(Invocation::class.java)!!.method()

                if (method.isAnnotationPresent(NotRequiredAuthorization::class.java)) {
                    chain.proceed(originalRequest)
                } else {
                    val token = runBlocking(Dispatchers.IO) {
                        preferences.userCredentials.first()?.accessToken.orEmpty()
                    }
                    val request = originalRequest.newBuilder()
                        .addHeader(AUTHORIZATION_HEADER, "Bearer $token")
                        .build()
                    chain.proceed(request)
                }
            })
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }

        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder().create()
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideScalarsConverterFactory(): ScalarsConverterFactory {
        return ScalarsConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofitApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory
    ): NimbleSurveyApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NIMBLE_BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(gsonConverterFactory)
            .addConverterFactory(scalarsConverterFactory)
            .client(okHttpClient)
            .build()
            .create(NimbleSurveyApiService::class.java)
    }

    private const val AUTHORIZATION_HEADER = "Authorization"
}