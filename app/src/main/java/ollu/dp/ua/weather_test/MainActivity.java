package ollu.dp.ua.weather_test;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ollu.dp.ua.weather_test.databinding.ActivityMainBinding;
import ollu.dp.ua.weather_test.event_bus.BusFactory;

public class MainActivity extends AppCompatActivity {

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final int REQUEST_CITY = 100;
    private ActivityMainBinding binding;
    private Observable.OnPropertyChangedCallback onMessage = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            //noinspection unchecked
            showMessage(((ObservableField<String>) sender).get());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityVM vm = ViewModelProviders.of(this).get(MainActivityVM.class);
        binding.setVm(vm);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                //noinspection ConstantConditions
                binding.getVm().showCityWeather(data.getIntExtra(CITY_ID, 0), data.getStringExtra(CITY_NAME));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //noinspection ConstantConditions
        binding.getVm().getShowMessage().addOnPropertyChangedCallback(onMessage);
        BusFactory.getInstance().subscribe(this, e -> {
            System.out.println(e.getName());
            binding.getVm().loadData();
        }, WeatherApp.TIME_EVENT);
    }

    @Override
    protected void onPause() {
        BusFactory.getInstance().unsubscribe(this);
        //noinspection ConstantConditions
        binding.getVm().getShowMessage().removeOnPropertyChangedCallback(onMessage);
        super.onPause();
    }

    private void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_CITY);
    }

    public void showMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.root_layout), message, Toast.LENGTH_SHORT).show();
    }
}
