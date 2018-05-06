package ollu.dp.ua.weather.event_bus


import rx.android.schedulers.AndroidSchedulers

/**
 * Статический класс, поставляющий экземпляр интерфейса [EventBus], реализованный в [RxEventBus]
 *
 * У экземпляр инициализируется шедулер для обработки событий и поведение при повторной подписке.
 *
 * В качестве класса события используется [Event]
 */

object BusFactory {

    val instance: EventBus<Event> by lazy {
        newInstance()
    }

    /**
     * Возвращает экземпляр интерфейса [EventBus]
     */
    private fun newInstance(): EventBus<Event> {
        val bus = RxEventBus<Event>()
        bus.scheduler = AndroidSchedulers.mainThread()
        return bus
    }


}
