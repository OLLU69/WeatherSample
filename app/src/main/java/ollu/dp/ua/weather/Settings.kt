package ollu.dp.ua.weather

import android.content.Context
import android.content.SharedPreferences

/**
 * ----
 * Created by Лукащук Олег(master) on 07.03.18.
 */

internal object Settings {

    private const val LAST_CITY_ID = "LAST_CITY_ID"
    private const val LAST_CITY_NAME = "LAST_CITY_NAME"
    private var preferences: SharedPreferences? = null

    var lastCityId: Int
        get() = preferences!!.getInt(LAST_CITY_ID, 706483)
        set(cityId) = preferences!!.edit().putInt(LAST_CITY_ID, cityId).apply()

    var lastCityName: String
        get() = preferences?.getString(LAST_CITY_NAME, "Харьков")?:"Не известно"
        set(cityName) = preferences!!.edit().putString(LAST_CITY_NAME, cityName).apply()

    fun init(context: Context) {
        preferences = context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }
}
