package ollu.dp.ua.weather

import android.support.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import ollu.dp.ua.weather.model.Model
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

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
        launch {
            val data = Model.instance.getWeatherData(706483)
            assertNotNull(data)
            assertNotNull(data?.main)
            assertNotNull(data?.weather?.get(0)?.description)
            assertNotNull(Model.getImageUrl(data))
            data?.let {
                val image = Model.instance.getRawImage(it)
                assertNotNull(image)
            }
            assertNotNull(data?.weather!![0].description)
        }.join()
    }

//    @Test
//    @Throws(Exception::class)
//    fun asyncWeatherTest() {
//        runBlocking {
//
//            Model.instance.getWeatherData(706483, OnResult { data: WeatherData? ->
//                assertNotNull(data)
//                assertNotNull(data?.main)
//                assertNotNull(Objects.requireNonNull<Array<WeatherData.Weather>>(data?.weather)[0].description)
//                assertNotNull(Model.getImageUrl(data))
//                data?.let {
//                    Model.instance.getRawImage(it,
//                            OnResult { responseBody ->
//                                Assert.assertNotNull(responseBody)
//                                notifyEnd()
//                            },
//                            OnFailure { _ ->
//                                fail()
//                                notifyEnd()
//                            })
//                }
//                assertNotNull(data?.weather!![0].description)
//            }, OnFailure { throwable ->
//                println("ERROR:" + throwable.message)
//                fail()
//                notifyEnd()
//            })
//        }
//        wait(60000)
//    }
}
