package ollu.dp.ua.weather_test.model;

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

@SuppressWarnings("ALL")
public class WeatherData {
    public Coord coord;
    public String id;
    public String name;
    public String localName;
    public String cod;
    public Weather[] weather;
    public String base;
    public Main main;
    public int visibility;
    public Wind wind;
    public Clouds clouds;
    public long dt;
    public Sys sys;

    public static class Coord {
        public double lon;
        public double lat;
    }

    public static class Weather {
        public String description;
        public int id;
        public String main;
        public String icon;
    }

    public static class Main {
        public float temp;
        public float temp_min;
        public float temp_max;
        public int pressure;
        public int humidity;
    }

    public static class Wind {
        public float speed;
        public int deg;
    }

    public static class Clouds { //Cloudiness %
        public int all; //%
    }

    public static class Sys {
        public int type;
        public int id;
        public double message;
        public String country;
        public long sunrise;
        public long sunset;
    }
}

