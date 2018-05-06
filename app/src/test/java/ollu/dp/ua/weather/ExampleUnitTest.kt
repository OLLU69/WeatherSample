package ollu.dp.ua.weather

import ollu.dp.ua.weather.model.Model
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    @Throws(Exception::class)
    fun weatherTest() {
        val data = Model.instance.getWeatherData(706483)
        assertNotNull(data)
        assertNotNull(data?.main)
        assertNotNull(data?.weather?.get(0)?.description)
        assertNotNull(Model.getImageUrl(data))
        assertNotNull(data?.let { Model.instance.getRawImage(it) })

        assertNotNull(data?.weather!![0].description)
    }
}