package com.rojoxpress.gbmtest.model

import retrofit2.HttpException

data class Resource<T>(
    val status: Status,
    val data: T?,
    val error: Error?
) {
    constructor(e: HttpException) : this(Status.ERROR, null, Error(e.message))
    constructor(data: T?) : this(Status.SUCCESS, data, null)
}
enum class Status {
    SUCCESS, ERROR
}
