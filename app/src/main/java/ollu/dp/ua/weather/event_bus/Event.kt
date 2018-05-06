package ollu.dp.ua.weather.event_bus

/**
 * Именованное событие с объектом Params, который может хранить именованные переменные различных типов:
 * int, long, string, boolean, date, Object.
 */

class Event(private val eventName: String) : Params(), EventBus.Event {
    override val name: String
        get() = eventName
}
