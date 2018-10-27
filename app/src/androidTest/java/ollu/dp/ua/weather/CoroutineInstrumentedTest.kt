package ollu.dp.ua.weather

import android.support.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import ollu.dp.ua.weather.model.Model
import ollu.dp.ua.weather.model.WeatherData
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

private const val CITY_ID = 706483

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
@RunWith(AndroidJUnit4::class)
class CoroutineInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun syncWeatherTest() = runBlocking {
        var data: WeatherData? = null
        launch {
            data = Model.model.getWeatherData(CITY_ID)
        }.join()
        testData(data)
    }

    private fun testData(data: WeatherData?) {
        assertNotNull(data)
        assertNotNull(data?.main)
        assertNotNull(data?.weather?.get(0)?.description)
        assertNotNull(Model.getImageUrl(data))
        runBlocking(CommonPool) {
            data?.let {
                val image = Model.model.getRawImage(it)
                assertNotNull(image)
            }
        }
    }

    @Test
    fun asyncCoroutineWeatherTest() {
        var passed = false
        runBlocking(UI) {
            Model.model.getWeatherData(CITY_ID, { weatherData ->
                testData(weatherData)
                passed = true
                println("Данные получены!")
            }
                    , {
                println("Ошибка получения  данных!")
                fail()
            })
            delay(10000)
            println("Данные должны быть получены!")
        }
        assertTrue(passed)
        println("runBlocking ok")
    }

    @Test
    fun asyncWeatherTest() {
        var passed = false
        runTestTimer()
        runBlocking(UI) {
            val model = Model.model

            suspend {
                val asyncData = async(CommonPool) {
                    delay(1000)
                    model.getWeatherData(CITY_ID)
                }
                val weatherData = asyncData.await()
                weatherData?.localName = "Город"
                testData(weatherData)
                stopTestTimer()
                println("Данные получены!")
                passed = true
            }()

            println("launch ok")
        }
        println("runBlocking ok")
        assertTrue(passed)
    }

    private fun stopTestTimer() {
        task?.cancel()
        task = null
    }

    private val testTimer = Timer()
    private var task: TimerTask? = null

    private fun runTestTimer() {
        task = object : TimerTask() {
            private var count = 0

            override fun run() {
                println(count)
                count++
            }

        }
        testTimer.schedule(task, 1000, 1000)
    }

    @Test
    @Throws(Exception::class)
    fun asyncTest() {
        println("before runBlocking")
        runBlocking {
            println("before delay")
            delay(1000)
            println("after delay")
        }
        println("after runBlocking")
    }
}
