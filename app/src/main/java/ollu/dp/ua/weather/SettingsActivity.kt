package ollu.dp.ua.weather

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.ListView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        val cityList = findViewById<ListView>(R.id.city_list)
        val data = arrayOf(
                City(709930, "Днепр"),
                City(687700, "Запорожье"),
                City(703448, "Киев"),
                City(706483, "Харьков"))
        cityList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        cityList.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra(MainActivity.CITY_ID, data[position].id)
            intent.putExtra(MainActivity.CITY_NAME, data[position].name)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private class City(var id: Int, var name: String) {

        override fun toString(): String {
            return name
        }
    }
}
