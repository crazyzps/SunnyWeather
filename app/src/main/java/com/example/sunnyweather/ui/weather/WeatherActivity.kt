package com.example.sunnyweather.ui.weather

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        val decorView = window.decorView
        decorView.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this) { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.includeNow.tvPlaceName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily

        val currentTempText = "${realtime.temperature.toInt()}℃"
        binding.includeNow.tvCurrentTemp.text = currentTempText
        binding.includeNow.tvCurrentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.includeNow.tvCurrentAqi.text = currentPM25Text
        binding.includeNow.layoutNow.setBackgroundResource(getSky(realtime.skycon).bg)

        binding.includeForecast.layoutForecast.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(
                R.layout.forecast_item, binding.includeForecast.layoutForecast, false
            )
            val dataInfo = view.findViewById(R.id.tv_date_info) as TextView
            val skyIcon = view.findViewById(R.id.iv_sky_icon) as ImageView
            val skyInfo = view.findViewById(R.id.tv_sky_info) as TextView
            val temperatureInfo = view.findViewById(R.id.tv_temperature_info) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dataInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()}~${temperature.max.toInt()}"
            temperatureInfo.text = tempText
            binding.includeForecast.layoutForecast.addView(view)
        }

        val lifeIndex = daily.lifeIndex
        binding.includeLifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.includeLifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.includeLifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.includeLifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.layoutWeather.visibility = View.VISIBLE
    }
}