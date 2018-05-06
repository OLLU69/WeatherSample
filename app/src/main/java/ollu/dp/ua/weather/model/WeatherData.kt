package ollu.dp.ua.weather.model

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */
@Suppress("unused")
class WeatherData {
    var coord: Coord? = null
    var id: String? = null
    var name: String? = null
    var localName: String? = null
    var cod: String? = null
    @JvmField
    var weather: Array<Weather>? = null
    var base: String? = null
    @JvmField
    var main: Main? = null
    var visibility: Int = 0
    var wind: Wind? = null
    var clouds: Clouds? = null
    var dt: Long = 0
    var sys: Sys? = null

    class Coord {
        var lon: Double = 0.toDouble()
        var lat: Double = 0.toDouble()
    }

    class Weather {
        @JvmField
        var description: String? = null
        var id: Int = 0
        var main: String? = null
        var icon: String? = null
    }

    @Suppress("PropertyName")
    class Main {
        var temp: Float = 0.toFloat()
        var temp_min: Float = 0.toFloat()
        var temp_max: Float = 0.toFloat()
        var pressure: Int = 0
        var humidity: Int = 0
    }

    class Wind {
        var speed: Float = 0.toFloat()
        var deg: Int = 0
    }

    class Clouds { //Cloudiness %
        var all: Int = 0 //%
    }

    class Sys {
        var type: Int = 0
        var id: Int = 0
        var message: Double = 0.toDouble()
        var country: String? = null
        var sunrise: Long = 0
        var sunset: Long = 0
    }
}

