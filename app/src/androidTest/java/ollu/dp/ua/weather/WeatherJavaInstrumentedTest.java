package ollu.dp.ua.weather;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import ollu.dp.ua.weather.model.Model;
import ollu.dp.ua.weather.model.WeatherData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WeatherJavaInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("ollu.dp.ua.weather", appContext.getPackageName());
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
        System.out.println("Test Ok");
    }


    @Test
    synchronized public void asyncWeatherTest() throws Exception {
        Model.getInstance().getWeatherData(706483, (WeatherData data) -> {
            assertNotNull(data);
            assertNotNull(data.main);
            if (data.weather == null) {
                fail();
            } else {
                assertNotNull(data.weather[0].description);
            }
            assertNotNull(Model.getImageUrl(data));
            Model.getInstance().getRawImage(data,
                    responseBody -> {
                        Assert.assertNotNull(responseBody);
                        notifyEnd();
                        return null;
                    },
                    t -> {
                        fail();
                        return null;
                    }
            );
            return null;
        }, t -> {
            fail();
            return null;
        });
        wait(60000);
    }

    synchronized private void notifyEnd() {
        notifyAll();
    }

    @Test
    public void degreeFormat() {
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
