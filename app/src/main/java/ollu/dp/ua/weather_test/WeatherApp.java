package ollu.dp.ua.weather_test;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ----
 * Created by Лукащук Олег(master) on 07.03.18.
 */

public class WeatherApp extends Application {
    private static final String WEATHER_TIMER = "weatherTimer";
    private static final MutableLiveData<Boolean> timeEvent = new MutableLiveData<>();
    private static final long HOUR = 1000 * 60 * 60;
    private Timer timer;

    public static LiveData<Boolean> getTimeEvent() {
        return timeEvent;
    }

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
                timeEvent.postValue(true);
            }
        }, HOUR, HOUR);
    }
}
