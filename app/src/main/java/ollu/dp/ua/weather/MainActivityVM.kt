package ollu.dp.ua.weather

import android.arch.lifecycle.ViewModel
import android.databinding.*
import ollu.dp.ua.weather.model.*

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

//MainActivityVM - view model
class MainActivityVM : ViewModel() {
    var icon: String? = null
    var showProgress = ObservableBoolean(false)
    var bindData = BindData()
    val showMessage: ObservableField<String> = object : ObservableField<String>() {
        private var mValue: String? = null

        override fun set(value: String) {
            mValue = value
            notifyChange()
        }

        override fun get(): String? {
            return mValue
        }
    }

    init {
        loadData()
    }

    fun loadData() {
        showCityWeather(Settings.lastCityId, Settings.lastCityName)
    }

    internal fun showCityWeather(cityId: Int, cityName: String) {
        Settings.lastCityId = cityId
        Settings.lastCityName = cityName
        showProgress.set(true)
        Model.instance.getWeatherData(cityId, OnResult { weatherData: WeatherData? ->
            weatherData?.localName = cityName
            bindData(weatherData)
            showMessage.set(if (weatherData == null) {
                "Ошибка получения данных"
            } else {
                "Данные получены!"
            })
            showProgress.set(false)
        }, OnFailure {
            showMessage.set("Ошибка получения данных")
            showProgress.set(false)
        })

    }

    private fun bindData(data: WeatherData?) {
        bindData.cityName = data?.localName ?: "-----"
        bindData.temp = data?.main?.temp ?: 0.toFloat()
        bindData.tempMin = data?.main?.temp_min ?: 0.0f
        bindData.tempMax = data?.main?.temp_max ?: 0.0f
        bindData.weatherDescr = data?.weather?.get(0)?.description ?: "-----"
        bindData.url = Model.getImageUrl(data)
        bindData.notifyChange()
    }

    class BindData : BaseObservable() {
        var cityName: String? = ""
        var weatherDescr: String? = ""
        var temp: Float = 0.toFloat()
        var tempMin: Float = 0.toFloat()
        var tempMax: Float = 0.toFloat()
        var url: String? = null
    }
}
