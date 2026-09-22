package com.cnstrokesdict.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

/**
 * 网络客户端配置
 */
object NetworkClient {

    private const val BASE_URL = "https://api.cnstrokesdict.com/"

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val updateApi: UpdateApi = retrofit.create(UpdateApi::class.java)
}
