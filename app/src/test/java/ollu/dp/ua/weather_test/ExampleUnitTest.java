package ollu.dp.ua.weather_test;

import org.junit.Test;

import ollu.dp.ua.weather_test.model.Model;
import ollu.dp.ua.weather_test.model.WeatherData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void weatherTest() throws Exception {
        WeatherData data = Model.getInstance().getWeatherData(706483);
        assertNotNull(data);
        assertNotNull(data.main);
        assertNotNull(data.weather[0].description);
        assertNotNull(Model.getImageUrl(data));
        assertNotNull(Model.getInstance().getRawImage(data));

        assertNotNull(data.weather[0].description);
    }
}