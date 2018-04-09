package ollu.dp.ua.weather_test;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

import ollu.dp.ua.weather_test.model.Model;
import ollu.dp.ua.weather_test.model.WeatherData;

/**
 * ----
 * Created by Лукащук Олег(master) on 18.03.18.
 */

public class MainActivityMVVM extends BaseObservable {
    private static MainActivityMVVM vModel;
    public String cityName;
    public float temp;
    public float tempMin;
    public float tempMax;
    public String icon;
    public String weatherDescr;
    public ObservableBoolean showProgress = new ObservableBoolean(false);
    public String url;
    private WeatherData data;
    private MVVMViewer viewer;

    MainActivityMVVM(MainActivityMVVM.MVVMViewer viewer) {
        this.viewer = viewer;
        loadData();
    }

    static MainActivityMVVM getInstance(MVVMViewer viewer) {
        if (vModel == null) {
            vModel = new MainActivityMVVM(viewer);
        } else {
            vModel.viewer = viewer;
        }
        return vModel;
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
        }, throwable -> {
            throwable.printStackTrace();
            viewer.showMessage("Нет соединения с интернетом");
            showProgress.set(false);
        });
    }

    private void bindData(WeatherData data) {
        if (data == null) return;
        cityName = data.localName;
        temp = data.main.temp;
        tempMin = data.main.temp_min;
        tempMax = data.main.temp_max;
        weatherDescr = String.valueOf(data.weather[0].description);
        url = Model.getImageUrl(data);
        notifyChange();
    }


    public interface MVVMViewer {
        @SuppressWarnings("SameParameterValue")
        void showMessage(String message);
    }
}
