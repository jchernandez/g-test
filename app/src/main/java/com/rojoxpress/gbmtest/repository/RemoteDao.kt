package com.rojoxpress.gbmtest.repository

import com.rojoxpress.gbmtest.model.IPC
import com.rojoxpress.gbmtest.model.Top
import dagger.Module
import retrofit2.http.GET

interface  RemoteDao {

    @GET("cc4c350b-1f11-42a0-a1aa-f8593eafeb1e")
    suspend fun getIPC(): List<IPC>

    @GET("b4eb963c-4aee-4b60-a378-20cb5b00678f")
    suspend fun getTop():List<Top>

}