package ollu.dp.ua.weather.event_bus;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Класс на основе JSONObject, дополненный возможностью хранить объект типа Date.
 */

@SuppressWarnings("unused")
public class Params extends JSONObject {

    @Override
    public JSONObject put(@NonNull String name, Object value) {
        try {
            super.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public JSONObject put(@NonNull String name, int value) {
        return put(name, (Object)value);
    }

    @Override
    public JSONObject put(@NonNull String name, long value) {
        return put(name, (Object)value);
    }

    @Override
    public JSONObject put(@NonNull String name, double value) {
        return put(name, (Object)value);
    }

    @Override
    public JSONObject put(@NonNull String name, boolean value) {
        return put(name, (Object)value);
    }

    public JSONObject put(@NonNull String name, Date value) {
        return put(name, Long.valueOf(value.getTime()));
    }

    @SuppressWarnings("SameParameterValue")
    private String getString(@NonNull String name, String defaultValue) {
        try {
            return super.getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public String getString(@NonNull String name) {
        return getString(name, "");
    }

    public long getLong(@NonNull String name, long defaultValue) {
        try {
            return super.getLong(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public int getInt(@NonNull String name, int defaultValue) {
        try {
            return super.getInt(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public boolean getBoolean(@NonNull String name, boolean defaultValue) {
        try {
            return super.getBoolean(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public double getDouble(@NonNull String name, double defaultValue) {
        try {
            return super.getDouble(name);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public Date getDate(@NonNull String name, Date defaultValue) {
        try {
            return new Date(getLong(name));
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


}
