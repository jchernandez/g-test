package com.rojoxpress.gbmtest.utils

import com.rojoxpress.gbmtest.config.AppConfig
import com.rojoxpress.gbmtest.config.AppConfig.IO_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Retrofit {
    fun getClient(baseUrl: String = AppConfig.API_ENDPOINT): retrofit2.Retrofit =
        retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(IO_TIMEOUT, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}