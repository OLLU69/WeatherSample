package ollu.dp.ua.weather

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import ollu.dp.ua.weather.model.*
import org.junit.Assert
import org.junit.Assert.*
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
    @Synchronized
    @Throws(Exception::class)
    fun asyncWeatherTest() {
        Model.instance.getWeatherData(706483, OnResult { data: WeatherData? ->
            assertNotNull(data)
            assertNotNull(data?.main)
            assertNotNull(Objects.requireNonNull<Array<WeatherData.Weather>>(data?.weather)[0].description)
            assertNotNull(Model.getImageUrl(data))
            data?.let {
                Model.instance.getRawImage(it,
                        OnResult { responseBody ->
                            Assert.assertNotNull(responseBody)
                            notifyEnd()
                        },
                        OnFailure { _ ->
                            fail()
                            notifyEnd()
                        })
            }
            assertNotNull(data?.weather!![0].description)
        }, OnFailure { throwable ->
            println("ERROR:" + throwable.message)
            fail()
            notifyEnd()
        })
        wait(60000)
    }

    @Synchronized
    private fun notifyEnd() {
        notifyAll()
    }

    private val lock = Object()
    @Test
    @Throws(Exception::class)
    fun asyncWeatherTest2() {
        Model.instance.getWeatherData(706483, OnResult { data: WeatherData? ->
            assertNotNull(data)
            assertNotNull(data?.main)
            assertNotNull(data?.weather?.get(0)?.description)
            assertNotNull(Model.getImageUrl(data))
            data?.let {
                Model.instance.getRawImage(it,
                        OnResult { responseBody ->
                            Assert.assertNotNull(responseBody)
                            notifyEnd2()
                        },
                        OnFailure { _ ->
                            fail()
                            notifyEnd2()
                        })
            }
            assertNotNull(data?.weather!![0].description)
        }, OnFailure { throwable ->
            println("ERROR:" + throwable.message)
            fail()
            notifyEnd2()
        })
        synchronized(lock) {
            lock.wait(60000)
        }
    }

    private fun notifyEnd2() {
        synchronized(lock) {
            lock.notifyAll()
        }
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
