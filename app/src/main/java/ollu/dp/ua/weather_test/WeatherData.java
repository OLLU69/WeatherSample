package ollu.dp.ua.weather_test;

/**
 * ----
 * Created by Лукащук Олег(master) on 06.03.18.
 */

@SuppressWarnings("ALL")
class WeatherData {
    Coord coord;
    String id;
    String name;
    String localName;
    String cod;
    Weather[] weather;
    String base;
    Main main;
    int visibility;
    Wind wind;
    Clouds clouds;
    long dt;
    Sys sys;

    static class Coord {

        double lon;
        double lat;
    }

    static class Weather {
        int id;
        String main;
        String description;
        String icon;
    }

    static class Main {
        float temp;
        int pressure;
        int humidity;
        float temp_min;
        float temp_max;
    }

    static class Wind {
        float speed;
        int deg;
    }

    static class Clouds { //Cloudiness %
        int all; //%
    }

    static class Sys {
        int type;
        int id;
        double message;
        String country;
        long sunrise;
        long sunset;
    }
}

