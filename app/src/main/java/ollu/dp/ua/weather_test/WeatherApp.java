package ollu.dp.ua.weather_test;

import android.app.Application;

import java.util.Timer;
import java.util.TimerTask;

import ollu.dp.ua.weather_test.event_bus.BusFactory;
import ollu.dp.ua.weather_test.event_bus.Event;

/**
 * ----
 * Created by Лукащук Олег(master) on 07.03.18.
 */

public class WeatherApp extends Application {
    public static final String WEATHER_TIMER = "weatherTimer";
    public static final String TIME_EVENT = "timeEvent";
    private static final long HOUR = 1000 * 60 * 60;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.init(this);
        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer(WEATHER_TIMER);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Event event = new Event(TIME_EVENT);
                BusFactory.getInstance().send(event);
            }
        }, HOUR, HOUR);
    }
}
