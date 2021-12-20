package com.rojoxpress.gbmtest.utils

import android.util.Log
import com.rojoxpress.gbmtest.config.AppConfig
import com.rojoxpress.gbmtest.config.AppConfig.IO_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
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

    fun Throwable.printRetrofitError() {
        this.printStackTrace()
        when (this) {
            is IOException -> Log.e(
                this::class.java.simpleName,
                "Network Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            is HttpException -> Log.e(
                this::class.java.simpleName,
                "HTTP Exception happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
            else -> Log.e(
                this::class.java.simpleName,
                "Unknown Error happened in Retrofit | cause: ${this.cause} | message: ${this.message}"
            )
        }
    }
}