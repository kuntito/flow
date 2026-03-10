package com.example.flow.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FlowApiClient {
    // TODO replace with stable base url
    private const val BASE_URL = "https://68fa-2-101-9-191.ngrok-free.app"

    // JSON parser with Kotlin support, apparently, JS has this in-built, so i didn't have to do it.
    private val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // allows to log requests and responses in `Logcat`
    private val okHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
        )
        .build()

    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(
            MoshiConverterFactory
                .create(moshi)
        )
        .build()

    val flowApi: FlowApiService = retrofit.create(
        FlowApiService::class.java
    )
}