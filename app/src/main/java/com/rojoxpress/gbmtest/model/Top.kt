package com.rojoxpress.gbmtest.model

data class Top (
    val issueId: String,
    val lastPrice: Float,
    val percentageChange: Float,
    val riseLowTypeId: Int) {

    companion object {
        const val RISE = 2
        const val FALL = 1
        const val VOLUME = 3
    }
}
