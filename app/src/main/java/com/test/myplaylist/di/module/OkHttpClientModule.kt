package com.test.myplaylist.di.module

import android.content.Context
import com.test.myplaylist.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private const val READ_TIME_OUT = 45L

@Module
@InstallIn(SingletonComponent::class)
class OkHttpClientModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ) = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        // Dangerous interceptor that rewrites the server's cache-control header.
        val interceptor = Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            chain.proceed(requestBuilder.build())
        }

        val authInterceptor = Authenticator { _: Route?, response: Response ->
            val request = response.request
            if (response.code == 401) {
                null
            } else {
                request
            }
        }

        addInterceptor(interceptor)
        authenticator(authInterceptor)
        readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        connectTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
    }.build()
}
