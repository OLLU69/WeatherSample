package ollu.dp.ua.weather

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import ollu.dp.ua.weather.WeatherApp.Companion.timeEvent
import ollu.dp.ua.weather.databinding.ActivityMainBinding

/**
 * ----
 * Created by Лукащук Олег(master) on 27.04.18.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm?.showMessage?.observe(this, Observer(this@MainActivity::showMessage))
        timeEvent.observe(this, Observer {
            binding.vm?.loadData()

        })
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        binding.vm = ViewModelProviders.of(this).get(MainActivityVM::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            navigateToSettings()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CITY) {
                data ?: return
                binding.vm?.showCityWeather(data.getIntExtra(CITY_ID, 0), data.getStringExtra(CITY_NAME))
            }
        }
    }

    private fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, REQUEST_CITY)
    }

    private fun showMessage(message: String?) {
        //        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById<View>(R.id.root_layout), message!!, Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val CITY_ID = "city_id"
        const val CITY_NAME = "city_name"
        const val REQUEST_CITY = 100
    }
}