package com.example.sunnyweather.logic.model

/**
@date:2022/3/21
@time:11:03 下午
@author:zhaops
@desc:
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
