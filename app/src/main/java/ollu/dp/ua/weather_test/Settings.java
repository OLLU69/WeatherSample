package ollu.dp.ua.weather_test;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

/**
 * ----
 * Created by Лукащук Олег(master) on 07.03.18.
 */

class Settings {

    private static final String LAST_CITY_ID = "LAST_CITY_ID";
    private static final String LAST_CITY_NAME = "LAST_CITY_NAME";
    private static SharedPreferences main;

    static int getLastCityId() {
        return main.getInt(LAST_CITY_ID, 706483);
    }

    static void init(Context context) {
        main = context.getSharedPreferences("main", Context.MODE_PRIVATE);
    }

    static String getLastCityName() {
        return main.getString(LAST_CITY_NAME, "Харьков");
    }

    static void setLastCityId(int cityId) {
        main.edit().putInt(LAST_CITY_ID, cityId).apply();
    }

    static void setLastCityName(String cityName) {
        main.edit().putString(LAST_CITY_NAME, cityName).apply();
    }
}
