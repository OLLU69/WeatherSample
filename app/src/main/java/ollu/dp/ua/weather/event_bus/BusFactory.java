package ollu.dp.ua.weather.event_bus;


import rx.android.schedulers.AndroidSchedulers;

/**
 * Статический класс, поставляющий экземпляр интерфейса {@link EventBus}, реализованный в {@link RxEventBus}
 * <p>У экземпляр инициализируется шедулер для обработки событий и поведение при повторной подписке.
 * <p>В качестве класса события используется {@link Event}
 */

public class BusFactory {

    private BusFactory() {
    }

    /**
     * Возвращает экземпляр интерфейса {@link EventBus}
     */
    private static EventBus<Event> newInstance() {
        RxEventBus<Event> bus = new RxEventBus<>();
        bus.setScheduler(AndroidSchedulers.mainThread());
        return bus;
    }

    private static EventBus<Event> instance;

    public static EventBus<Event> getInstance() {
        if (instance == null){
            instance = newInstance();
        }
        return instance;
    }


}
