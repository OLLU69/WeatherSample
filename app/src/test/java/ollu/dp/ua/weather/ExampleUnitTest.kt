package ollu.dp.ua.weather

import kotlinx.coroutines.*
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
        val data = Model.model.getWeatherData(706483)
        assertNotNull(data)
        assertNotNull(data?.main)
        assertNotNull(data?.weather?.get(0)?.description)
        assertNotNull(Model.getImageUrl(data))
        assertNotNull(data?.let { Model.model.getRawImage(it) })

        assertNotNull(data?.weather!![0].description)
    }

    @Test
    @Throws(Exception::class)
    fun asyncTest() {
        println("before runBlocking")
        runBlocking(Dispatchers.Default) {
            println("before delay")
            delay(5000)
            println("after delay")
        }
        println("after runBlocking")
    }

    @Test
    fun exceptionTest() {
        println("before runBlocking")
        val i = runBlocking(Dispatchers.Default) {
            println("before delay")
            delay(5000)
            println("after delay")
            suspend {
                val asyncResult = async {
                    @Suppress("ConstantConditionIf")
                    if (true) {
                        throw Exception("Test Exception")
                    }
                    100
                }
                try {

                    val result = asyncResult.await()
                    println(result)
                } catch (e: Exception) {
                    println(e.message)
                }
            }()
            100
        }
        println("after runBlocking result $i")
    }

    @Test
    fun coroutineTest() {
        runBlocking {
            val list = (1..1_000_000).map {
                async {
                    delay(1000)
                    it
                }
            }
            delay(1000)
            val sum = list.sumBy { it.await() }
            println("Sum: $sum")
            assertEquals(1784293664, sum)
        }
    }
}