package ollu.dp.ua.weather.event_bus;

import android.support.annotation.NonNull;

import rx.functions.Action1;

/**
 * Интерфейс шины для обмена событиями внутри приложения.
 * <p>Сделана шаблоном, T - класс события. Обработчик события - интерфейс {@link Action1}
 * <p>Для идентификации подписчика при подписке и отписке используется параметр Object subscriber.
 * <p>Подписчик регистрируется на строковые идентификаторы событий eventNames (можно указать несколько) методом
 * {@link EventBus#subscribe}.
 * <p>Подписчик отписывается от всех событий методом {@link EventBus#unsubscribe}
 * <p>События рассылаются вызовом {@link EventBus#send}
 */

public interface EventBus<T extends EventBus.Event> {
    /**
     * Подписывает вызов action на события из subjectKeys для подписчика subscriber.
     * <p>Не забыть отписаться вызовом {@link EventBus#unsubscribe(Object)}.
     *
     * @param subscriber подписчик, используется для идентификации подписки.
     * @param action обработчик события, будет вызван при получении события.
     * @param eventNames перечень имен событий, на которые подписывается подписчик.
     */
    void subscribe(@NonNull Object subscriber, @NonNull Action1<T> action, @NonNull String... eventNames);

    /**
     * Отписывает подписчика subscriber от всех событий, на которые он подписан.
     *
     * @param subscriber подписчик, который будет отписан от событий
     */
    void unsubscribe(@NonNull Object subscriber);

    /**
     * Рассылает событие event всем подписчикам, подписаным на событие eventName.
     *
     * @param event объект - событие.
     */

    void send(@NonNull T event);

    interface Event {
        String getName();
    }

}
