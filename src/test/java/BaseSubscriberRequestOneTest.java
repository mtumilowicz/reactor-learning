import org.junit.Test;
import reactor.core.publisher.Flux;

/**
 * Created by mtumilowicz on 2018-09-02.
 */
public class BaseSubscriberRequestOneTest {
    
    @Test
    public void subscribe() throws InterruptedException {
        Flux.range(1, 4)
                .subscribe(new BaseSubscriberRequestOne());
        
        Thread.sleep(1000);
    }

    @Test
    public void error() throws InterruptedException {
        Flux.range(1, 4)
                .map(i -> {
                    if (i < 4) {
                        return i;
                    }
                    throw new IllegalStateException("error occurred!");
                })
                .subscribe(new BaseSubscriberRequestOne());

        Thread.sleep(1000);
    }
    
    @Test
    public void cancel() throws InterruptedException {
        BaseSubscriberRequestOne subscriber = new BaseSubscriberRequestOne();
        Flux.range(1, 10)
                .map(i -> {
                    if (i < 4) {
                        return i;
                    }
                    subscriber.cancel();
                    return i;
                })
                .subscribe(subscriber);

        Thread.sleep(1000);
    }

}