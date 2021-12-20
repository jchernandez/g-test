package com.rojoxpress.gbmtest.repository

import com.rojoxpress.gbmtest.model.IPC
import com.rojoxpress.gbmtest.model.Resource
import com.rojoxpress.gbmtest.model.Top

interface DataRepository {
    suspend fun getTop(): Resource<List<Top>>
    suspend fun getIPC(): Resource<List<IPC>>

}