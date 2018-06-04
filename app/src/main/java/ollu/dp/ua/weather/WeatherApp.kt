package ollu.dp.ua.weather

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import java.util.*

/**
 * ----
 * Created by Лукащук Олег(master) on 07.03.18.
 */

class WeatherApp : Application() {
    private var timer: Timer? = null

    override fun onCreate() {
        super.onCreate()
        Settings.init(this)
        startTimer()
    }

    private fun startTimer() {
        if (timer != null) timer!!.cancel()
        timer = Timer(WEATHER_TIMER)
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                timeEvent.postValue(true)
            }
        }, HOUR, HOUR)
    }

    companion object {
        const val WEATHER_TIMER = "weatherTimer"
        //        private const val HOUR = (1000 * 10).toLong()
        private const val HOUR = (1000 * 60 * 60).toLong()
        val timeEvent = MutableLiveData<Boolean>()
    }
}
