package ollu.dp.ua.weather

import android.app.Application
import ollu.dp.ua.weather.event_bus.BusFactory
import ollu.dp.ua.weather.event_bus.Event
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
                val event = Event(TIME_EVENT)
                BusFactory.instance.send(event)
            }
        }, HOUR, HOUR)
    }

    companion object {
        const val WEATHER_TIMER = "weatherTimer"
        const val TIME_EVENT = "timeEvent"
        private const val HOUR = (1000 * 60 * 60).toLong()
    }
}
