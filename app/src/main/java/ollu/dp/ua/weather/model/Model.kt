package ollu.dp.ua.weather.model

import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import java.io.IOException

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

typealias OnResult<T> = Action1<T>

typealias OnFailure = Action1<Throwable>

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

    fun getWeatherData(cityId: Int, onResult: OnResult<WeatherData?>) {
        val func = {
            try {
                getWeatherData(cityId)
            } catch (e: IOException) {
                throw Error(e)
            }
        }
        runFunc1(func, onResult)

    }

    @Throws(IOException::class)
    fun getWeatherData(cityId: Int): WeatherData? {
        val dataCall = getWeatherII().getData(cityId)
        val response = dataCall.execute()
        return response.body()
    }

    private fun <T> runFunc1(func: () -> T, onResult: OnResult<T>) {
        launch(UI) {
            try {
                val data = async {
                    func()
                }.await()
                onResult.call(data)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult.call(null)
            }
        }
    }

    private fun <T> runFunc(func: () -> T, onResult: OnResult<T>, onFailure: OnFailure) {
        Observable
                .create<T> { subscriber -> subscriber.onNext(func()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onFailure)
    }

    private fun getWeatherII(): WeatherII {
        if (weatherII == null) {
            weatherII = getRetrofit().create(WeatherII::class.java)
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
        private var model: Model? = null
        private var retrofit: Retrofit? = null

        @JvmStatic
        val instance: Model
            get() {
                if (model == null) {
                    model = Model()
                }
                return model!!
            }

        @JvmStatic
        fun getImageUrl(data: WeatherData?): String? {
            return if (data?.weather?.get(0)?.icon != null) {
                "http://openweathermap.org/img/w/${data.weather?.get(0)?.icon ?: ""}.png"
            } else {
                null
            }
        }

        private fun getRetrofit(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl("http://api.openweathermap.org/data/2.5/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit!!
        }
    }
}
