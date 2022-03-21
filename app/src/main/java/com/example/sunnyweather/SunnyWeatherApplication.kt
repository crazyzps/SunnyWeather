package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
@date:2022/3/21
@time:2:25 下午
@author:zhaops
@desc:
 */
class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "sOt9foYSFCOjB8Xs"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}