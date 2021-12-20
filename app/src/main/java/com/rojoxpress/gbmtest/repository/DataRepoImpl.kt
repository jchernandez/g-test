package com.rojoxpress.gbmtest.repository
import com.rojoxpress.gbmtest.model.IPC
import com.rojoxpress.gbmtest.model.Resource
import com.rojoxpress.gbmtest.model.Top
import com.rojoxpress.gbmtest.utils.Retrofit
import retrofit2.HttpException
import javax.inject.Inject

class DataRepoImpl @Inject constructor(private val remoteDao: RemoteDao) : DataRepository {

    override suspend fun getIPC(): Resource<List<IPC>> {
        return try {
            Resource(remoteDao.getIPC())
        } catch (e: HttpException) {
            Resource(e)
        }

    }
    override suspend fun getTop(): Resource<List<Top>> {
        return try {
            Resource(remoteDao.getTop())
        } catch (e: HttpException) {
            Resource(e)
        }

    }
}