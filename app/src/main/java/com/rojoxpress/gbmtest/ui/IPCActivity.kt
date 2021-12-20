package com.rojoxpress.gbmtest.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.rojoxpress.gbmtest.R
import com.rojoxpress.gbmtest.model.Error
import com.rojoxpress.gbmtest.model.IPC
import com.rojoxpress.gbmtest.utils.ResourceObserver
import com.github.mikephil.charting.formatter.ValueFormatter
import com.rojoxpress.gbmtest.databinding.ActivityIpcBinding
import java.text.SimpleDateFormat
import java.util.*
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineData
import com.rojoxpress.gbmtest.model.Top
import com.rojoxpress.gbmtest.ui.adapter.TopTenAdapter
import com.rojoxpress.gbmtest.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


@AndroidEntryPoint
class IPCActivity : AppCompatActivity() {

    private val viewModel by viewModels<IPCViewModel>()
    private lateinit var binding: ActivityIpcBinding
    private val timer = Timer()

    private val ipcObserver = object : ResourceObserver<List<IPC>> {
        override fun onSuccess() {
            showGraph()
            viewModel.getTops()
        }

        override fun onError(error: Error) {
        }

    }

    private val topObserver = object : ResourceObserver<List<Top>> {
        override fun onSuccess() {
            populateData()
            scheduleRefresh()
        }

        override fun onError(error: Error) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setViewModel()
        setBinding()
        configChart()
        viewModel.getIPC()
    }





    private fun setViewModel() {
        viewModel.ipc.observe(this, ipcObserver)
        viewModel.top.observe(this, topObserver)
    }


    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ipc)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.rvFalls.layoutManager = LinearLayoutManager(this)
        binding.rvRises.layoutManager = LinearLayoutManager(this)
        binding.rvVolume.layoutManager = LinearLayoutManager(this)
    }


    private fun configChart() {
        val blackColor = ContextCompat.getColor(this, R.color.black)
        val desc = Description()
        desc.text = "IPC"
        desc.textColor = blackColor
        binding.lineChart.description = desc

        val xAxis = binding.lineChart.xAxis
        xAxis.textColor = blackColor
        xAxis.valueFormatter = object : ValueFormatter() {
            private val mFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            override fun getFormattedValue(value: Float): String {
                val millis = value.toLong()
                return mFormat.format(Date(millis))
            }
        }
        val xFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return Utils.amountFormat(value)
            }
        }
        binding.lineChart.axisRight.apply {
            textColor = blackColor
            valueFormatter = xFormatter
        }
        binding.lineChart.axisLeft.apply {
            valueFormatter = xFormatter
            textColor = blackColor
        }

        binding.lineChart.legend.textColor = blackColor
    }

    private fun showGraph() {

        viewModel.setGraphEntries()
        val highLineDataSet = LineDataSet(
            viewModel.chartEntries,getString(R.string.price))
        highLineDataSet.apply {
            setDrawValues(false)
            lineWidth = 3f
            color = Color.GREEN
            setDrawCircles(false)
            valueTextColor = ContextCompat.getColor(this@IPCActivity, R.color.black)
        }

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(highLineDataSet)

        val lineData = LineData(dataSets)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()
    }


    private fun populateData() {
        binding.rvVolume.adapter = TopTenAdapter(this, viewModel.volumeList!!)
        binding.rvRises.adapter = TopTenAdapter(this, viewModel.riseList!!)
        binding.rvFalls.adapter = TopTenAdapter(this, viewModel.fallList!!)
    }

    private fun scheduleRefresh() {
        timer.schedule(timerTask {
            viewModel.getTops()
        }, TimeUnit.SECONDS.toMillis(20))
    }

}