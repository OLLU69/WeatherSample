package ollu.dp.ua.weather

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.Toast
import ollu.dp.ua.weather.databinding.ActivityMainBinding
import ollu.dp.ua.weather.event_bus.BusFactory
import rx.functions.Action1

/**
 * ----
 * Created by Лукащук Олег(master) on 27.04.18.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val onMessage = object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable, propertyId: Int) {

            @Suppress("UNCHECKED_CAST")
            showMessage((sender as ObservableField<String>).get())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = ViewModelProviders.of(this).get(MainActivityVM::class.java)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CITY) {

                binding.vm?.showCityWeather(data.getIntExtra(CITY_ID, 0), data.getStringExtra(CITY_NAME))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.vm?.showMessage?.addOnPropertyChangedCallback(onMessage)
        BusFactory.instance.subscribe(this, Action1 { e ->
            println(e.name)
            binding.vm?.loadData()

        }, WeatherApp.TIME_EVENT)
    }

    override fun onPause() {
        BusFactory.instance.unsubscribe(this)

        binding.vm?.showMessage?.removeOnPropertyChangedCallback(onMessage)
        super.onPause()
    }

    private fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivityForResult(intent, REQUEST_CITY)
    }

    fun showMessage(message: String?) {
        //        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById<View>(R.id.root_layout), message!!, Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val CITY_ID = "city_id"
        const val CITY_NAME = "city_name"
        const val REQUEST_CITY = 100
    }
}