package com.rojoxpress.gbmtest.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Utils {

    fun amountFormat(value: Float): String  {

        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 2

        return format.format(value)
    }


    fun percentFormat(value: Float): String {
        val format = NumberFormat.getPercentInstance()
        format.maximumFractionDigits = 2
        return format.format(value)
    }
}