package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

/**
@date:2022/3/21
@time:10:51 下午
@author:zhaops
@desc:
 */
data class DailyResponse(val status: String, val result: Result) {
    data class Result(val daily: Daily)

    data class Daily(
        val temperature: List<Temperature>,
        val skycon: List<Skycon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class LifeIndex(
        val coldRisk: List<LifeDescription>,
        val carWashing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)

    data class Skycon(val value: String, val date: Date)

    data class Temperature(val max: Float, val min: Float)


}
