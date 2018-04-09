package ollu.dp.ua.weather_test.model;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

public class Model {
    private static Model model;
    private static Retrofit retrofit;
    private WeatherII weatherII;

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public static String getImageUrl(WeatherData data) {
        return "http://openweathermap.org/img/w/" + data.weather[0].icon + ".png";
    }

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public void getWeatherData(int cityId, OnResult<WeatherData> onResult, OnFailure onFailure) {

        Func0<WeatherData> func = () -> {
            try {
                return getWeatherData(cityId);
            } catch (IOException e) {
                throw new Error(e);
            }
        };
        runFunc(func, onResult, onFailure);

    }

    @Nullable
    public WeatherData getWeatherData(int cityId) throws IOException {
        Call<WeatherData> dataCall = getWeatherII().getData(cityId);
        Response<WeatherData> response = dataCall.execute();
        return response.body();
    }

    private <T> void runFunc(Func0<T> func, OnResult<T> onResult, OnFailure onFailure) {
        Observable
                .<T>create(subscriber -> subscriber.onNext(func.call()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onFailure);
    }

    private WeatherII getWeatherII() {
        if (weatherII == null) {
            weatherII = getRetrofit().create(WeatherII.class);
        }
        return weatherII;
    }

    public void getRawImage(WeatherData data, OnResult<ResponseBody> onResult, OnFailure onFailure) {
        Func0<ResponseBody> func = () -> getRawImage(data);
        runFunc(func, onResult, onFailure);
    }

    public ResponseBody getRawImage(WeatherData data) {
        Call<ResponseBody> responseBodyCall = getWeatherII().getRawImage(data.weather[0].icon);
        try {
            Response<ResponseBody> response = responseBodyCall.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    interface WeatherII {

        String KEY_SETTINGS = "?lang=ru&appid=7e6a0418b29d52e644692a16127e87b7&units=metric";

        @GET("weather" + KEY_SETTINGS)
        Call<WeatherData> getData(@Query("id") int cityId);

        @GET("http://openweathermap.org/img/w/{icon}.png")
        Call<ResponseBody> getRawImage(@Path("icon") String icon);
    }

    public interface OnResult<T> extends Action1<T> {
    }

    public interface OnFailure extends Action1<Throwable> {
    }
}
