package ollu.dp.ua.weather;

import android.arch.lifecycle.ViewModel;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import ollu.dp.ua.weather.model.Model;
import ollu.dp.ua.weather.model.WeatherData;

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

//MainActivityVM - view model
public class MainActivityVM extends ViewModel {
    public String icon;
    public ObservableBoolean showProgress = new ObservableBoolean(false);
    public BindData bindData = new BindData();
    private WeatherData data;
    private ObservableField<String> showMessage = new ObservableField<String>() {
        private String mValue;

        @Override
        public void set(String value) {
            mValue = value;
            notifyChange();
        }

        @Nullable
        @Override
        public String get() {
            return mValue;
        }
    };

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
            showMessage.set("Данные получены!");
        }, throwable -> {
            throwable.printStackTrace();
            showMessage.set("Нет соединения с интернетом");
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

    public ObservableField<String> getShowMessage() {
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
