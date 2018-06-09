package ollu.dp.ua.weather_test;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

import ollu.dp.ua.weather_test.model.Model;
import ollu.dp.ua.weather_test.model.WeatherData;

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

//MainActivityVM - view model
public class MainActivityVM extends ViewModel {
    private final MutableLiveData<String> showMessage = new MutableLiveData<>();
    public String icon;
    public ObservableBoolean showProgress = new ObservableBoolean(false);
    public BindData bindData = new BindData();
    private WeatherData data;

    public MainActivityVM() {
        loadData();
    }

    void loadData() {
        showCityWeather(Settings.getLastCityId(), Settings.getLastCityName());
    }

    void showCityWeather(int cityId, String cityName) {
        Settings.setLastCityId(cityId);
        Settings.setLastCityName(cityName);
        showProgress.set(true);
        Model model = Model.getInstance();
        model.getWeatherData(cityId, (_data) -> {
            _data.localName = cityName;
            data = _data;
            bindData(data);
            showProgress.set(false);
            showMessage.setValue("Данные получены!");
        }, throwable -> {
            throwable.printStackTrace();
            showMessage.setValue("Нет соединения с интернетом");
            showProgress.set(false);
        });
    }

    private void bindData(WeatherData data) {
        if (data == null) return;
        bindData.cityName = data.localName;
        bindData.temp = data.main.temp;
        bindData.tempMin = data.main.temp_min;
        bindData.tempMax = data.main.temp_max;
        bindData.weatherDescr = String.valueOf(data.weather[0].description);
        bindData.url = Model.getImageUrl(data);
        bindData.notifyChange();
    }

    public LiveData<String> getShowMessage() {
        return showMessage;
    }

    public class BindData extends BaseObservable {
        public String cityName;
        public String weatherDescr;
        public float temp;
        public float tempMin;
        public float tempMax;
        public String url;
    }
}
