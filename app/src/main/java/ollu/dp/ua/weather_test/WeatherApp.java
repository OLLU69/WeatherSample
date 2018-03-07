package ollu.dp.ua.weather_test;

import android.app.Application;

import java.util.Calendar;
import java.util.Date;
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
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.init(this);
        timer = new Timer(WEATHER_TIMER);
        startTimer();
    }

    private void startTimer() {
        timer.purge();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Event event = new Event(TIME_EVENT);
                BusFactory.getInstance().send(event);
                startTimer();
            }
        }, getNextDate());
    }

    private Date getNextDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        return calendar.getTime();
    }

    @Override
    public void onTerminate() {
        timer.cancel();
        timer.purge();
        timer = null;
        super.onTerminate();
    }
}
