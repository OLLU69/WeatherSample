package ollu.dp.ua.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ListView cityList = findViewById(R.id.city_list);
        City[] data = {
                new City(709930, "Днепр"),
                new City(687700, "Запорожье"),
                new City(703448, "Киев"),
                new City(706483, "Харьков"),
        };
        cityList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));
        cityList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra(MainActivity.CITY_ID, data[position].id);
            intent.putExtra(MainActivity.CITY_NAME, data[position].name);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private class City {
        int id;
        String name;

        private City(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
