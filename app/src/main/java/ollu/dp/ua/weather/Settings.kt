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
    private var main: SharedPreferences? = null

    var lastCityId: Int
        get() = main!!.getInt(LAST_CITY_ID, 706483)
        set(cityId) = main!!.edit().putInt(LAST_CITY_ID, cityId).apply()

    var lastCityName: String
        get() = main!!.getString(LAST_CITY_NAME, "Харьков")
        set(cityName) = main!!.edit().putString(LAST_CITY_NAME, cityName).apply()

    fun init(context: Context) {
        main = context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }
}
