package ollu.dp.ua.weather.event_bus;


import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * Шина для обмена событиями внутри приложения. Реализует интерфейс
 * {@link EventBus}.
 *
 * <p>Можно управлять тем, в каком потоке будет вызываться обработчик с помощью метода
 * {@link RxEventBus#setScheduler}.
 * <p>По умолчанию используется {@link Schedulers#newThread()}.
 */

public final class RxEventBus<T extends EventBus.Event> implements EventBus<T> {

    private final Map<String, PublishSubject<T>> mSubjectMap = new HashMap<>();
    private final Map<Object, CompositeSubscription> mSubscriptionsMap = new HashMap<>();
    private Scheduler mScheduler = Schedulers.newThread();

    public Scheduler getScheduler() {
        return mScheduler;
    }

    public void setScheduler(@NonNull Scheduler scheduler) {
        this.mScheduler = scheduler;
    }

    /**
     * Получает subject по ключу или создаем, если его не было.
     * @param subjectKey имя события
     */
    private PublishSubject<T> getSubject(String subjectKey) {
        PublishSubject<T> subject = mSubjectMap.get(subjectKey);
        if (subject == null) {
            subject = PublishSubject.create();
            mSubjectMap.put(subjectKey, subject);
        }
        return subject;
    }

    /**
     * Получаем CompositeSubscription по объекту-подписчику, или создаем, если его не было.
     * @param subscriber подписчик
     */
    private CompositeSubscription getCompositeSubscription(Object subscriber) {
        CompositeSubscription compositeSubscription = mSubscriptionsMap.get(subscriber);
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
            mSubscriptionsMap.put(subscriber, compositeSubscription);
        }
        return compositeSubscription;
    }

    /**
     * Подписывает вызов action на события из subjectKeys для подписчика subscriber.
     * <p>Не забыть отписаться вызовом {@link RxEventBus#unsubscribe(Object)}.
     * @param subscriber подписчик
     * @param action обработчик, будет вызван в потоке обработки событий при получении события.
     * @param eventNames имя события
     */
    @Override
    public final synchronized void subscribe(@NonNull Object subscriber, @NonNull Action1<T> action, @NonNull String... eventNames) {
        for (String key : eventNames) {
            Subscription subscription = getSubject(key).observeOn(mScheduler).subscribe(action, Actions.empty());
            getCompositeSubscription(subscriber).add(subscription);
        }
    }

    /**
     * Отписывает подписчика subscriber от всех событий, на которые он подписан.
     * <p>И удаляет соответствующий CompositeSubscription.
     * @param subscriber подписчик
     */
    @Override
    public final synchronized void unsubscribe(@NonNull Object subscriber) {
        CompositeSubscription compositeSubscription = mSubscriptionsMap.remove(subscriber);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }

    /**
     * Рассылает событие event всем подписчикам, подписаным на событие eventName.
     * @param event событие
     */
    @Override
    public final synchronized void send(@NonNull T event) {
        getSubject(event.getName()).onNext(event);
    }
}
