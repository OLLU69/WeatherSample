package ollu.dp.ua.weather.event_bus;


import android.support.annotation.NonNull;

/**
 * Именованное событие с объектом Params, который может хранить именованные переменные различных типов:
 * int, long, string, boolean, date, Object.
 */

public class Event extends Params implements EventBus.Event {

    private final String eventName;

    public Event(@NonNull String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getName() {
        return eventName;
    }
}
