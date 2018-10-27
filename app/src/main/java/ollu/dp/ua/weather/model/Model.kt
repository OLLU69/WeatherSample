package ollu.dp.ua.weather.model

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

typealias OnResult<T> = (T) -> Unit

typealias OnFailure = (t: Throwable) -> Unit

class Model {
    private var weatherII: WeatherII? = null
    fun getWeatherData(cityId: Int, onResult: OnResult<WeatherData?>, onFailure: OnFailure) {
        val func = {
            try {
                getWeatherData(cityId)
            } catch (e: IOException) {
                throw Error(e)
            }
        }
        runFunc(func, onResult, onFailure)

    }

    @Throws(IOException::class)
    fun getWeatherData(cityId: Int): WeatherData? {
        val dataCall = getWeatherII().getData(cityId)
        val response = dataCall.execute()
        return response.body()
    }

    private fun <T> runFunc(func: () -> T, onResult: OnResult<T>, onFailure: OnFailure) {
        launch(UI) {
            try {
                val data = withContext(DefaultDispatcher) {
                    func()
                }
                onResult(data)
            } catch (e: Throwable) {
                e.printStackTrace()
                onFailure(e)
            }
        }
    }

    private fun getWeatherII(): WeatherII {
        if (weatherII == null) {
            weatherII = retrofit.create(WeatherII::class.java)
        }
        return weatherII!!
    }

    fun getRawImage(data: WeatherData, onResult: OnResult<ResponseBody?>, onFailure: OnFailure) {
        val func = { getRawImage(data) }
        runFunc(func, onResult, onFailure)
    }

    fun getRawImage(data: WeatherData): ResponseBody? {
        val responseBodyCall = data.weather?.get(0)?.icon?.let { getWeatherII().getRawImage(it) }
        try {
            val response = responseBodyCall?.execute()
            return response?.body()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    internal interface WeatherII {

        @GET("weather$KEY_SETTINGS")
        fun getData(@Query("id") cityId: Int): Call<WeatherData>

        @GET("http://openweathermap.org/img/w/{icon}.png")
        fun getRawImage(@Path("icon") icon: String): Call<ResponseBody>

        companion object {

            const val KEY_SETTINGS = "?lang=ru&appid=7e6a0418b29d52e644692a16127e87b7&units=metric"
        }
    }

    companion object {
        @JvmField
        val model: Model = Model()
        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        @JvmStatic
        fun getImageUrl(data: WeatherData?): String? {
            return "http://openweathermap.org/img/w/${data?.weather?.get(0)?.icon
                    ?: return null}.png"
        }
    }
}
