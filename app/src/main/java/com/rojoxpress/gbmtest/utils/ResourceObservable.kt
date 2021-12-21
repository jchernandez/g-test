package com.rojoxpress.gbmtest.utils
import androidx.lifecycle.Observer
import com.rojoxpress.gbmtest.model.Error
import com.rojoxpress.gbmtest.model.Resource
import com.rojoxpress.gbmtest.model.Status

interface  ResourceObserver<T>: Observer<Resource<T>> {

    override fun onChanged(t: Resource<T>) {
        if (t.status == Status.SUCCESS) {
            onSuccess()
        } else if (t.status == Status.ERROR) {
            onError(t.error!!)
        }
    }

    fun onSuccess()
    fun onError(error: Error)
}