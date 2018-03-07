package ollu.dp.ua.weather_test;

import android.net.Uri;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

class Model {
    private static Model model;
    private static Retrofit retrofit;
    private WeatherII weatherII;

    static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    static Uri getImageUri(WeatherData data) {
        return Uri.parse("http://openweathermap.org/img/w/" + data.weather[0].icon + ".png");
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

    void getWeatherData(int cityId, Action1<WeatherData> onResult, Action1<Throwable> onFailure) {

        Func1<Object, WeatherData> func = o -> {
            try {
                return getWeatherData(cityId);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        };
        runFunc(func, onResult, onFailure);

    }

    @Nullable
    WeatherData getWeatherData(int cityId) throws IOException {
        Call<WeatherData> dataCall = getWeatherII().getData(cityId);
        Response<WeatherData> response = dataCall.execute();
        return response.body();
    }

    // Создает новый источник для RX цепочки ассинхронных вызовов (по умолчанию на отдельном планировщике потоков)
    private Observable<Object> getObservable() {
        return Observable.just(null)
                .subscribeOn(Schedulers.io());
    }


    private <T> void runFunc(Func1<Object, T> func, Action1<T> onResult, Action1<Throwable> onFailure) {
        getObservable()
                .map(func)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onResult, onFailure);
    }

    private WeatherII getWeatherII() {
        if (weatherII == null) {
            weatherII = getRetrofit().create(WeatherII.class);
        }
        return weatherII;
    }

    ResponseBody getRawImage(WeatherData data) {
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
}
