package ollu.dp.ua.weather

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ollu.dp.ua.weather.model.Model
import ollu.dp.ua.weather.model.WeatherData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest : Object() {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("ollu.dp.ua.weather", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun weatherTest() {
        val data = Model.instance.getWeatherData(706483)
        assertNotNull(data)
        assertNotNull(data!!.main)
        assertNotNull(Objects.requireNonNull<Array<WeatherData.Weather>>(data.weather)[0].description)
        assertNotNull(Model.getImageUrl(data))
        assertNotNull(Model.instance.getRawImage(data))
        assertNotNull(data.weather!![0].description)
        println("Test Ok")
    }

    @Test
    fun degreeFormat() {
        val context = InstrumentationRegistry.getTargetContext()
        var temp = context.getString(R.string.degree_float_format, 3.01f)
        assertEquals("+3°", temp)
        println(temp)
        temp = context.getString(R.string.degree_float_format, 30.01f)
        assertEquals("+30°", temp)
        println(temp)
        temp = context.getString(R.string.degree_float_format, -3.01f)
        assertEquals("-3°", temp)
        println(temp)
        temp = context.getString(R.string.degree_float_format, -30.01f)
        assertEquals("-30°", temp)
        println(temp)
        temp = context.getString(R.string.degree_float_format, -300.01f)
        assertEquals("-300°", temp)
        println(temp)
    }
}
