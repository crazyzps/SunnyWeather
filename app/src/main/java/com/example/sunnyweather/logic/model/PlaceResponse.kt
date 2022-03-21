package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName


/**
@date:2022/3/21
@time:2:37 下午
@author:zhaops
@desc:
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String, val location: Location,
    @SerializedName("formatted_address") val address: String
)

data class Location(val lng: String, val lat: String)



