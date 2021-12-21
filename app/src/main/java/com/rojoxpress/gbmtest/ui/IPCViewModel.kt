package com.rojoxpress.gbmtest.ui

import androidx.lifecycle.*
import com.github.mikephil.charting.data.Entry
import com.rojoxpress.gbmtest.model.IPC
import com.rojoxpress.gbmtest.model.Resource
import com.rojoxpress.gbmtest.model.Status
import com.rojoxpress.gbmtest.model.Top
import com.rojoxpress.gbmtest.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class IPCViewModel @Inject constructor(private val dataRepository: DataRepository): ViewModel() {

    private val _ipc = MutableLiveData<Resource<List<IPC>>>()
    val ipc: LiveData<Resource<List<IPC>>> = _ipc
    private val _top = MutableLiveData<Resource<List<Top>>>()
    val top: LiveData<Resource<List<Top>>> = _top
    val chartEntries = ArrayList<Entry>()
    var riseList: ArrayList<Top>? = null
    var fallList: ArrayList<Top>? = null
    var volumeList: ArrayList<Top>? = null

    fun getIPC() {
        viewModelScope.launch {
            _ipc.value = dataRepository.getIPC()
        }
    }


    fun setGraphEntries() {
        chartEntries.clear()
        ipc.value!!.data!!.forEach {
            val mFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz", Locale.ENGLISH)
            val date = mFormat.parse(it.date)!!
            val x = date.time.toFloat()
            val y = it.price
            chartEntries.add(Entry(x,y))
        }
    }

    fun getTops() {
        _top.value = Resource()
        viewModelScope.launch {
            val topResource = dataRepository.getTop()
            if (topResource.status == Status.SUCCESS) {
               divideList(topResource.data!!)
            }
            _top.value = topResource
        }
    }

    private fun divideList(topList: List<Top>) {
        riseList = ArrayList()
        fallList = ArrayList()
        volumeList = ArrayList()
        topList.forEach {
            when (it.riseLowTypeId) {
                Top.RISE -> riseList!!.add(it)
                Top.FALL -> fallList!!.add(it)
                else -> volumeList!!.add(it)
            }
        }
    }
}