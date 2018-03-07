package ollu.dp.ua.weather_test;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import ollu.dp.ua.weather_test.event_bus.BusFactory;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final int REQUEST_CITY = 100;
    public static final String DATA_KEY = "data";
    @BindView(R.id.city_name)
    TextView cityName;
    @BindView(R.id.temp)
    TextView temp;
    @BindView(R.id.temp_min)
    TextView tempMin;
    @BindView(R.id.temp_max)
    TextView tempMax;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.weather_descr)
    TextView weatherDescr;
    @BindView(R.id.load_bar)
    FrameLayout loadBar;
    private WeatherData data = new WeatherData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            navigateToSettings();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CITY) {
                showCityWeather(data.getIntExtra(CITY_ID, 0), data.getStringExtra(CITY_NAME));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusFactory.getInstance().subscribe(this, e -> {
            System.out.println(e.getName());
            loadData();
        }, WeatherApp.TIME_EVENT);
    }

    @Override
    protected void onPause() {
        BusFactory.getInstance().unsubscribe(this);
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATA_KEY, new Gson().toJson(data));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String dd = savedInstanceState.getString(DATA_KEY);
        data = new Gson().fromJson(dd, WeatherData.class);
        bindData(data);
    }

    private void loadData() {
        showCityWeather(Settings.getLastCityId(), Settings.getLastCityName());
    }

    private void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_CITY);
    }

    private void showCityWeather(int cityId, String cityName) {
        Settings.setLastCityId(cityId);
        Settings.setLastCityName(cityName);
        Model model = Model.getInstance();
        showProgress(true);
        model.getWeatherData(cityId, (_data) -> {
            _data.localName = cityName;
            data = _data;
            bindData(data);
            showProgress(false);
        }, throwable -> {
            throwable.printStackTrace();
            Toast.makeText(MainActivity.this, "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
            showProgress(false);
        });
    }

    private void bindData(WeatherData data) {
        cityName.setText(data.localName);
        temp.setText(getString(R.string.degree_float_format, data.main.temp));
        tempMin.setText(getString(R.string.degree_float_format, data.main.temp_min));
        tempMax.setText(getString(R.string.degree_float_format, data.main.temp_max));
        weatherDescr.setText(String.valueOf(data.weather[0].description));
        Uri uri = Model.getImageUri(data);
        Glide.with(this).load(uri).into(icon);
    }

    private void showProgress(boolean show) {
        loadBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
