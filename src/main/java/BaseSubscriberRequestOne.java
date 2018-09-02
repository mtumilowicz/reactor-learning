import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class BaseSubscriberRequestOne extends BaseSubscriber<Integer> {
    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("BaseSubscriberRequestOne: subscribed");
        request(1);
    }

    @Override
    protected void hookOnNext(Integer value) {
        System.out.println("BaseSubscriberRequestOne: value " + value);
        request(1);
    }

    @Override
    protected void hookOnComplete() {
        System.out.println("BaseSubscriberRequestOne: completed");
    }

    @Override
    protected void hookOnError(Throwable throwable) {
        System.out.println("BaseSubscriberRequestOne: error " + throwable.getLocalizedMessage());
    }

    @Override
    protected void hookOnCancel() {
        System.out.println("BaseSubscriberRequestOne: cancelled");
    }
}
