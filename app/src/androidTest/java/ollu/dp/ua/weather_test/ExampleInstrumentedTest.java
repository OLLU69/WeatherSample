package ollu.dp.ua.weather_test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ollu.dp.ua.weathertest", appContext.getPackageName());
    }

    @Test
    public void weatherTest() throws Exception {
        WeatherData data = Model.getInstance().getWeatherData(706483);
        assertNotNull(data);
        assertNotNull(data.main.temp);
        assertNotNull(data.main.temp_min);
        assertNotNull(data.main.temp_max);
        assertNotNull(data.weather[0].description);
        assertNotNull(Model.getImageUrl(data));
        assertNotNull(Model.getInstance().getRawImage(data));
        assertNotNull(data.weather[0].description);
        System.out.println("Test Ok");
    }


    @Test
    synchronized public void asyncWeatherTest() throws Exception {
        Model.getInstance().getWeatherData(706483, (WeatherData data) -> {
            assertNotNull(data);
            assertNotNull(data.main.temp);
            assertNotNull(data.main.temp_min);
            assertNotNull(data.main.temp_max);
            assertNotNull(data.weather[0].description);
            assertNotNull(Model.getImageUrl(data));
            Model.getInstance().getRawImage(data,
                    responseBody -> {
                        Assert.assertNotNull(responseBody);
                        notifyEnd();
                    },
                    t -> {
                        fail();
                        notifyEnd();
                    });
            assertNotNull(data.weather[0].description);
        }, throwable -> {
            System.out.println("ERROR:" + throwable.getMessage());
            fail();
            notifyEnd();
        });
        wait(60000);
    }

    synchronized private void notifyEnd() {
        notifyAll();
    }

    @Test
    public void degreeFormat() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        String temp = context.getString(R.string.degree_float_format, 3.01f);
        assertEquals("+3°", temp);
        System.out.println(temp);
        temp = context.getString(R.string.degree_float_format, 30.01f);
        assertEquals("+30°", temp);
        System.out.println(temp);
        temp = context.getString(R.string.degree_float_format, -3.01f);
        assertEquals("-3°", temp);
        System.out.println(temp);
        temp = context.getString(R.string.degree_float_format, -30.01f);
        assertEquals("-30°", temp);
        System.out.println(temp);
        temp = context.getString(R.string.degree_float_format, -300.01f);
        assertEquals("-300°", temp);
        System.out.println(temp);
    }
}
