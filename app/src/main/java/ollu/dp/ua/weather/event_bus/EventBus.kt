package ollu.dp.ua.weather.event_bus

import rx.functions.Action1

/**
 * Интерфейс шины для обмена событиями внутри приложения.
 *
 * Сделана шаблоном, T - класс события. Обработчик события - интерфейс [Action1]
 *
 * Для идентификации подписчика при подписке и отписке используется параметр Object subscriber.
 *
 * Подписчик регистрируется на строковые идентификаторы событий eventNames (можно указать несколько) методом
 * [EventBus.subscribe].
 *
 * Подписчик отписывается от всех событий методом [EventBus.unsubscribe]
 *
 * События рассылаются вызовом [EventBus.send]
 */

interface EventBus<T : EventBus.Event> {
    /**
     * Подписывает вызов action на события из subjectKeys для подписчика subscriber.
     *
     * Не забыть отписаться вызовом [EventBus.unsubscribe].
     *
     * @param subscriber подписчик, используется для идентификации подписки.
     * @param action обработчик события, будет вызван при получении события.
     * @param eventNames перечень имен событий, на которые подписывается подписчик.
     */
    fun subscribe(subscriber: Any, action: Action1<T>, vararg eventNames: String)

    /**
     * Отписывает подписчика subscriber от всех событий, на которые он подписан.
     *
     * @param subscriber подписчик, который будет отписан от событий
     */
    fun unsubscribe(subscriber: Any)

    /**
     * Рассылает событие event всем подписчикам, подписаным на событие eventName.
     *
     * @param event объект - событие.
     */

    fun send(event: T)

    interface Event {
        val name: String
    }

}
