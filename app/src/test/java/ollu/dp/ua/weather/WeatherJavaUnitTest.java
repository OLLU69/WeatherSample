package ollu.dp.ua.weather;

import org.junit.Test;

import ollu.dp.ua.weather.model.Model;
import ollu.dp.ua.weather.model.WeatherData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class WeatherJavaUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void weatherTest() throws Exception {
        WeatherData data = Model.getInstance().getWeatherData(706483);
        assertNotNull(data);
        assertNotNull(data.main);
        if (data.weather == null) {
            fail();
        } else {
            assertNotNull(data.weather[0].description);
        }
        assertNotNull(Model.getImageUrl(data));
        assertNotNull(Model.getInstance().getRawImage(data));
    }
}