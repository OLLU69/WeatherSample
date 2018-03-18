package ollu.dp.ua.weather_test;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import ollu.dp.ua.weather_test.databinding.ActivityMainBinding;
import ollu.dp.ua.weather_test.event_bus.BusFactory;

public class MainActivity extends AppCompatActivity implements MainActivityMVVM.MVVMViewer {

    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final int REQUEST_CITY = 100;
    private ActivityMainBinding binding;

    @BindingAdapter("url")
    public static void imageUrlBinding(ImageView view, String url) {
        if (url == null) return;
        Uri uri = Uri.parse(url);
        Glide.with(view).load(uri).into(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null) {
            binding.setMvvm(new MainActivityMVVM(this));
        } else {
            binding.setMvvm(MainActivityMVVM.getInstance(this));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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
                binding.getMvvm().showCityWeather(data.getIntExtra(CITY_ID, 0), data.getStringExtra(CITY_NAME));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusFactory.getInstance().subscribe(this, e -> {
            System.out.println(e.getName());
            binding.getMvvm().loadData();
        }, WeatherApp.TIME_EVENT);
    }

    @Override
    protected void onPause() {
        BusFactory.getInstance().unsubscribe(this);
        super.onPause();
    }

    private void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_CITY);
    }

    @Override
    public void showMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.root_layout), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sync() {
        binding.invalidateAll();
    }

}
