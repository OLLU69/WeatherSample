@file:Suppress("KDocUnresolvedReference", "PackageName")

package ollu.dp.ua.weather.event_bus


import rx.Scheduler
import rx.functions.Action1
import rx.functions.Actions
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Шина для обмена событиями внутри приложения. Реализует интерфейс
 * [EventBus].
 *
 *
 * Можно управлять тем, в каком потоке будет вызываться обработчик с помощью метода
 * [RxEventBus.setScheduler].
 *
 * По умолчанию используется [Schedulers.newThread].
 */

class RxEventBus<T : EventBus.Event> : EventBus<T> {

    private val mSubjectMap = HashMap<String, PublishSubject<T>>()
    private val mSubscriptionsMap = HashMap<Any, CompositeSubscription>()
    var scheduler: Scheduler = Schedulers.newThread()

    /**
     * Получает subject по ключу или создаем, если его не было.
     * @param subjectKey имя события
     */
    private fun getSubject(subjectKey: String): PublishSubject<T> {
        var subject: PublishSubject<T>? = mSubjectMap[subjectKey]
        if (subject == null) {
            subject = PublishSubject.create()
            mSubjectMap[subjectKey] = subject
        }
        return subject!!
    }

    /**
     * Получаем CompositeSubscription по объекту-подписчику, или создаем, если его не было.
     * @param subscriber подписчик
     */
    private fun getCompositeSubscription(subscriber: Any): CompositeSubscription {
        var compositeSubscription: CompositeSubscription? = mSubscriptionsMap[subscriber]
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
            mSubscriptionsMap[subscriber] = compositeSubscription
        }
        return compositeSubscription
    }

    /**
     * Подписывает вызов action на события из subjectKeys для подписчика subscriber.
     *
     * Не забыть отписаться вызовом [RxEventBus.unsubscribe].
     * @param subscriber подписчик
     * @param action обработчик, будет вызван в потоке обработки событий при получении события.
     * @param eventNames имя события
     */
    @Synchronized
    override fun subscribe(subscriber: Any, action: Action1<T>, vararg eventNames: String) {
        for (key in eventNames) {
            @Suppress("INACCESSIBLE_TYPE")
            val subscription = getSubject(key).observeOn(scheduler).subscribe(action, Actions.empty<Throwable, Any, Any, Any, Any, Any, Any, Any, Any>())
            getCompositeSubscription(subscriber).add(subscription)
        }
    }

    /**
     * Отписывает подписчика subscriber от всех событий, на которые он подписан.
     *
     * И удаляет соответствующий CompositeSubscription.
     * @param subscriber подписчик
     */
    @Synchronized
    override fun unsubscribe(subscriber: Any) {
        val compositeSubscription = mSubscriptionsMap.remove(subscriber)
        compositeSubscription?.unsubscribe()
    }

    /**
     * Рассылает событие event всем подписчикам, подписаным на событие eventName.
     * @param event событие
     */
    @Synchronized
    override fun send(event: T) {
        getSubject(event.name).onNext(event)
    }
}
