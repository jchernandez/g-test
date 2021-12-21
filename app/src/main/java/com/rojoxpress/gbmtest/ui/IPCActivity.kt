package com.rojoxpress.gbmtest.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.accept
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import com.rojoxpress.gbmtest.databinding.ViewIpcBinding
import com.rojoxpress.gbmtest.databinding.ViewTopTenBinding
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


@AndroidEntryPoint
class IPCActivity : AppCompatActivity() {

    private val viewModel by viewModels<IPCViewModel>()
    private lateinit var binding: ActivityIpcBinding
    private lateinit var ipcView: ViewIpcBinding
    private lateinit var topTenView: ViewTopTenBinding
    private val timer = Timer()

    private val ipcObserver = object : ResourceObserver<List<IPC>> {
        override fun onSuccess() {
            showGraph()
            viewModel.getTops()
        }

        override fun onError(error: Error) {
            onIPCError()
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
        ipcView = binding.viewIpc
        topTenView = binding.viewTopTen
        binding.viewModel = viewModel
        topTenView.rvFalls.layoutManager = LinearLayoutManager(this)
        topTenView.rvRises.layoutManager = LinearLayoutManager(this)
        topTenView.rvVolume.layoutManager = LinearLayoutManager(this)
    }


    private fun configChart() {
        val blackColor = ContextCompat.getColor(this, R.color.black)
        val desc = Description()
        desc.text = "IPC"
        desc.textColor = blackColor
        ipcView.lineChart.description = desc

        val xAxis = ipcView.lineChart.xAxis
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
        ipcView.lineChart.axisRight.apply {
            textColor = blackColor
            valueFormatter = xFormatter
        }
        ipcView.lineChart.axisLeft.apply {
            valueFormatter = xFormatter
            textColor = blackColor
        }

        ipcView.lineChart.legend.textColor = blackColor
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
        ipcView.lineChart.data = lineData
        ipcView.lineChart.invalidate()
    }


    private fun populateData() {
        topTenView.rvVolume.adapter = TopTenAdapter(this, viewModel.volumeList!!)
        topTenView.rvRises.adapter = TopTenAdapter(this, viewModel.riseList!!)
        topTenView.rvFalls.adapter = TopTenAdapter(this, viewModel.fallList!!)
    }

    private fun scheduleRefresh() {
        timer.schedule(timerTask {
            viewModel.getTops()
        }, TimeUnit.SECONDS.toMillis(20))
    }

    fun onIPCError() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.error_title)
            setMessage(R.string.error_ipc_message)
            setPositiveButton(R.string.retry) { _, _ ->
                viewModel.getIPC()
            }
        }.show()
    }

}