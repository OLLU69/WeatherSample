@file:Suppress("PackageName")

package ollu.dp.ua.weather.event_bus

import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Класс на основе JSONObject, дополненный возможностью хранить объект типа Date.
 */

@Suppress("unused")
open class Params : JSONObject() {

    override fun put(name: String, value: Any): JSONObject {
        try {
            super.put(name, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return this
    }

    override fun put(name: String, value: Int): JSONObject {
        return put(name, value as Any)
    }

    override fun put(name: String, value: Long): JSONObject {
        return put(name, value as Any)
    }

    override fun put(name: String, value: Double): JSONObject {
        return put(name, value as Any)
    }

    override fun put(name: String, value: Boolean): JSONObject {
        return put(name, value as Any)
    }

    fun put(name: String, value: Date): JSONObject {
        return put(name, java.lang.Long.valueOf(value.time))
    }

    private fun getString(name: String, defaultValue: String): String {
        return try {
            super.getString(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            defaultValue
        }

    }

    override fun getString(name: String): String {
        return getString(name, "")
    }

    fun getLong(name: String, defaultValue: Long): Long {
        return try {
            super.getLong(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            defaultValue
        }

    }

    fun getInt(name: String, defaultValue: Int): Int {
        return try {
            super.getInt(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            defaultValue
        }

    }

    fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        return try {
            super.getBoolean(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            defaultValue
        }

    }

    fun getDouble(name: String, defaultValue: Double): Double {
        return try {
            super.getDouble(name)
        } catch (e: JSONException) {
            e.printStackTrace()
            defaultValue
        }

    }

    fun getDate(name: String, defaultValue: Date): Date {
        return try {
            Date(getLong(name))
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }

    }


}
